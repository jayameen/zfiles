package com.jayameen.zfiles.factory;

import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public List<String> createOrUpdateMultipleFromBase64Content(List<FileRequest> requests) throws Exception{
        List<String> responseUrls = new ArrayList<>();
        for(FileRequest req : requests ){
            responseUrls.add(createOrUpdateFromBase64Content(req));
        }
        return responseUrls;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String createOrUpdateFromBase64Content(FileRequest request) throws Exception{
        String   fileName = FileUtils.cleanFileName(request.getFileName());
        String   filePath = FileUtils.cleanFilePath(request.getFilePath());
        String uploadPath = FileUtils.cleanFilePath(prefixUpload + filePath);
        FileUtils.ensureDirectoryExists(uploadPath);

        File                        file = new File( uploadPath + File.separator + fileName);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(Base64.getDecoder().decode(request.getBase64Data().getBytes()));
            stream.flush();
        } catch (IOException e) {
            throw new Exception("Error creating file!");
        }
        return prefixFetch + filePath + "/" +file.getName();
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
            responseUrls.add(deleteFileByUrl(req));
        }
        return responseUrls;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
