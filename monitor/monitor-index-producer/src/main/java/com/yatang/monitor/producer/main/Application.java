package com.yatang.monitor.producer.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by eson on 2017/11/3.
 */
@SpringBootApplication
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
