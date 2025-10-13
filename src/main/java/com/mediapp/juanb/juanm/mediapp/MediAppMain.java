package com.mediapp.juanb.juanm.mediapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.mediapp.juanb.juanm.mediapp.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class MediAppMain {

	public static void main(String[] args) {
		SpringApplication.run(MediAppMain.class, args);
	}

}