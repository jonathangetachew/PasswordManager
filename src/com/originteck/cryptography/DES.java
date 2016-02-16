
package com.originteck.cryptography;

import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;


import it.sauronsoftware.base64.Base64;

public class DES implements CryptAlgs{
	
	private final String alg = "DES";
	private final int keySize = 56;
	private Key DESKey;
	private byte[] encryptedText;
	private byte[] decryptedText;
	private final String source = "BuiltByOriginTech";
	
	public DES(){
		produceKey();
	}
	
	public Key produceKey() {

		try {		
			    DESKeySpec DKS = new DESKeySpec(source.getBytes());				//password string is inspected 
				SecretKeyFactory SKF = SecretKeyFactory.getInstance(alg);		//DES is selected
				DESKey = SKF.generateSecret(DKS);								//Key is generated pertaining to inputed string
				
		} catch(InvalidKeyException IKEex) {
			System.out.println("Invalid Key!");
		} catch(NoSuchAlgorithmException NSAex){
			System.out.println("No Such Algorithm Found!");
		} catch(InvalidKeySpecException IKEex){
			System.out.println("Invalid Key Spec Input!");
		}
		return DESKey;
	}
	
	@Override
	public Key generateKey() throws Exception {
		
		KeyGenerator KeyGen = KeyGenerator.getInstance(alg);
		KeyGen.init(keySize);
		Key key = KeyGen.generateKey();
		return key;
	}
	
	@Override
	public String encrypt(String plainText) { 
			String encryptedString = null;
			try {	
				
				Cipher DES = Cipher.getInstance("DES/ECB/PKCS5Padding");	//DES with specific blocking and padding methods is selected
				DES.init(Cipher.ENCRYPT_MODE,DESKey);						//set on Encryption Mode
				encryptedText = DES.doFinal(plainText.getBytes());			//input is encrypted and decoded
				encryptedString = Base64.encode(new String(encryptedText));
				
			} catch(InvalidKeyException IKEex) {
				System.out.println("Invalid Key!");
			} catch(NoSuchAlgorithmException NSAex){
				System.out.println("No Such Algorithm Found!");
			} catch(NoSuchPaddingException IKEex){
				System.out.println("Padding Problem!");
			} catch(IllegalBlockSizeException IBSEex){
				System.out.println("BlockSize Problem");
			} catch(BadPaddingException BPEEx) {
				System.out.println("Padding Exception");
			}
			return encryptedString;
	}
	
	@Override
	public String decrypt(String encText) {
		String decryptedString = null;	
		try {	
			
			Cipher DES = Cipher.getInstance("DES/ECB/PKCS5Padding");		//DES with specific blocking and padding methods is selected
			DES.init(Cipher.DECRYPT_MODE, DESKey);							//set on decryption Mode
			decryptedText = DES.doFinal(Base64.decode(encText).getBytes());	//input is decoded and then decrypted
			return new String(decryptedText);	
			
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
	//return encrypted data
	public String getEncryptedData() {
		return new String(encryptedText);
	}
	
	@Override
	//return decrypted data
	public String getDecryptedData() {
		return new String(decryptedText);
	}

	
}
