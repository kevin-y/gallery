package org.keviny.gallery.util;

import java.util.Random;

import org.springframework.util.Base64Utils;

public class RandomUtils {
	private static char[] chs = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
		'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	private static int DEFAULT_LEN = 6;
	private static int CHS_LEN = chs.length;
	
	public static String getRandomString() {
		return getRandomString(DEFAULT_LEN);
	}
	
	public static String getRandomString(int n) {
		if(n <= 0) n = DEFAULT_LEN;
		Random rand = new Random();
		StringBuilder rString = new StringBuilder();
		for(int i = 0; i < n; i++) {
			int j = rand.nextInt(CHS_LEN);
			rString.append(chs[j]);
		}
		return rString.toString();
	}
	
	public static void main(String[] args) {
		String test = "test";
		String salt = getRandomString();
		MessageDigestUtils md5 = MessageDigestUtils.getInstance("md5");
		String md5Str = md5.encode(Base64Utils.encodeToString((test + salt).getBytes()));
		System.out.println( md5Str + ":" + salt);
	}
}
