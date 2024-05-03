package com.jayameen.zfiles.api;

import com.jayameen.zfiles.dto.AppResponse;
import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.ZfileFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Madan KN
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ZFileAPI {

    private final ZfileFactory factory;
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
        appResponse.setData(Collections.singletonList(factory.getZfile().createOrUpdateMultipleFromBase64Content(requests)));
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
