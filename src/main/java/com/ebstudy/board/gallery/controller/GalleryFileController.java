package com.ebstudy.board.gallery.controller;

import com.ebstudy.board.dto.FileDTO;
import com.ebstudy.board.gallery.service.GalleryFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GalleryFileController {

    private final GalleryFileService fileService;

    /**
     * fileName 파라미터를 통해 경로를 노출하지 않고 파일을 다운로드
     *
     * @param fileName     서버상에 저장된 파일명
     * @param fileRealName 사용자가 입력한 실제 파일명
     * @return 사용자가 입력한 실제 파일명의 파일을 담은 ResponseEntity<Resource> 객체
     */
    @GetMapping("/api/boards/gallery/file")
    public ResponseEntity<?> getFile(@RequestParam String fileName, @RequestParam String fileRealName) throws IOException {

        HashMap<String, Object> headerAndResourece = fileService.getFile(fileName, fileRealName);

        Resource resource = (Resource) headerAndResourece.get("resource");
        HttpHeaders headers = (HttpHeaders) headerAndResourece.get("headers");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * 이미지 파일 업로드 후 접근 가능한 url을 반환
     *
     * @param file 업로드할 이미지 파일
     * @return 업로드 이미지에 간접 접근이 가능한 url
     */
    @PostMapping("/api/boards/gallery/file")
    public ResponseEntity saveFile(@RequestPart MultipartFile file) throws IOException {
        FileDTO imageInfo = fileService.saveFile(file);
        return ResponseEntity.ok(imageInfo);
    }
}
