package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.ShellStreamReader.StreamType;

import com.skype.Chat;
import com.skype.SkypeException;

public class ShellCommand {

  private static int id = 0;

  private static int getId(){
    return ++id;
  }

  private static HashMap<Integer, Process> procs = 
      new HashMap<>();
  private static ConcurrentHashMap<Integer, Class<? extends ShellStreamReader>> types =
      new ConcurrentHashMap<>();
  private static ConcurrentHashMap<Integer, ArrayList<ShellStreamReader>> readers = 
      new ConcurrentHashMap<>();

  /**
   * Execute shell command. 
   * @param chat - chat to execute this command from
   * @param args - command to run
   * @return the id of the resulting process
   */
  public static int exec(final Chat chat, String args){
    return exec(chat, args, 0, true);
  }

  /**
   * Execute shell command. 
   * @param chat - chat to execute this command from
   * @param args - command to run
   * @param limit - time limit in ms for this process to run. 0 for infinite
   * @param displayCommand - if true, will print the command and process ID in chat
   * @return the id of the resulting process
   */
  public static int exec(final Chat chat, String args, long limit,  boolean displayCommand){
    return exec(chat, args, limit, displayCommand, false);
  }

  /**
   * Execute shell command. 
   * @param chat - chat to execute this command from
   * @param args - command to run
   * @param limit - time limit in ms for this process to run. 0 for infinite
   * @param displayCommand - if true, will print the command and process ID in chat
   * @param wait - if true, will block until this process returns
   * @return the id of the resulting process
   */
  public static int exec(final Chat chat, String args, long limit,  boolean displayCommand, boolean wait){
    return exec(chat, args, limit, displayCommand, wait, DefaultShellStreamReader.class);
  }

  /**
   * Execute shell command. 
   * @param chat - chat to execute this command from
   * @param args - command to run
   * @param limit - time limit in ms for this process to run. 0 for infinite
   * @param displayCommand - if true, will print the command and process ID in chat
   * @param wait - if true, will block until this process returns
   * @param reader - Custom ShellStreamReader for this process
   * @return the id of the resulting process
   */
  public static int exec(final Chat chat, String args, long limit,  boolean displayCommand, boolean wait, Class<? extends ShellStreamReader> reader){
    int id = getId();
    try{
      String name = "files/temp"+System.currentTimeMillis()+".sh";
      FileUtils.writeFile(new File(name), args);

      if(displayCommand)
        ChatManager.chat(chat, "Running command: \""+ args +"\" ID: "+id);

      final Process proc = Runtime.getRuntime().exec("bash "+name);

      procs.put(id, proc);
      types.put(id, reader);
      readers.put(id, new ArrayList<ShellStreamReader>());

      readToChat(chat, id);
      startReaders(id, reader);

      if(wait)
        proc.waitFor();
      if(limit != 0)
        new Limiter(proc, chat, limit, id).start();

    }catch(Exception e){
      e.printStackTrace();
    }
    return id;
  }

  private static void startReaders(int id, Class<? extends ShellStreamReader> reader){
    Process proc = procs.get(id);

    readStream(StreamType.INPUT, id, proc.getInputStream());
    readStream(StreamType.ERROR, id, proc.getErrorStream());
  }

  /**
   * Starts reading the output of a process to this chat.
   * @param chat - Chat to read to
   * @param id - id of process to read 
   * @throws Exception
   */
  public static void readToChat(Chat chat, int id) throws Exception{
    ShellStreamReader reader = getReaderInstance(id, chat);
    readers.get(id).add(reader);
  }

  private static ShellStreamReader getReaderInstance(int id, Chat chat) throws Exception{
    Class<? extends ShellStreamReader> type = types.get(id);
    ShellStreamReader reader =  type.newInstance();
    reader.setChat(chat);
    return reader;
  }

  /**
   * Stops reading output to this chat
   * @param chat - chat to quiet
   * @param id - id of process to quiet
   */
  public static void quietChat(Chat chat, int id){
    for(ShellStreamReader reader : readers.get(id).toArray(new ShellStreamReader[0])){
      if(reader.getChat().equals(chat)){
        readers.get(id).remove(reader);
      }
    }
  }

 /**
  * write to the input stream of this process. 
  * @param id - process id to write to
  * @param text - message to write
  */
  public static void writeStream(final int id,  final String text){
    new Thread(){
      public void run(){
        PrintWriter pw = new PrintWriter(procs.get(id).getOutputStream());
        pw.println(text);
        pw.flush();
      }
    }.start();
  }

  private static void readStream(StreamType type, final int id,  final InputStream input){
    new Thread(){
      public void run(){
        try{
          String line = "";

          BufferedReader in = new BufferedReader(
              new InputStreamReader(input));
          while ((line = in.readLine()) != null) {
            for(ShellStreamReader reader : readers.get(id)){
              reader.read(type, line);
            }
          }
          in.close();
        }catch (Exception e){}
      }
    }.start();
  }

  /**
   * Get the process represented by this ID
   * @param id - process id
   * @return Process 
   */
  public static Process getProcess(int id){
    return procs.get(id);
  }

  /**
   * Get the ID that represents this process
   * @param process - process
   * @return ID
   */
  public static int getProcessID(Process process) {
    if (process != null) {
      for (Map.Entry<Integer, Process> entryProcess : procs.entrySet()) {
        if (entryProcess.getValue().equals(process)) return entryProcess.getKey();
      }
    }
    return -1;
  }

  /**
   * Kill a process
   * @param id - id of process to kill
   */
  public static void kill(int id){
    procs.get(id).destroy();
    cleanupProcess(id);
  }

  /**
   * Force kill a process
   * @param id - process to kill
   */
  public static void forceKill(int id){
    procs.get(id).destroyForcibly();
    cleanupProcess(id);
  }
  
  private static void cleanupProcess(int id){
    readers.remove(id);
    types.remove(id);
    procs.remove(id);
  }

  static class  Limiter extends Thread{
    Process process;
    long time;
    int id;
    Chat chat;
    public Limiter(Process process, Chat chat, long time, int id){
      this.process = process;
      this.time = time;
      this.id = id;
      this.chat = chat;
    }

    public void run(){
      try { Thread.sleep(time); } catch (Exception e){ ChatManager.printThrowable(chat, e);}
      if(process.isAlive()){
        process.destroyForcibly();
        try {
          chat.send("Process "+id+" exceeded time limit. Process was killed");
          cleanupProcess(id);
        } catch (SkypeException e) {
          e.printStackTrace();
        }
      }
    }
  }

  static class DefaultShellStreamReader extends ShellStreamReader {

    @Override
    public void read(StreamType inputType, String msg) {
      ChatManager.chat(getChat(), inputType.getPrefix() + msg);
    }

  }

}
