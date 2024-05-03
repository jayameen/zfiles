package com.jayameen.zfiles.factory;

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
   private final ZfileDigitalOcean zfileDigitalOcean;
   private final ZfileAWS zfileAWS;
   private final ZfileGCP zfileGCP;

   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public Zfile getZfile(){
       if(Objects.isNull(zfile)){  init();  }
       return zfile;
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private synchronized void init(){
       switch (cdnType) {
           case "local": this.zfile = zfileLocal; break;
           //case "digitalocean": this.zfile = zfileDigitalOcean; break;
           //case "gcp": this.zfile = zfileGCP; break;
           //case "aws": this.zfile = zfileAWS; break;
       }
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}