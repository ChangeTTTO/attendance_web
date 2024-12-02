package com.pn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pn.mapper")
public class AttendanceWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceWebApplication.class, args);
	}

}
