package com.jayameen.zfiles.factory;

import com.jayameen.zfiles.factory.adaptors.*;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileAWS;
import com.jayameen.zfiles.factory.adaptors.impl.ZfileDigitalOcean;
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
   private final ZfileDigitalOcean zfileDigitalOcean;
   private final ZfileAWS zfileAWS;

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
           case "aws": this.zfile = zfileAWS; break;
           case "digitalocean": this.zfile = zfileDigitalOcean; break;

       }
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
