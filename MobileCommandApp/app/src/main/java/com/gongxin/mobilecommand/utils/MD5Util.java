package com.gongxin.mobilecommand.utils;

import java.security.MessageDigest;

public class MD5Util {

	public static String string2MD5(String s) throws Exception {
		StringBuffer stringBuffer = new StringBuffer(32);
		MessageDigest md    = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(s.getBytes("utf-8"));
		for (int i = 0; i < array.length; i++) {
			stringBuffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
		}
		return stringBuffer.toString();
	}


	public static String convertMD5(String inStr){

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

}
