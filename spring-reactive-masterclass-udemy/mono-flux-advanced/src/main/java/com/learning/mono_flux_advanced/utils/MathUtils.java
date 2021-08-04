package com.learning.mono_flux_advanced.utils;

public class MathUtils {

	public static boolean isEven(int i) {
		return i % 2 == 0;
	}
	
	public static double getRandomNumber(double min, double max) {
	    return ((Math.random() * (max - min)) + min);
	}
	
	
	public static int getRandomNumber(int min, int max) {
	    return (int)((Math.random() * (max - min)) + min);
	}
}
