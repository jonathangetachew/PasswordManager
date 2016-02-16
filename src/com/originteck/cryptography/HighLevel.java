package com.originteck.cryptography;

public class HighLevel {
	//AES Algorithm
	public static String encrypt(String plainText) {
		AES AESCrypt  = new AES();
		return AESCrypt.encrypt(plainText);
	}
	public static String decrypt(String encryptedText) {
		AES AESCrypt = new AES();
		return AESCrypt.decrypt(encryptedText);
	}
}
