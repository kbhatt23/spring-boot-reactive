package com.learning.springwebdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//if max thread from tomcat is completed
//newwer request will wait for one of the old thread to be free
@RestController
@RequestMapping("/blocked")
public class BlockedThreadController {

	@GetMapping
	public String demoBlocked() {
		//time consuming task
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "jai shree ram";
	}
	
}
