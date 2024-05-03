package com.jayameen.zfiles.factory;

import com.jayameen.zfiles.dto.FileRequest;
import org.springframework.stereotype.Component;

/**
 * @author Madan KN
 */
@Component
public class ZfileAWS /*implements Zfile*/ {

    public String createOrUpdateFromBase64Content(FileRequest request){
        return  null;
    }

    public boolean deleteFileByUrl(FileRequest request) throws Exception{
        return false;
    }
}
