package com.jayameen.zfiles.factory.adaptors;

import com.jayameen.zfiles.dto.FileRequest;

import java.util.List;

/**
 * @author Madan KN
 */
public interface Zfile {

    String createOrUpdateFromFile(FileRequest request) throws Exception;

    String getFileInBase64(FileRequest request) throws Exception;

    String createOrUpdateFromBase64Content(FileRequest request) throws Exception;

    List<String> createOrUpdateMultipleFromBase64Content(List<FileRequest> request) throws Exception;

    Boolean deleteFileByUrl(FileRequest request) throws Exception;

    List<Boolean> deleteMultipleFilesByUrl(List<FileRequest> request) throws Exception;

}
