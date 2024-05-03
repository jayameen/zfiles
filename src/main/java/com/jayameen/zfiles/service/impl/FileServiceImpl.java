package com.jayameen.zfiles.service.impl;

import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Madan KN
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Override
    public String createNewFileUpload(FileRequest request) {
        return "/URL";
    }

}
