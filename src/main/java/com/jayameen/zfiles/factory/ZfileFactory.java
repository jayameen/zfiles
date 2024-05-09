package com.jayameen.zfiles.factory;

import com.jayameen.zfiles.factory.adaptors.*;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileAzure;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileS3;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileGCS;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Madan KN
 */
@Component
@RequiredArgsConstructor
public class ZfileFactory {

   @Value("${zfiles.cdn-type}") private String cdnType;
   private Zfile zfile;
   private final ZfileLocal zfileLocal;
   private final ZfileGCS zfileGCS;
   private final ZfileS3 zfileS3;
    private final ZfileAzure zfileAzure;

   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public Zfile getZfile(){
       if(Objects.isNull(zfile)){  init();  }
       return zfile;
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private synchronized void init(){
       switch (cdnType) {
           case "local": this.zfile = zfileLocal; break;
           case "gcp": this.zfile = zfileGCS; break;
           case "s3": this.zfile = zfileS3; break;
           case "azure": this.zfile = zfileAzure; break;
       }
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
