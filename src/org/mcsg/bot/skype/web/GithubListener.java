package org.mcsg.bot.skype.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.mcsg.bot.skype.Bot;
import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.web.GithubListener.GitHubMessage.Commit;

import com.google.gson.Gson;
import com.skype.Chat;
import com.skype.SkypeException;

public class GithubListener {

	private static Gson gson = new Gson();

	public static void main(String args[]){
		GithubListener.listen();	
	}

	public static void listen(){
		System.out.println("Staring github listener on "+Settings.Root.Github.port);

		new Thread(){
			public void run(){
				ServerSocket sskt;
				try {
					sskt = new ServerSocket(Settings.Root.Github.port);
				} catch (IOException e1) {
					return;
				}

				while(true){
					try{
						Socket skt = sskt.accept();
						System.out.println("Accepted github connection from "+skt.getInetAddress());
						BufferedReader read = new BufferedReader(new InputStreamReader(skt.getInputStream()));



						StringBuilder sb = new StringBuilder();
						String line = "";
						HashMap<String, HttpHeader> headers = new HashMap<>();


						//Read headers
						while ((line = read.readLine()) != null) {
							if(line.length() == 0) break;
							System.out.println(line);
							String split[] = line.split(":");
							if(split.length > 1)
								headers.put(split[0].trim(), new HttpHeader(split[0].trim(), split[1].trim()));
						}

						long length = Long.parseLong(headers.get("Content-Length").getValue());


						//Read body
						for(int a = 0; a < length; a++){
							sb.append((char) read.read());
						}
						skt.close();
						sendMessageToChats(headers.get("X-GitHub-Event").getValue(), gson.fromJson(sb.toString(), GitHubMessage.class));
					}catch (Exception e){
						e.printStackTrace();
					}

				}
			}
		}.start();





	}


	public static void sendMessageToChats(String type, GitHubMessage data) throws SkypeException{
		System.out.println(type);
		List<String> chats = Settings.Root.Github.github_update_chat.get(data.repository.url);
		System.out.println(chats);
		for(String chatid: chats){
			Chat chat = Bot.getChat(chatid);
			if(type.equalsIgnoreCase("push")){
				if(data.commits.size() > 0){
					StringBuilder sb = new StringBuilder();
					sb.append(data.pusher.name+" pushed "+data.commits.size()+" new commit to "+data.repository.url);
					sb.append("\n");
					for(Commit commit : data.commits){
						sb.append(commit.committer.name+": "+commit.message.replace("\n", "").replace("  ", "").replace("\t", ""));
						sb.append("\n");
						sb.append(commit.url);
						sb.append("\n");
					}
					chat.send(sb.toString().trim());
				}
			}
		}
	}


	class GitHubMessage {
		public List<Commit> commits;
		public Pusher pusher;
		public Repo repository;
		public String url;

		class Commit {
			public String message;
			public String url;
			public Committer committer;


			class Committer {
				public String name;
			}


		}
		class Repo {
			public String name;
			public String url;
		}
		
		class Pusher {
			String name;
		}




	}

}
