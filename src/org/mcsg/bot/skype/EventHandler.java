package org.mcsg.bot.skype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mcsg.bot.skype.events.Event;
import org.mcsg.bot.skype.events.listeners.Listener;

public class EventHandler {

  private static HashMap<Class<? extends Event>, List<Listener<? extends Event>>> listeners = 
      new HashMap<>();

  private static HashMap<McsgBotPlugin, List<Listener<?>>> plugins = 
      new HashMap<>();

  public static void registerListener(McsgBotPlugin plugin, Class<? extends Event> eventclass, Listener<? extends Event> listener){
    List<Listener<? extends Event>> list = listeners.getOrDefault(eventclass, new ArrayList<>());
    list.add(listener);
    listeners.put(eventclass, list);
    
    List<Listener<?>> olist = plugins.getOrDefault(plugin, new ArrayList<Listener<?>>());
    olist.add(listener);
    plugins.put(plugin, olist);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Event>  void callEvent(T event){
    List<Listener<? extends Event>> list = listeners.get(event.getClass());
    if(list != null){
      for(Listener<? extends Event> listener : list){
        Listener<T> lis = (Listener<T>) listener;
        lis.onEvent(event);
      }
    }
  }
  
  public static void unregisterListeners(McsgBotPlugin plugin){
    for(Listener<?> listener : plugins.get(plugin)){
      for(List<Listener<?>> llist : listeners.values()){
        for(Listener<?> olisten : llist.toArray(new Listener<?>[0])){
          if(listener == llist){
            llist.remove(olisten);
          }
        }
      }
    }
  }

}
