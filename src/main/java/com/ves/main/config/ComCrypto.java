package com.ves.main.config;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ComCrypto {
	public static String encrypt(final String message, final String SECRET_KEY) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("sha1");
		final byte[] digestOfPassword = md.digest(SECRET_KEY.getBytes());
		final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}

		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		final byte[] plainTextBytes = message.getBytes();
		final byte[] cipherText = cipher.doFinal(plainTextBytes);

		return new String(Base64.encodeBase64(cipherText));
	}

	public static String decrypt(final String message, final String SECRET_KEY) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("sha1");
		final byte[] digestOfPassword = md.digest(SECRET_KEY.getBytes());
		final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}

		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		final byte[] plainText = decipher.doFinal(Base64.decodeBase64(message.getBytes()));

		return new String(plainText, "UTF-8");
	}

	/**
	 * Örnek
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("123456", "b9d17ad0-daeb-11e3-9c1a-0800200c9a66"));
		System.out.println(decrypt("cnG6LxLuVMg=", "223b6e1c-a7b4-4567-8d49-ec993b82bb3c"));
	}
}
