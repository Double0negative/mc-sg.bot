package org.mcsg.bot.skype.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {

	public static void writeFile(File file, String text) throws IOException{
		if(!file.exists()) file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.println(text);
		pw.flush();
		pw.close();
	}
	
}
