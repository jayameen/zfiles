package com.jayameen.zfiles.service;

import com.jayameen.zfiles.dto.FileRequest;

public interface FileService {

    String createNewFileUpload(FileRequest request) throws Exception;

}
