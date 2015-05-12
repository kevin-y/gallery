package org.keviny.gallery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtils {

	private enum Algorithm{MD5,SHA1,SHA}
	
	private static MessageDigest md = null;
	private static char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7',
								'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	//512Mb
	private final long BLOCK_SIZE = 512 * 1024 * 1024;
	
	public String md5(ByteBuffer input) {
		md.update(input);
		byte[] b = md.digest();
		return toHexString(b);
	}
	
	public static String toHexString(byte[] b) {
		int length = b.length;
		char[] s = new char[length << 1];
		for(int i = 0; i < length; i++) {
			//取出高四位
			s[i << 1] = ch[b[i] >>> 4 & 0xF];
			//取出低四位
			s[(i << 1) + 1] = ch[b[i] & 0xF];
		}
		return new String(s);
	}
	
	public String encode(byte[] input) {
		return md5(ByteBuffer.wrap(input));
	}
	
	public String encode(String input) {
		return encode(input.getBytes());
	}
	
	public String checksum(String name) throws IOException {
		return checksum(name != null ? new File(name) : null);
	}
	
	public String checksum(File file) throws IOException {
		FileInputStream in  = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		long length = file.length();
		
		MappedByteBuffer  mBuffer = null;
		long i = 0;
		long size;
		while(length > 0) {
			size = (length -= BLOCK_SIZE) < 0 ? (length + BLOCK_SIZE) : BLOCK_SIZE;
			mBuffer = ch.map(MapMode.READ_ONLY, i * BLOCK_SIZE, size);
			md.update(mBuffer);
			i++;
		}
		ch.close();
		in.close();
		return toHexString(md.digest());
	}
	
	private MessageDigestUtils(String algorithm) {
		init(algorithm);
	}
	
	private void init(String algorithm) {
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static MessageDigestUtils getInstance(String algorithm) {
		try {
			Algorithm.valueOf(algorithm.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Unsupported algorithm: " + algorithm);
		}
		return new MessageDigestUtils(algorithm);
	} 

	public static void main(String[] args) throws IOException {
		MessageDigestUtils d = MessageDigestUtils.getInstance("sha1");
		System.out.println(d.checksum("C:/Users/kevin-y/Downloads/pdfbox-1.8.2.jar").equals("468dddd645c8e06b2cb466a634cf290c3fc36ab0"));
		
	}

}
