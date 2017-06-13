package com.ves.main.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileContentUtil {
	public static void saveAs(byte[] bytes, String fileLocation) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileLocation));
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] bytes(File file) throws IOException {
		int size = (int) file.length();
		byte[] fileContent = new byte[size];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		int read = 0;
		int numRead = 0;
		while (read < fileContent.length && (numRead = dis.read(fileContent, read, fileContent.length - read)) >= 0) {
			read = read + numRead;
		}
		dis.close();
		return fileContent;
	}

	public static byte[] bytes(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
}