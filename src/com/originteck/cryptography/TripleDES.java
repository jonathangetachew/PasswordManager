package com.originteck.cryptography;


import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.*;

import it.sauronsoftware.base64.Base64;

public class TripleDES
{
	// Creates an initialization vector (necessary for CBC mode)
	final byte[] IV = new byte[] { 12, 34, 56, 78, 90, 87, 65, 43 };
	IvParameterSpec IvParameters = new IvParameterSpec(IV);
	//used to generate TripleDES key
	byte[] encryptKey = "BuiltByOriginTeck".getBytes();
	
	
	public SecretKey produceKey()  {
		//Only 24bits retrieved from String
		//loops if not enough
		SecretKey theKey = null;
		byte[] encKey = new byte[24];
		for(int i = 0; i < 23; i++){
			encKey[i] = encryptKey[i%encryptKey.length];
		}
		
		
		try {
			// Creates a DESede key spec from the key
			 DESedeKeySpec spec = new DESedeKeySpec(encKey);
			// Gets the secret key factor for generating DESede keys
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			// Generates a DESede SecretKey object
			theKey = keyFactory.generateSecret(spec);
		} catch(InvalidKeySpecException IKSE) {
				IKSE.getStackTrace();
		} catch(NoSuchAlgorithmException NSAE) {
				NSAE.getStackTrace();
	    } catch(InvalidKeyException IKE){
	    		IKE.getStackTrace();
	    }
	   
		return theKey;
	}
	public String encrypt(String plainText)  {

		String encryptedString = null;
		try {	
			
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");	//Triple DES with CBC block and PCKC5 padding system is selected
			cipher.init(Cipher.ENCRYPT_MODE, produceKey(), IvParameters);	//EncryptMode, Key, and Initialization Vector are set 
			byte[] encrypted = cipher.doFinal(plainText.getBytes());		//encrypts input
			encryptedString = new String(Base64.encode(encrypted));					//encodes and returns encrypted data
			
		} catch(InvalidKeyException IKEex) {
			IKEex.getStackTrace();
		} catch(NoSuchAlgorithmException NSAex){
			NSAex.getStackTrace();
		} catch(NoSuchPaddingException IKEex){
			IKEex.getStackTrace();
		} catch(IllegalBlockSizeException IBSEex){
			IBSEex.getStackTrace();
		} catch(BadPaddingException BPEEx) {
			BPEEx.getStackTrace();
		} catch(InvalidAlgorithmParameterException IAPE) {
			IAPE.getStackTrace();
		}
		return encryptedString;
	}
	public String decrypt(String encryptedText) {
		String decryptedString = null;	
		try {	
			
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");					//Triple DES with CBC block and PCKC5 padding system is selected
			cipher.init(Cipher.DECRYPT_MODE, produceKey(), IvParameters);					//DecryptionMOde Key, and Initialization Vector are set
			byte[] decrypted = cipher.doFinal(Base64.decode(encryptedText.getBytes()));		//decodes and decrypts input string
			decryptedString =  new String(decrypted);
			
		} catch(InvalidKeyException IKEex) {
			IKEex.getStackTrace();
		} catch(NoSuchAlgorithmException NSAex){
			NSAex.getStackTrace();
		} catch(NoSuchPaddingException IKEex){
			IKEex.getStackTrace();
		} catch(IllegalBlockSizeException IBSEex){
			IBSEex.getStackTrace();
		} catch(BadPaddingException BPEEx) {
			BPEEx.getStackTrace();
		} catch(InvalidAlgorithmParameterException IAPE) {
			IAPE.getStackTrace();
		}
		return decryptedString;
		
	}
}


