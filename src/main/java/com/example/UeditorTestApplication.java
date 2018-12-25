package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class UeditorTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UeditorTestApplication.class, args);
	}

	@Bean
	public BinaryUploader getBinaryUploader(){
		return new BinaryUploader();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//允许上传的文件最大值
		factory.setMaxFileSize("50MB"); //KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("50MB");
		return factory.createMultipartConfig();
	}
}
