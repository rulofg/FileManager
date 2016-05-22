package io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public abstract class FileRW {
	public static byte[] readBinaryFile(String sourcePath) {
		File source = new File(sourcePath);
		byte[] res = new byte[(int) source.length()];
		try {
			InputStream inputStream = null;
			try {
				int totalBytesRead = 0;
				inputStream = new BufferedInputStream(new FileInputStream(
						source));
				while (totalBytesRead < res.length) {
					int bytesRemaining = res.length - totalBytesRead;
					// input.read() returns -1, 0, or more :
					int bytesRead = inputStream.read(res, totalBytesRead,
							bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;

					}
				}
			} finally {
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void writeBinaryFile(byte[] data, String filePath) {
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(
						new FileOutputStream(filePath));
				output.write(data);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// ex.printStackTrace();
		}
	}

	public static void writeTextFile(String text, String filePath) {
		try {
			OutputStream ops = new FileOutputStream(filePath);
			OutputStreamWriter opsw = new OutputStreamWriter(ops);
			BufferedWriter bw = new BufferedWriter(opsw);
			bw.write(text);
			bw.close();
			opsw.close();
			ops.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String readTextFile(String filePath) {
		String text = null;
		try {
			text = "";
			InputStream ips = new FileInputStream(filePath);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				text += line + "\n";
			}
			br.close();
			ipsr.close();
			ips.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public static Object readObjectFile(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			return o;
		} catch (NullPointerException e){
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void writeObjectFile(Serializable object, String filePath) {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
