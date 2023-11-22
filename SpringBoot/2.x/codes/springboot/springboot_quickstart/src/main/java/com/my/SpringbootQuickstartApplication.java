package com.my;

import com.my.controller.BookController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SpringbootQuickstartApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringbootQuickstartApplication.class, args);
		BookController bean = context.getBean(BookController.class);
		System.out.println(bean);
	}

}
