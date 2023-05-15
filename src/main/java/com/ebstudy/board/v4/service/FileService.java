package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.FileDTO;
import com.ebstudy.board.v4.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    @Value("${spring.servlet.multipart.location}")
    String basicPath;

    /**
     * 파일 리스트를 가져오는 메소드
     *
     * @param postId 가져올 파일 리스트가 포함된 게시글의 id
     * @return 파일 리스트
     */
    public List<FileDTO> getFileList(Long postId) {
        List<FileDTO> fileList = fileMapper.getFileList(postId);
        return fileList;
    }

    /**
     * 파일의 논리정보를 DB에, 실제 파일을 서버의 파일 저장소 폴더에 저장
     *
     * @param postId            저장할 파일의 부모 게시글 id
     * @param multipartFileList MultipartFile로 변환된 파일 리스트
     * @throws IOException transferTo 메소드에서 발생할 수 있는 예외
     */
    public void saveFile(Long postId, List<MultipartFile> multipartFileList) throws IOException {

        if (multipartFileList == null) {
            return;
        }

        for (MultipartFile multipartFile : multipartFileList) {
            if (!(multipartFile.getSize() > 0)) {
                continue;
            }

            FileDTO file = new FileDTO();

            file.setPostId(postId);
            file.setFileName(UUID.randomUUID() + "_" + multipartFile.getOriginalFilename());
            file.setFileRealName(multipartFile.getOriginalFilename());
            file.setExtension(multipartFile.getContentType());
            file.setSize(multipartFile.getSize());

            File fileName = new File(file.getFileName());

            multipartFile.transferTo(fileName);

            fileMapper.saveFile(file);
        }
    }

    /**
     * 입력된 파일 리스트를 참조하여 현재 DB와 파일 저장소의 파일을 수정
     *
     * @param postId            수정할 파일들의 부모 게시글 id
     * @param deliveryFiles     입력된 기존 파일
     * @param multipartFileList 새로 입력할 파일(수정됨, 혹은 추가)
     * @throws IOException
     */
    public void updateFile(Long postId, List<FileDTO> deliveryFiles, List<MultipartFile> multipartFileList) throws IOException {

        // 기존 파일 리스트 로딩
        List<FileDTO> originFiles = fileMapper.getFileList(postId);

        deleteFileNotExistsDatabase(deliveryFiles, originFiles);

        // 기존 파일 외 신규 입력되는 파일에 대해 저장
        saveFile(postId, multipartFileList);
    }

    /**
     * 수정 시 입력으로 전송된 FileDTO 리스트에 해당하지 않는 파일들을 DB 및 파일 저장소에서 제거
     * @param deliveryFiles 게시글에서 보낸 기존 파일 리스트
     * @param originFiles DB상의 기존 파일 리스트
     */
    private void deleteFileNotExistsDatabase(List<FileDTO> deliveryFiles, List<FileDTO> originFiles) {

        // TODO: Depth를 줄일 수 있을지 고민해 보자.
        if (originFiles != null) { // DB에 애초에 파일이 없으면 전부 저장만 하면 됨
            outerLoop:
            for (FileDTO originFile : originFiles) {
                if (deliveryFiles != null) { // 전달된 기존 파일 리스트가 없다면 대조하지 않고 전부 삭제후 다시 저장하면 됨
                    for (FileDTO deliveryFile : deliveryFiles) {
                        if (originFile.getFileName().equals(deliveryFile.getFileName())) {
                            continue outerLoop;
                        }
                    }
                }
                deletFile(originFile);
            }
        }
    }

    /**
     * 요청한 파일을 DB와 실제 파일 저장소에서 모두 삭제한다.
     * @param file 전달한 파일의 정보
     */
    public void deletFile(FileDTO file) {

        String filePath = basicPath + file.getFileName();
        File targetFile = new File(filePath);

        // TODO: 2023/03/18 파일 삭제 과정 중 오류가 발생할 때의 상황을 고려해야 한다. 
        if (targetFile.exists()) {
            targetFile.delete();
        }

        fileMapper.deleteFile(file.getFileId());
    }

    /**
     * fileName을 주소에서 입력받아 실제 파일을 저장소에서 전달하는 메소드
     *
     * @param fileName     서버에서 변환된 파일명
     * @param fileRealName 사용자가 실제로 입력한 파일명
     * @return
     */
    public HashMap<String, Object> downloadFile(String fileName, String fileRealName) throws IOException {

        String requestPath = basicPath + fileName;
        log.info("requestPath : " + requestPath);

        Path filePath = Paths.get(requestPath);
        Resource resource = null;

        resource = new InputStreamResource(Files.newInputStream(filePath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(fileRealName, StandardCharsets.UTF_8)
                .build());

        HashMap<String, Object> headerAndResource = new HashMap<>();
        headerAndResource.put("resource", resource);
        headerAndResource.put("headers", headers);

        return headerAndResource;
    }

}
