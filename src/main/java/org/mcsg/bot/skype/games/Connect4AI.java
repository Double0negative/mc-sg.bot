package org.mcsg.bot.skype.games;

import org.mcsg.bot.skype.games.Connect4Game.Tile;

import com.samczsun.skype4j.chat.Chat;

public abstract class Connect4AI {

  public Tile side;
  public Connect4Game game;
  public Chat chat;

  public Connect4AI(){

  }
  
  public Connect4AI load(Chat chat, Tile side, Connect4Game game){
    this.chat = chat;
    this.side = side;
    this.game = game;
    return this;
  }
  
  
  public abstract boolean makeMove();
}
