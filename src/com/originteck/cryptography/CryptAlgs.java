package com.originteck.cryptography;

import java.security.*;

//Common Functions are outlined here
public interface CryptAlgs {
	
	Key generateKey() throws Exception;				
	String encrypt(String plainText) throws Exception;
	String decrypt(String encText) throws Exception;
	String getEncryptedData();
	String getDecryptedData();
	
}
