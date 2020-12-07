package com.learning.springreactive;

import org.junit.After;

public class BaseTest {
	public static <T> void findSuccessConsumer(T element) {
		System.out.println("Found Element " + element);
	}

	public static <T extends Throwable> void findErrorConsumer(T error) {
		System.err.println("Exception occurred " + error.getMessage());
	}

	public static void findCompletedConsumer() {
		System.out.println("Complete event recieved");
	}

	@After
	public void printSpaceAfterEveryTest() {
		System.out.println("==========================");
	}

	public static void sleep(long ms) {
		// we will recive items only until main is avaialbel and hence wont recive
		// complete event at all
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
