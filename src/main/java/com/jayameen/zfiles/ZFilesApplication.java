package com.jayameen.zfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource({"classpath:/application.yml"})
public class ZFilesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ZFilesApplication.class, args);
	}

}
