package org.mcsg.bot.skype.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarCreator {
  JarOutputStream target;
  public JarCreator(File file) throws Exception{
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    target = new JarOutputStream(new FileOutputStream(file), manifest);
  }
  
  public void add(String base, File source) throws IOException{
    BufferedInputStream in = null;
    try{
      if (source.isDirectory()){
        String name = source.getPath().replace("\\", "/");
        if (!name.isEmpty()){
          if (!name.endsWith("/"))
            name += "/";
          JarEntry entry = new JarEntry(name);
          entry.setTime(source.lastModified());
          target.putNextEntry(entry);
          target.closeEntry();
        }
        for (File nestedFile: source.listFiles())
          add(base, nestedFile);
        return;
      }
  
      JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").replace(base, ""));
      entry.setTime(source.lastModified());
      target.putNextEntry(entry);
      in = new BufferedInputStream(new FileInputStream(source));
  
      byte[] buffer = new byte[1024];
      while (true) {
        int count = in.read(buffer);
        if (count == -1)
          break;
        target.write(buffer, 0, count);
      }
      target.closeEntry();
    }
    finally
    {
      if (in != null)
        in.close();
    }
  }
  
  public void close() throws IOException{
    target.close();
  }
}

