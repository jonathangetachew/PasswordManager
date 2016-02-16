
package com.originteck.cryptography;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import it.sauronsoftware.base64.Base64;



public class AES implements CryptAlgs{
	
	private final String alg = "AES";		//Encryption Algorithm
	private final int keySize = 256;		//Encryption KeySize
	private Key AESKey;						//Encryption Key
	private byte[] encryptedText;			//Holds encryptedData
	private byte[] decryptedText;			//Holds decryptedData
	private final byte[] source = "BuiltByOriginTeck".getBytes();		//is used to generate password
	
	//The class generates key first in every instances
	public AES() {
		produceKey();
	}
	
	//generates 128 bits hash array
	public byte[] getHash(){
		byte[] hash = null;
		try {
			MessageDigest hashInput = MessageDigest.getInstance("MD5");
			hash = hashInput.digest(source);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
		return hash;
	}
	
	//produces appropriate key from previously calculated hash
	public Key produceKey()  {
		
		AESKey = new SecretKeySpec(getHash(),alg);
		return AESKey;
	}

	@Override
	//This function is not used
	//generates key using KeyGenerator class
	public Key generateKey() throws Exception {
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES/ECB/PKCS5Padding");		//AES is selected
		keyGen.init(keySize);										//128 bit key is selected
		Key key = keyGen.generateKey();								//generated key is saved on key variable
		return key;
	}
	
	@Override
	public String encrypt(String plainText) {
		String encryptedString = null;
		try {	
			
			Cipher AES = Cipher.getInstance("AES/ECB/PKCS5Padding");				//Algorithm selected
			AES.init(Cipher.ENCRYPT_MODE, AESKey);			    //Initialized with Encrypt Mode and Key
			encryptedText = AES.doFinal(plainText.getBytes());	//Input string is encrypted
			encryptedString=Base64.encode(new String(encryptedText));	//encrypted data is encoded with BASE64 encoding system
			
		} catch(InvalidKeyException IKEex) {
			System.out.println("Invalid Key!");
		} catch(NoSuchAlgorithmException NSAex){
			System.out.println("No Such Algorithm Found!");
		} catch(NoSuchPaddingException IKEex){
			System.out.println("Padding Problem!");
		} catch(IllegalBlockSizeException IBSEex){
			System.out.println("BlockSize Problem");
		} catch(BadPaddingException BPEEx) {
			System.out.println("Badding Exception");
		}
		return encryptedString;
		
		
	}
	
	@Override
	public String decrypt(String encText){
		String decryptedString = null;	
		try {	
			
			Cipher AES = Cipher.getInstance(alg);						//Algorithm selected
			AES.init(Cipher.DECRYPT_MODE, AESKey);						//Initialized with decrypt  Mode and Key
			String decodedString = Base64.decode(encText);				//Input string is decoded
			decryptedText = AES.doFinal(decodedString.getBytes());	    //decoded text is then again decrypted
			decryptedString = new String(decryptedText);
			
		} catch(InvalidKeyException IKEex) {
			System.out.println("Invalid Key!");
		} catch(NoSuchAlgorithmException NSAex){
			System.out.println("No Such Algorithm Found!");
		} catch(NoSuchPaddingException IKEex){
			System.out.println("Padding Problem!");
		} catch(IllegalBlockSizeException IBSEex){
			System.out.println("BlockSize Problem");
		} catch(BadPaddingException BPEEx) {
			System.out.println("Badding Exception");
		}
		return decryptedString;
		
	}
	
	@Override
	//returns encrypted data
	public String getEncryptedData() {
		return new String(encryptedText);
	}
	
	//returns decrypted data
	public String getDecryptedData() {
		return new String(decryptedText);
	}
}
