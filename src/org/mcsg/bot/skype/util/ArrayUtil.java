package org.mcsg.bot.skype.util;

import org.mcsg.bot.skype.games.Connect4Game.Tile;

public class ArrayUtil {

	
	public static Tile[][] clone2DArray(Tile[][] array) {
	    int rows=array.length ;
	    //Tile rowIs=array[0].length ;

	    //clone the 'shallow' structure of array
	    Tile[][] newArray =(Tile[][]) array.clone();
	    //clone the 'deep' structure of array
	    for(int row=0;row<rows;row++){
	        newArray[row]=(Tile[]) array[row].clone();
	    }

	    return newArray;
	}
}
