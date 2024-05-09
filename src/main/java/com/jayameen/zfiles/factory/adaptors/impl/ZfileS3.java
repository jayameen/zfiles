package com.jayameen.zfiles.factory.adaptors.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.adaptors.Zfile;
import com.jayameen.zfiles.utils.ZFileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Madan KN
 */
@Component
@RequiredArgsConstructor
public class ZfileS3 implements Zfile {


    @Value("${s3.prefix-http-url}") private String prefixHttpUrl;
    @Value("${s3.prefix-upload}") private String prefixUpload;
    @Value("${s3.bucket-id:''}") private String bucketID;

    private final AmazonS3 awsS3Client;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String createOrUpdateFromFile(FileRequest request) throws Exception {
        return createFileFromByteArray(request);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String getFileInBase64(FileRequest request) throws Exception {
        String             fileKey = request.getFileUrl().replace(prefixHttpUrl+"/", "");
        S3ObjectInputStream stream = awsS3Client.getObject(bucketID, fileKey).getObjectContent();
        byte []           fileData = IOUtils.toByteArray(stream);
        return Base64.getEncoder().encodeToString(fileData);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String createOrUpdateFromBase64Content(FileRequest request) throws Exception {
        request.setByteArray(Base64.getDecoder().decode(request.getBase64Data()));
        return createFileFromByteArray(request);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<String> createOrUpdateMultipleFromBase64Content(List<FileRequest> requests) throws Exception {
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
    public Boolean deleteFileByUrl(FileRequest request) throws Exception {
        String fileKey = request.getFileUrl().replace(prefixHttpUrl+"/", "");
        boolean exists = awsS3Client.doesObjectExist(bucketID, fileKey);
        if(!exists){
            throw new Exception("File Not Found");
        }
        awsS3Client.deleteObject(bucketID, fileKey);
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<Boolean> deleteMultipleFilesByUrl(List<FileRequest> requests) throws Exception {
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
    private String createFileFromByteArray(FileRequest request) {
        String                filePathKey = ZFileUtils.createFileHttpURL(prefixUpload,request.getFilePath(),request.getFileName());
        filePathKey                       = StringUtils.removeStart(filePathKey, "/");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketID, filePathKey , new ByteArrayInputStream(request.getByteArray()), new ObjectMetadata());

        if(request.getIsPrivate()){
            awsS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.Private));
        }else{
            awsS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        }
        return awsS3Client.getUrl(bucketID, filePathKey).toExternalForm();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
