package com.jayameen.zfiles.api;

import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.dto.AppResponse;
import com.jayameen.zfiles.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @author Madan KN
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ZFileAPI {

    private final FileService fileService;

    @PostMapping(value="/base64")
    public ResponseEntity<AppResponse> createNewFileUpload(@RequestBody FileRequest request) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(Collections.singletonList(fileService.createNewFileUpload(request)));
        return ResponseEntity.ok().body(appResponse);
    }

}
