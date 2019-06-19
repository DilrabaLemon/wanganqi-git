package com.boye.common.utils;

public class NumberRandomUtils {
	
	public static String numberRandom(int size) {
		String result = "";
		for (int i = 0; i < size; i ++) {
			int number = (int) (Math.random() * 10);
			result = result + number;
		}
		return result;
	}
}
