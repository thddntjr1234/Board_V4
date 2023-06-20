package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.FileDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.global.exception.CustomErrorCode;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.repository.GalleryFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryFileService {

    private final GalleryFileMapper fileMapper;

    @Value("${spring.servlet.multipart.location}")
    String localStoragePath;

    @Value("${spring.servlet.multipart.location}" + "/temp/")
    String tempStoragePath;

    @Value("${file.access.path}")
    String fileAccessPath;

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
     * @param multipartFile MultipartFile로 변환된 파일 리스트
     * @throws IOException transferTo 메소드에서 발생할 수 있는 예외
     */
    public FileDTO saveFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        // 파일 정보 입력
        FileDTO file = new FileDTO();

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        file.setFileName(fileName);
        file.setFileRealName(multipartFile.getOriginalFilename());
        file.setExtension(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());

        // 파일 저장 시 가상 디렉토리에 먼저 저장한다.
        String tempStoragePath = "temp/" + file.getFileName();
        File storageFile = new File(tempStoragePath);

        multipartFile.transferTo(storageFile);

        // DB상에 해당 파일의 정보를 저장
        fileMapper.saveFile(file);

        // 이미지 접근 주소 설정
        String imgAccessUrl = fileAccessPath + "api/boards/gallery/file?fileName=" + fileName + "&fileRealName=" + multipartFile.getOriginalFilename();
        file.setImgAccessUrl(imgAccessUrl);

        return file;
    }

    /**
     * 입력된 파일 리스트를 참조하여 현재 DB와 파일 저장소의 파일을 수정
     *
     * @param postId          수정할 파일들의 부모 게시글 id
     * @param registeredFiles 게시글에 실제로 등록된 파일 리스트
     * @throws IOException
     */
    public void updateFiles(Long postId, List<FileDTO> registeredFiles, List<FileDTO> originFiles) throws IOException {

        // 게시글 ID값이 부여된 파일 중 더이상 사용하지 않는 파일을 삭제한다
        deleteUnusedFiles(registeredFiles, originFiles);

        // 새로 등록한 게시글에 대해 게시글 ID를 부여하고 해당 파일들을 임시 폴더에서 로컬 저장소로 이동한다.
        updateRegisteredFilesWithPostId(postId, registeredFiles);
        moveTempFileToLocalStorage(registeredFiles);
    }

    /**
     * 게시글 수정 시 사용된 이미지 리스트를 참고하여 기존 등록한 이미지 중 사용되지 않는 이미지를 삭제한다.
     *
     * @param registeredFiles 게시글에 실제로 등록된 파일 리스트
     * @param originFiles     DB상의 기존 파일 리스트
     */
    private void deleteUnusedFiles(List<FileDTO> registeredFiles, List<FileDTO> originFiles) {

        // [file, null, file]과 같이 전달된 경우 NPE가 발생할 수 있으므로 이를 제거
        registeredFiles.removeAll(Collections.singletonList(null));

        if (originFiles.isEmpty()) {
            return;
        }

        // registeredFiles에서 UUID가 추가된 파일명만 분리한 리스트를 생성
        List<String> registeredFileNames = registeredFiles.stream()
                .map(FileDTO::getFileName)
                .collect(Collectors.toList());

        // originFiles를 순회하며 registeredFileNames에 없는 파일들을 삭제
        originFiles.stream()
                .filter(originFile -> !registeredFileNames.contains(originFile.getFileName()))
                .forEach(this::deleteFile);
    }

    /**
     * 요청한 파일을 DB와 실제 파일 저장소에서 모두 삭제한다.
     *
     * @param file 전달한 파일의 정보
     */
    public void deleteFile(FileDTO file) {

        String filePath = localStoragePath + file.getFileName();
        File targetFile = new File(filePath);

        // 삭제 과정중 발생하는 오류에 대해 처리할 수 있도록 수정
        if (targetFile.exists()) {
            if (!targetFile.delete()) {
                throw new CustomException(CustomErrorCode.FAILD_FILE_DELETE);
            }
        }

        fileMapper.deleteFile(file.getFileId());
    }

    /**
     * DB에서 썸네일 파일 정보를 삭제한다.
     * @param fileId
     */
    public void deleteThumbnail(Long fileId) {
        fileMapper.deleteFile(fileId);
    }

    /**
     * fileName, fileRealName을 주소에서 입력받아 실제 파일을 저장소에서 전달하는 메소드
     *
     * @param fileName     서버에서 변환된 파일명
     * @param fileRealName 사용자가 실제로 입력한 파일명
     * @return
     */
    public HashMap<String, Object> getFile(String fileName, String fileRealName) throws IOException {

        String requestPath = localStoragePath + fileName;
        Path filePath = Paths.get(requestPath);

        Resource resource = null;

        // local storage에서 파일을 조회했을 때 NoSuchFileException이 발생하는 경우 임시 폴더에서 다시 파일을 조회하고
        // 임시 폴더에서도 찾을 수 없어 NoSuchFileException이 발생하게 되면 조회할 수 없는 파일을 요청한 것이므로 404 커스텀 에러를 반환하도록 한다.
        try {
            resource = new InputStreamResource(Files.newInputStream(filePath));
        } catch (NoSuchFileException local) {
            requestPath = tempStoragePath + fileName;
            filePath = Paths.get(requestPath);

            try {
                resource = new InputStreamResource(Files.newInputStream(filePath));
            } catch (NoSuchFileException temp) {
                throw new CustomException(CustomErrorCode.NOT_FOUND);
            }
        }

        // 반환할 헤더와 리소스를 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(fileRealName, StandardCharsets.UTF_8)
                .build());

        HashMap<String, Object> headerAndResource = new HashMap<>();
        headerAndResource.put("resource", resource);
        headerAndResource.put("headers", headers);

        return headerAndResource;
    }

    /**
     * DB에서 실제 등록된 파일의 post_id 외래키 컬럼의 값을 게시글 id로 업데이트
     *
     * @param postId             파일이 등록된 게시글의 ID
     * @param registeredFileList 실제로 등록된 파일 리스트
     */
    public void updateRegisteredFilesWithPostId(Long postId, List<FileDTO> registeredFileList) {
        for (FileDTO file : registeredFileList) {
            if (file.getPostId() == null) {
                fileMapper.updatePostIdFromFile(file.getFileId(), postId);
            }
        }
    }

    /**
     * 실제 등록된 파일의 위치를 임시 폴더에서 로컬 스토리지로 이동
     *
     * @param registerdFileList 실제 등록된 파일 리스트
     */
    public void moveTempFileToLocalStorage(List<FileDTO> registerdFileList) throws IOException {
        for (FileDTO file : registerdFileList) {
            if (file.getPostId() == null) {
                Path temp = Path.of(tempStoragePath + file.getFileName());
                Path local = Path.of(localStoragePath + file.getFileName());
                Files.move(temp, local);
            }
        }
    }

    /**
     * 게시글에 첫 번째로 등록된 이미지 파일을 썸네일로 만들어 DB 및 파일 저장소에 저장한 후 해당 썸네일의 PK값을 반환한다.
     *
     * @param postId
     * @param registeredFileList 게시글에 실제로 등록된 이미지 파일 리스트
     * @return fileId 저장한 썸네일 이미지 정보의 PK
     * @throws IOException
     */
    public Long saveThumbnailImage(Long postId, List<FileDTO> registeredFileList) throws IOException {

        // 첫 번째로 등록된 이미지를 썸네일 이미지로 지정한다.
        FileDTO image = registeredFileList.get(0);

        // 썸네일 이미지로 저장하기 위해 로컬 저장소에서 이미지를 가져와 리사이징 후 로컬 저장소에 thumb_로 시작하도록 prefix를 붙여 저장한다.
        File file = new File(localStoragePath, image.getFileName());
        Thumbnails
                .of(file)
                .forceSize(80, 90)
                .toFile(new File(localStoragePath, "thumb_" + image.getFileName()));

        // 저장한 이미지를 불러와 리사이징된 크기와 썸네일 이미지 이름을 구한다.
        File thumbImage = new File(localStoragePath, "thumb_" + image.getFileName());
        Long thumbImageSize = thumbImage.length();
        String thumbImageName = thumbImage.getName();

        // 썸네일 이미지에 접근할 수 있는 경로를 생성한다.
        String thumbnailAccessUrl = fileAccessPath + "api/boards/gallery/file?fileName=" + thumbImageName + "&fileRealName=" + image.getFileRealName();

        // 첫번째로 등록된 이미지 정보를 일부 재사용하기 위해 정보를 덮어씌운다.
        image.setSize(thumbImageSize);
        image.setFileName(thumbImageName);
        image.setPostId(postId);
        image.setImgAccessUrl(thumbnailAccessUrl);

        // 썸네일 정보와 게시글 ID값을 저장한다.
        fileMapper.saveThumbnail(image);

        return image.getFileId();
    }
}
