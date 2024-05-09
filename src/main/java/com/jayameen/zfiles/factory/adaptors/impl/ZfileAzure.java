package com.jayameen.zfiles.factory.adaptors.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.jayameen.zfiles.dto.FileRequest;
import com.jayameen.zfiles.factory.adaptors.Zfile;
import com.jayameen.zfiles.utils.ZFileUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Madan KN
 */
@Component
@RequiredArgsConstructor
public class ZfileAzure implements Zfile {


    @Value("${azure.prefix-http-url:''}") private String prefixHttpUrl;
    @Value("${azure.prefix-upload}") private String prefixUpload;
    @Value("${azure.container-name:''}") private String containerName;

    private final BlobContainerClient azureStorageContainerClient;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String createOrUpdateFromFile(FileRequest request) throws Exception {
        return createFileFromByteArray(request);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String getFileInBase64(FileRequest request) throws Exception {
        BlobClient blobClient = azureStorageContainerClient.getBlobClient(getFileKey(request));
        byte[]           blob = blobClient.downloadContent().toBytes();
        if (blob != null) {
            return Base64.getEncoder().encodeToString(blob);
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
        BlobClient blobClient = azureStorageContainerClient.getBlobClient(getFileKey(request));
        return blobClient.deleteIfExists();
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
        OffsetDateTime         expiryTime = OffsetDateTime.now().plusDays(365*10);
        String                filePathKey = ZFileUtils.createFileHttpURL(prefixUpload,request.getFilePath(),request.getFileName());
        filePathKey                       = StringUtils.removeStart(filePathKey, "/");
        BlobClient             blobClient = azureStorageContainerClient.getBlobClient(filePathKey);
        blobClient.upload(new ByteArrayInputStream(request.getByteArray()), request.getByteArray().length, true);
        if(request.getIsPrivate()) {
            return blobClient.getBlobUrl();
        }else{
            return blobClient.getBlobUrl()+"?"+blobClient.generateSas(new BlobServiceSasSignatureValues(expiryTime, new BlobSasPermission().setReadPermission(true)));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String getFileKey(FileRequest request) throws Exception{
        String  fullPath = request.getFileUrl().replaceAll(prefixHttpUrl + "/" + containerName + "/", "");
                fullPath = URLDecoder.decode(fullPath, "UTF-8");
                if(fullPath.contains("?")){
                   fullPath = fullPath.substring(0, fullPath.indexOf("?"));
                }
        return fullPath;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
