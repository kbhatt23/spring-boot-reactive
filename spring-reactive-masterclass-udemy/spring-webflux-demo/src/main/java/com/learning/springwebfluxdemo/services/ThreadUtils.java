package com.learning.springwebfluxdemo.services;

public class ThreadUtils {


	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
