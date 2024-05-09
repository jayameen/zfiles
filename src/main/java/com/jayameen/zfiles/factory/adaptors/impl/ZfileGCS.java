package com.jayameen.zfiles.factory.adaptors.impl;

import com.google.cloud.storage.*;
import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.adaptors.Zfile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * @author Madan KN
 */
@Component
@RequiredArgsConstructor
public class ZfileGCS implements Zfile {

    @Value("${gcp.prefix-http-url:''}") private String prefixHttpUrl;
    @Value("${gcp.prefix-upload:''}") private String prefixUpload;
    @Value("${gcp.bucket-id:''}") private String bucketID;
    @Value("${gcp.upload-url:''}") private String uploadURL;

    private final Storage gcpStorage;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String createOrUpdateFromFile(FileRequest request) throws Exception {
        return createFileFromByteArray(request);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String getFileInBase64(FileRequest request) throws Exception {
        String fullPath = request.getFileUrl().replaceAll(prefixHttpUrl + "/" + bucketID, "");
        BlobId   blobId = BlobId.of(bucketID,fullPath);
        Blob       blob = gcpStorage.get(blobId);
        if (blob != null) {
            byte[] contentBytes = blob.getContent();
            return Base64.getEncoder().encodeToString(contentBytes);
        } else {
            throw new RuntimeException("File not found");
        }
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
        String fullPath = request.getFileUrl().replaceAll(prefixHttpUrl + "/" + bucketID, "");
        BlobId   blobId = BlobId.of(bucketID, StringUtils.removeStart(fullPath,"/"));
        return gcpStorage.delete(blobId);
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
        Blob blob;
        if(StringUtils.isNotBlank(prefixUpload)){ request.setFilePath(prefixUpload + request.getFilePath()); }
        BlobId blobId  = BlobId.of(bucketID, StringUtils.removeStart(request.getFilePath(), "/") + "/" + request.getFileName());
        String fullUrl = prefixHttpUrl + "/" + bucketID + request.getFilePath() + "/" + request.getFileName();
        if (request.getIsPrivate()){
            blob = gcpStorage.create(BlobInfo.newBuilder(blobId).build(), request.getByteArray());
        }else {
            blob = gcpStorage.create(BlobInfo.newBuilder(blobId).setAcl(new ArrayList<>(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))).build(), request.getByteArray());
        }
        return blob!=null? fullUrl : "";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
