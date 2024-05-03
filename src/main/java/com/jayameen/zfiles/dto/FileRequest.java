package com.jayameen.zfiles.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Madan KN
 */

@RequiredArgsConstructor
public @Data class FileRequest implements Serializable {

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("is_private")
    private String isPrivate;

    @JsonProperty("base64_data")
    private String base64Data;

}
