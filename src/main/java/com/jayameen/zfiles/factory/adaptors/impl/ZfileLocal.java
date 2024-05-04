package com.jayameen.zfiles.factory.adaptors.impl;

import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.adaptors.Zfile;
import com.jayameen.zfiles.utils.ZFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Madan KN
 */
@Component
public class ZfileLocal implements Zfile {

    @Value("${local.prefix-fetch}") private String prefixFetch;
    @Value("${local.prefix-upload}") private String prefixUpload;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String createOrUpdateFromFile(String filePath, String fileName, byte [] content) throws Exception{
        File file = new File(ZFileUtils.creatAbsoluteFilePath(prefixUpload, filePath, fileName));
        FileUtils.writeByteArrayToFile(file, content);
        return ZFileUtils.createFileHttpURL(prefixFetch,filePath,fileName);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getFileInBase64(FileRequest request) throws Exception {
        if(StringUtils.isBlank(request.getFileName())){
            throw new Exception("Invalid File Url");
        }else{
            String fetchFileName = request.getFileName();
            if(fetchFileName.trim().startsWith(prefixFetch)){
                fetchFileName = fetchFileName.replace(prefixFetch, "");
            }
            File file = new File(prefixUpload+fetchFileName);
            return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<String> createOrUpdateMultipleFromBase64Content(List<FileRequest> requests) throws Exception{
        List<String> responseUrls = new ArrayList<>();
        for(FileRequest req : requests ){
            try {
                responseUrls.add(createOrUpdateFromBase64Content(req));
            }catch (Exception ex){
                responseUrls.add(null);
            }
        }
        return responseUrls;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String createOrUpdateFromBase64Content(FileRequest request) throws Exception{
        File file = new File(ZFileUtils.creatAbsoluteFilePath(prefixUpload, request.getFilePath(), request.getFileName()));
        FileUtils.writeByteArrayToFile(file, Base64.getDecoder().decode(request.getBase64Data().getBytes()));
        return ZFileUtils.createFileHttpURL(prefixFetch,request.getFilePath(),request.getFileName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Boolean deleteFileByUrl(FileRequest request) throws Exception{
        if(StringUtils.isBlank(request.getFileName())){
            throw new Exception("Invalid File Url");
        }else{
            String deleteFilePath = request.getFileName();
            if(deleteFilePath.trim().startsWith(prefixFetch)){
                deleteFilePath = deleteFilePath.replace(prefixFetch, "");
            }
            File file = new File(prefixUpload+deleteFilePath);
            return file.delete();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Boolean> deleteMultipleFilesByUrl(List<FileRequest> requests) throws Exception{
        List<Boolean> responseUrls = new ArrayList<>();
        for(FileRequest req : requests ){
           try{
               responseUrls.add(deleteFileByUrl(req));
           }catch (Exception ex){
               responseUrls.add(false);
           }
        }
        return responseUrls;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
