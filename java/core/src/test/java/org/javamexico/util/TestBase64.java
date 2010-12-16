package org.javamexico.util;

import org.junit.Assert;
import org.junit.Test;

public class TestBase64 {

	@Test
	public void testBase64() {
		//Codificar, comparando contra cadenas generadas por OpenSSL
		byte[] data = "Cadena de prueba para codificacion de base640".getBytes();
		String enc = Base64.base64Encode(data, 0, data.length - 3);
		Assert.assertEquals("Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNl", enc);
		//Decodificar y debemos obtener la original
		byte[] dec = Base64.base64Decode(enc);
		enc = Base64.base64Encode(data, 0, data.length - 2);
		Assert.assertEquals("Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNg==", enc);
		dec = Base64.base64Decode(enc);
		enc = Base64.base64Encode(data, 0, data.length - 1);
		Assert.assertEquals("Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNjQ=", enc);
		dec = Base64.base64Decode(enc);
		enc = Base64.base64Encode(data, 0, data.length);
		Assert.assertEquals("Q2FkZW5hIGRlIHBydWViYSBwYXJhIGNvZGlmaWNhY2lvbiBkZSBiYXNlNjQw", enc);
		dec = Base64.base64Decode(enc);
		Assert.assertArrayEquals(data, dec);
	}

}
