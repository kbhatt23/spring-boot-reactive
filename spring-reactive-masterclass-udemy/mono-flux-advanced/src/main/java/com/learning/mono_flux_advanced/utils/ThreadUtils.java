package com.learning.mono_flux_advanced.utils;

public class ThreadUtils {

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
