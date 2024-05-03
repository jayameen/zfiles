package com.jayameen.zfiles.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Madan KN
 */
public class FileUtils {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String cleanFilePath(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            filePath = filePath.replaceAll("[^a-zA-Z0-9\\/]", "");
        }
        if (ObjectUtils.isEmpty(filePath)) {
            filePath = "/";
        }
        if (StringUtils.isNotBlank(filePath) && !filePath.startsWith("/")) {
            filePath = "/" + filePath.trim();
        }
        if (!StringUtils.isEmpty(filePath) && filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return filePath.trim();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void ensureDirectoryExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String cleanFileName(String fileName) {
        if (!StringUtils.isEmpty(fileName)) {
            fileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "");
        }
        return fileName;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
