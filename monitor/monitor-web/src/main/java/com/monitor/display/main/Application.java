package com.monitor.display.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by eson on 2018/1/11.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
