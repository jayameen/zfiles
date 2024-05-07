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

    @Value("${local.prefix-http-url:''}") private String prefixHttpUrl;
    @Value("${local.prefix-upload:''}") private String prefixUpload;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String createOrUpdateFromFile(FileRequest request) throws Exception{
        File file = new File(ZFileUtils.creatAbsoluteFilePath(prefixUpload, request.getFilePath(), request.getFileName()));
        FileUtils.writeByteArrayToFile(file, request.getByteArray());
        return ZFileUtils.createFileHttpURL(prefixHttpUrl,request.getFilePath(),request.getFileName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String getFileInBase64(FileRequest request) throws Exception {
        if(StringUtils.isBlank(request.getFileUrl())){
            throw new Exception("Invalid File Url");
        }else{
            String fetchFileName = request.getFileUrl();
            if(fetchFileName.trim().startsWith(prefixHttpUrl)){
                fetchFileName = fetchFileName.replace(prefixHttpUrl, "");
            }
            File file = new File(prefixUpload+fetchFileName);
            if(file.exists()){
                return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
            }
        }
        throw new Exception("File Not Found");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
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
    @Override
    public String createOrUpdateFromBase64Content(FileRequest request) throws Exception{
        File file = new File(ZFileUtils.creatAbsoluteFilePath(prefixUpload, request.getFilePath(), request.getFileName()));
        FileUtils.writeByteArrayToFile(file, Base64.getDecoder().decode(request.getBase64Data().getBytes()));
        return ZFileUtils.createFileHttpURL(prefixHttpUrl,request.getFilePath(),request.getFileName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Boolean deleteFileByUrl(FileRequest request) throws Exception{
        if(StringUtils.isBlank(request.getFileUrl())){
            throw new Exception("Invalid File Url");
        }else{
            String deleteFilePath = request.getFileUrl();
            if(deleteFilePath.trim().startsWith(prefixHttpUrl)){
                deleteFilePath = deleteFilePath.replace(prefixHttpUrl, "");
            }
            File file = new File(prefixUpload+deleteFilePath);
            return file.delete();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
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
