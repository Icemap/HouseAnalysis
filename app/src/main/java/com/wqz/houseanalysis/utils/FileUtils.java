package com.wqz.houseanalysis.utils;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils
{
	public static byte[] getBytesFromFile(File f) {
		if (f == null)
			return null;
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException localIOException) {
		}
		return null;
	}

	public static File saveFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
			stream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	public static String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取根目录
		}
		return sdDir.toString();
	}

	public static void saveToLocal(File in, String localPath) throws IOException
	{
		FileInputStream inputStream = new FileInputStream(in);
		byte[] data = new byte[1024];
		FileOutputStream outputStream =new FileOutputStream(new File(getSDPath() + localPath));
		while (inputStream.read(data) != -1)
		{
			outputStream.write(data);
		}
		inputStream.close();
		outputStream.close();
	}

	public static void createPath(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdir();
		}
	}

	public static void deleteFile(File file)
	{
		if (file.exists())
		{
			if (file.isFile())
			{
				file.delete();
			}
			else if (file.isDirectory())
			{
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++)
				{
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	public static void createKMLPath()
	{
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com");
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/gig");
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/gig/myriver");
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/gig/myriver/kml");
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/gig/myriver/pdf");
		FileUtils.createPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/gig/myriver/shoot");
	}

	public static void saveString(String path, String content) throws IOException
	{
		File writename = new File(path);
		writename.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		out.write(content);
		out.flush();
		out.close();
	}

	public static String readString(String path) throws IOException
	{
		File filename = new File(path);
		InputStreamReader reader = new InputStreamReader(
				new FileInputStream(filename)); // 建立一个输入流对象reader
		BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
		StringBuffer sb = new StringBuffer();
		String line;
		line = br.readLine();
		sb.append(line);
		while (line != null) {
			line = br.readLine(); // 一次读入一行数据
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}