package org.mcsg.bot.skype.events.listeners;

import org.mcsg.bot.skype.events.Event;

public interface Listener<T extends Event> {
  public void onEvent(T event);
}
