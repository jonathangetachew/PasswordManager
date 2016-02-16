package com.originteck.cryptography;

public class BasicLevel {
		//DES Algorithm
		public static String encrypt(String plainText) {
			DES DESCrypt  = new DES();
			return DESCrypt.encrypt(plainText);
		}
		public static String decrypt(String encryptedText){
			DES DESCrypt = new DES();
			return DESCrypt.decrypt(encryptedText);
		}
}
