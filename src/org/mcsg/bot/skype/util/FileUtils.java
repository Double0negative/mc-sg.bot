package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtils {

	public static void writeFile(File file, String text) throws IOException{
		if(!file.exists()) file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		BufferedWriter bw = new BufferedWriter(pw);

		bw.append(text);
		bw.flush();
		bw.close();
	}

	public static String readFile(File file) throws IOException{
		if(!file.exists()) file.createNewFile();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		char[] buff = new char[512];

		while (true) {
			int len = br.read(buff, 0, buff.length);
			if (len == -1) {
				break;
			}
			sb.append(buff, 0, len);
		}
		return sb.toString();
	}

	public static File getFile(String name){
		return new File("bot_files/", name);
	}
	
}
