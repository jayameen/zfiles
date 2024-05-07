package com.jayameen.zfiles.factory.adaptors.impl;

/**
 * @author Madan KN
 */

import com.jayameen.zfiles.dto.FileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@SpringBootTest
@Profile("local-digitalocean")
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = { "classpath:application.yml"})
public class ZfileLocalTest {

    @Value("${local.prefix-fetch}") private String prefixFetch;
    @Value("${local.prefix-upload}") private String prefixUpload;

    @InjectMocks
    private ZfileLocal zfileLocal;

    @Mock
    private FileRequest fileRequest;

    @BeforeEach
    public void setup() {
        System.out.println(">>>>>>>>>>>>>> "+prefixFetch);
        System.out.println(">>>>>>>>>>>>>> "+prefixUpload);
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(zfileLocal, "prefixFetch", "prefixFetch");
        ReflectionTestUtils.setField(zfileLocal, "prefixUpload", "prefixUpload");
    }

    @Test
    public void createOrUpdateFromFileHappyPath() throws Exception {
        String result = zfileLocal.createOrUpdateFromFile(fileRequest);
        assertNotNull(result);
    }

    @Test
    public void getFileInBase64HappyPath() throws Exception {
        when(fileRequest.getFileName()).thenReturn("fetchPrefixfileName");
        String result = zfileLocal.getFileInBase64(fileRequest);
        assertNotNull(result);
    }

    @Test
    public void getFileInBase64InvalidFileUrl() {
        when(fileRequest.getFileName()).thenReturn("");
        assertThrows(Exception.class, () -> zfileLocal.getFileInBase64(fileRequest));
    }

    @Test
    public void createOrUpdateMultipleFromBase64ContentHappyPath() throws Exception {
        List<FileRequest> requests = Arrays.asList(fileRequest, fileRequest);
        List<String> result = zfileLocal.createOrUpdateMultipleFromBase64Content(requests);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void createOrUpdateFromBase64ContentHappyPath() throws Exception {
        when(fileRequest.getFilePath()).thenReturn("filePath");
        when(fileRequest.getFileName()).thenReturn("fileName");
        when(fileRequest.getBase64Data()).thenReturn("base64Data");
        String result = zfileLocal.createOrUpdateFromBase64Content(fileRequest);
        assertNotNull(result);
    }

    @Test
    public void deleteFileByUrlHappyPath() throws Exception {
        when(fileRequest.getFileName()).thenReturn("fetchPrefixfileName");
        Boolean result = zfileLocal.deleteFileByUrl(fileRequest);
        assertNotNull(result);
    }

    @Test
    public void deleteFileByUrlInvalidFileUrl() {
        when(fileRequest.getFileName()).thenReturn("");
        assertThrows(Exception.class, () -> zfileLocal.deleteFileByUrl(fileRequest));
    }

    @Test
    public void deleteMultipleFilesByUrlHappyPath() throws Exception {
        List<FileRequest> requests = Arrays.asList(fileRequest, fileRequest);
        List<Boolean> result = zfileLocal.deleteMultipleFilesByUrl(requests);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}