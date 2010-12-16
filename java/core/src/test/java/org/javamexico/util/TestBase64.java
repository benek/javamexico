package org.javamexico.util;

import org.junit.Assert;
import org.junit.Test;

public class TestBase64 {

	void test(byte[] data, String expected) {
		String enc = Base64.base64Encode(data, 0, data.length);
		Assert.assertEquals(expected, enc);
		byte[] dec = Base64.base64Decode(enc);
		Assert.assertArrayEquals(data, dec);
	}

	@Test
	public void testBase64() {
		//Codificar, comparando contra cadenas generadas por OpenSSL
		String data = "Cadena de prueba para codificacion de base640";
		test(data.substring(0, data.length() - 3).getBytes(), "Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNl");
		test(data.substring(0, data.length() - 2).getBytes(), "Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNg==");
		test(data.substring(0, data.length() - 1).getBytes(), "Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNjQ=");
		test(data.getBytes(), "Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNjQw");
	}

}
