package com.learning.springreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringReactiveApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringReactiveApplication.class, args);
		TestBean bean = context.getBean(TestBean.class);
		System.out.println(new TestBean("messi",23));
		bean.setAge(32);
		System.out.println(bean);
	}

}
