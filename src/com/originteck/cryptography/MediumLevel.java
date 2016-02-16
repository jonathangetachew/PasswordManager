package com.originteck.cryptography;


public class MediumLevel {
		//TripleDES Algorithm
		public static String encrypt(String plainText){
			TripleDES DESede  = new TripleDES();
			return DESede.encrypt(plainText);
		}
		public static String decrypt(String encryptedText)  {
			TripleDES DESede  = new TripleDES();
			return DESede.decrypt(encryptedText);
		}
}
