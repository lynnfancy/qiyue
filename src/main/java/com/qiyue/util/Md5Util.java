package com.qiyue.util;

import java.security.MessageDigest;

import com.qiyue.interf.encrypt.EncryptInterface;

public class Md5Util implements EncryptInterface{
	public String encrypt(String inStr) throws Exception {
		MessageDigest md5 = null;

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception arg6) {
			arg6.printStackTrace();
			return "";
		}

		byte[] byteArray = inStr.getBytes("UTF-8");
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; ++i) {
			int val = md5Bytes[i] & 255;
			if (val < 16) {
				hexValue.append("0");
			}

			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
}
