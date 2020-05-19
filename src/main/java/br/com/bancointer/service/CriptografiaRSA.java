package br.com.bancointer.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Service
public class CriptografiaRSA {

	private static final String RSA = "RSA";
	private static final int TAMANHO = 2048;
	private static final BASE64Encoder ENCODER = new BASE64Encoder();
	private static final BASE64Decoder DECODER = new BASE64Decoder();

	public KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
		generator.initialize(TAMANHO, new SecureRandom());
		return generator.generateKeyPair();
	}

	public String para(PublicKey chave) {
		return ENCODER.encode(chave.getEncoded());
	}

	public String para(PrivateKey chave) {
		return ENCODER.encode(chave.getEncoded());
	}

	public PublicKey generatePublic(String key)
			throws InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		byte[] keyBytes = DECODER.decodeBuffer(key);

		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
		return keyFactory.generatePublic(publicKeySpec);

	}

	private PrivateKey generatePrivate(String key) throws Exception {

		byte[] keyBytes = DECODER.decodeBuffer(key);

		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		return keyFactory.generatePrivate(publicKeySpec);

	}

	public String criptografa(String texto, String chave) throws Exception {
		PublicKey key = generatePublic(chave);
		return this.criptografa(texto, key);
	}

	public String criptografa(String texto, PublicKey chave) throws Exception {
		byte[] cipherText = null;

		final Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.ENCRYPT_MODE, chave);
		cipherText = cipher.doFinal(texto.getBytes());

		return ENCODER.encode(cipherText);
	}

	public String decriptografa(String texto, String chave) throws Exception {
		PrivateKey key = generatePrivate(chave);
		return this.decriptografa(texto, key);
	}

	public String decriptografa(String texto, PrivateKey chave) throws Exception {
		byte[] dectyptedText = null;

		final Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.DECRYPT_MODE, chave);
		dectyptedText = cipher.doFinal(DECODER.decodeBuffer(texto));

		return new String(dectyptedText);
	}

}