package com.jayameen.zfiles.api;

import com.jayameen.zfiles.dto.AppResponse;
import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.ZfileFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * @author Madan KN
 */

@RestController
@RequestMapping("/api/object")
@RequiredArgsConstructor
public class ZFileAPI {

    private final ZfileFactory factory;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/file", method = { RequestMethod.POST, RequestMethod.PUT })
    public ResponseEntity<AppResponse> createOrUpdateFromFile(@RequestParam("file") MultipartFile file, @RequestParam("file_path") String uploadPath, @RequestParam("is_private") Boolean isPrivate) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        FileRequest request     = new FileRequest();
        request.setFileName(file.getOriginalFilename());
        request.setFilePath(uploadPath);
        request.setIsPrivate(isPrivate);
        request.setByteArray(file.getBytes());
        appResponse.setData(Collections.singletonList(factory.getZfile().createOrUpdateFromFile(request)));
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/base64")
    public ResponseEntity<AppResponse> getFileInBase64(@RequestBody FileRequest request) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(Collections.singletonList(factory.getZfile().getFileInBase64(request)));
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/base64", method = { RequestMethod.POST, RequestMethod.PUT })
    public ResponseEntity<AppResponse> createOrUpdateFromBase64Content(@RequestBody FileRequest request) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(Collections.singletonList(factory.getZfile().createOrUpdateFromBase64Content(request)));
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/base64/multiple", method = { RequestMethod.POST, RequestMethod.PUT })
    public ResponseEntity<AppResponse> createOrUpdateMultipleFromBase64Content(@RequestBody List<FileRequest> requests) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(factory.getZfile().createOrUpdateMultipleFromBase64Content(requests));
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping
    public ResponseEntity<AppResponse> deleteFileByUrl(@RequestBody FileRequest request) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(Collections.singletonList(factory.getZfile().deleteFileByUrl(request)));
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/multiple")
    public ResponseEntity<AppResponse> deleteMultipleFilesByUrl(@RequestBody List<FileRequest> request) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        appResponse.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        appResponse.setData(Collections.singletonList(factory.getZfile().deleteMultipleFilesByUrl(request)));
        return ResponseEntity.ok().body(appResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
