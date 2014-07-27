package org.mcsg.bot.skype;

import java.util.ArrayList;
import java.util.HashMap;

import org.mcsg.bot.skype.events.Event;
import org.mcsg.bot.skype.events.listeners.Listener;

public class EventHandler {

  private static HashMap<Class<? extends Event>, ArrayList<Listener<? extends Event>>> listeners = 
      new HashMap<>();

  

  public static void registerListener(Class<? extends Event> eventclass, Listener<? extends Event> listener){
    ArrayList<Listener<? extends Event>> list = listeners.getOrDefault(eventclass, new ArrayList<>());
    list.add(listener);
    listeners.put(eventclass, list);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Event>  void callEvent(T event){
    ArrayList<Listener<? extends Event>> list = listeners.get(event.getClass());
    if(list != null){
      for(Listener<? extends Event> listener : list){
        Listener<T> lis = (Listener<T>) listener;
        lis.onEvent(event);
      }
    }
  }

}
