package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/boards/free")
public class FileController {

    private final FileService fileService;

    // TODO: view단 작성할 때 uri에 쿼리 파라미터 방식으로 전송하도록 변경사항 적용할 것
    /**
     * fileName 파라미터를 통해 경로를 노출하지 않고 파일을 다운로드
     * @param fileName 서버상에 저장된 파일명
     * @param fileRealName 사용자가 입력한 실제 파일명
     * @return 사용자가 입력한 실제 파일명의 파일을 담은 ResponseEntity<Resource> 객체
     */
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName, @RequestParam String fileRealName) {

        HashMap<String, Object> headerAndResourece = fileService.downloadFile(fileName, fileRealName);

        Resource resource = (Resource) headerAndResourece.get("resource");
        HttpHeaders headers = (HttpHeaders) headerAndResourece.get("headers");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
