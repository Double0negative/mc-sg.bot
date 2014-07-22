package org.mcsg.bot.skype.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.Progress;
import org.mcsg.bot.skype.util.ThreadUtil;

import com.skype.Chat;

public class WebClient {

	private static final String DOWNLOAD_MESSAGE = "Downloading";
	private static final String UPLOAD_MESSAGE = "Uploading";
	private static final String RESULT_MESSAGE = "Getting result";
	private static final String WAITING_MESSAGE = "Waiting for server";

	public static Progress<String> postArgsProgress(final Chat chat, final String url,final List<HttpHeader> headers, final String ... args){
		final Progress<String> prog = new Progress<>();
		ThreadUtil.run("Web Post Args", new Thread(){
			public void run(){
				try{
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.setRequestMethod("POST");

					if(headers != null)
						for (HttpHeader header : headers) {
							connection.setRequestProperty(header.getName(), header.getValue());
						}

					String data = "";
					for(int a = 0; a < args.length; a+=2){
						data += URLEncoder.encode(args[a], "UTF-8") + "="+ URLEncoder.encode(args[a+1], "UTF-8") + "&";
					}

					connection.setUseCaches(false);
					connection.setDoInput(true);
					connection.setDoOutput(true);

					BufferedOutputStream writer = new BufferedOutputStream(connection.getOutputStream());

          byte[] bytes = data.getBytes();
					prog.setMax(bytes.length);
					prog.setMessage(UPLOAD_MESSAGE);
					

					for(int a = 0; a < bytes.length; a++){
						writer.write(bytes[a]);
						prog.setProgress(a);
					}
					writer.flush();
					writer.close();

					prog.setMessage(WAITING_MESSAGE);
					BufferedReader br = new BufferedReader(new InputStreamReader(
							connection.getInputStream()));
					prog.setMax(connection.getContentLengthLong());
					prog.setMessage(RESULT_MESSAGE);

					StringBuilder sb = new StringBuilder();
					char[] buff = new char[512];

					while (true) {
						int len = br.read(buff, 0, buff.length);
						prog.incProgress(len);
						if (len == -1) {
							break;
						}
						sb.append(buff, 0, len);
					}

					br.close();
					writer.close();
					prog.finish(sb.toString());
				}catch (Exception e){
					if(chat != null){
						ChatManager.printThrowable(chat, e);
					}
				}
			}
		});
		return prog;
	}


	public static Progress<byte[]> requestByteProgress(final Chat chat, final String urls){
		final Progress<byte[]> prog = new Progress<>();
		ThreadUtil.run("Request", new Thread(){
			public void run(){
				try{
					URL url = new URL(urls.replace(" ", "%20"));
					URLConnection con = url.openConnection();
					con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20100101 Firefox/31.0");

					BufferedInputStream br = new BufferedInputStream(con.getInputStream());

					byte[] buff = new byte[512];

					prog.setMax(con.getContentLengthLong());
					prog.setMessage(DOWNLOAD_MESSAGE);

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while (true) {
						int len = br.read(buff, 0, buff.length);
						if (len == -1) {
							break;
						}

						out.write(buff, 0, len);

						prog.incProgress(len);
					}
					br.close();

					prog.finish(out.toByteArray());

				}catch (Exception e){
					if(chat != null){
						ChatManager.printThrowable(chat, e);
					}
				}
			}
		});
		return prog;
	}

	public static Progress<String> requestProgress(final Chat chat, final String urls){
		final Progress<String> prog = new Progress<>();
		ThreadUtil.run("Request", new Thread(){
			public void run(){
				try{
					StringBuilder sb = new StringBuilder();

					URL url = new URL(urls.replace(" ", "%20"));
					URLConnection con = url.openConnection();
					con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20100101 Firefox/31.0");

					BufferedReader br = new BufferedReader(new InputStreamReader(
							con.getInputStream()));

					char[] buff = new char[512];

					prog.setMax(con.getContentLengthLong());
					prog.setMessage(DOWNLOAD_MESSAGE);

					while (true) {
						int len = br.read(buff, 0, buff.length);
						if (len == -1) {
							break;
						}
						sb.append(buff, 0, len);
						prog.incProgress(len);
					}
					br.close();

					prog.finish(sb.toString());

				}catch (Exception e){
					if(chat != null){
						ChatManager.printThrowable(chat, e);
					}
				}
			}
		});
		return prog;
	}


	public static Progress<String> postProgress(final Chat chat, final String url, final String body, final List<HttpHeader> headers){
		final Progress<String> prog = new Progress<>();
		ThreadUtil.run("Post", new Thread(){
			public void run(){
				try{
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.setRequestMethod("POST");

					if(headers != null)
						for (HttpHeader header : headers) {
							connection.setRequestProperty(header.getName(), header.getValue());
						}

					connection.setUseCaches(false);
					connection.setDoInput(true);
					connection.setDoOutput(true);

					BufferedOutputStream writer = new BufferedOutputStream(connection.getOutputStream());
					byte[] bytes = body.getBytes();
					prog.setMax(bytes.length);
					prog.setMessage(UPLOAD_MESSAGE);
					for(int a = 0; a < bytes.length; a++){
						writer.write(bytes[a]);
						prog.setProgress(a);
					}
					writer.flush();

					prog.setProgress(0);
					prog.setMax(connection.getContentLengthLong());
					prog.setMessage(DOWNLOAD_MESSAGE);
					BufferedReader br = new BufferedReader(new InputStreamReader(
							connection.getInputStream()));

					StringBuilder sb = new StringBuilder();
					char[] buff = new char[512];

					while (true) {
						int len = br.read(buff, 0, buff.length);
						prog.incProgress(len);
						if (len == -1) {
							break;
						}
						sb.append(buff, 0, len);
					}
					br.close();


					prog.finish(sb.toString());
				}catch (Exception e){
					if(chat != null){
						ChatManager.printThrowable(chat, e);
					}
				}
			}
		});
		return prog;
	}
	public static String request(String urls){
		return request(null, urls);
	}

	public static String request(Chat chat, String urls){
		Progress<String> prog = requestProgress(chat, urls);
		prog.waitForFinish();
		return prog.getResult();

	}

	public static String postArgs(Chat chat, String url,List<HttpHeader> headers, String ... args) {
		Progress<String> prog = postArgsProgress(chat, url, headers, args);
		prog.waitForFinish();
		return prog.getResult();
	}

	public static String postArgs(String url,List<HttpHeader> headers, String ... args){
		return postArgs(null, url, headers, args);
	}

	public static String post(String url, String body, List<HttpHeader> headers){
		return post(null, url, body, headers);
	}

	public static String post(Chat chat, String url, String body, List<HttpHeader> headers){
		Progress<String> prog = postProgress(chat, url, body, headers);
		prog.waitForFinish();
		return prog.getResult();
	}


}

