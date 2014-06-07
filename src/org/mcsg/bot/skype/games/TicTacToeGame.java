package org.mcsg.bot.skype.games;

public class TicTacToeGame {


	public static final int ROWS = 3;
	public static final int COLS = 3;

	private String player1;
	private String player2;

	private Tile p1Tile;
	private Tile p2Tile;

	private int move = 0;

	public TicTacToeGame(String p1, String p2, Tile p1Tile, Tile p2Tile){
		this.player1 = p1;
		this.player2 = p2;

		this.p1Tile = p1Tile;
		this.p2Tile = p2Tile;
	}

	public String getPlayer1(){
		return player1;
	}

	public String getPlayer2(){
		return player2;
	}

	public boolean isMove(String player){
		return move == 0 && player.equals(player1) || move == 1 && player.equals(player2);
	}

	public String getMover(){
		return (move == 0)? player1 : player2;
	}

	public Tile getTile(String player){
		if(player.equals(player1)){
			return p1Tile;
		} else {
			return p2Tile;
		}
	}

	public enum Tile {
		SQUARE("X"), CIRCLE("O");


		public String symbol;
		private Tile(String symbol){
			this.symbol = symbol;
		}
		public String getSymbol(){
			return symbol;
		}
	}

	private Tile[][] tiles = new Tile[ROWS][COLS];

	public Tile[][] getTiles(){
		return tiles;
	}

	public boolean makeMove(Tile tile, int row, int col) throws  BoardFullException, TileIsFilledException, IllegalTileException {
		if(col < 0 || col > COLS - 1 || row < 0 || row > ROWS -1){
			throw new IllegalTileException();
		}
		if(tiles[row][col] != null){
			throw new TileIsFilledException();
		}
		boolean full = true;
		for(Tile[] tileea : tiles){
			for(Tile tilee : tileea){
				if(tilee == null)
					full = false;
			}
		}
		if(full) throw new BoardFullException();

		tiles[row][col] = tile;
		move = Math.abs(move - 1);
		return checkForVictory(tile);
	}


	private boolean checkForVictory(Tile tile){ //Steal the code from C4 ;D
		int count = 0;
		try{
			//			x, y, rowdelt, coldelt, rowinc, colinx;

			//				ChatManager.chat(Skype.getAllBookmarkedChats()[0], "Checking for tile: "+tile);

			int [][] patterns = {
					new int[]{0,0,1,0,0,1},
					new int[]{0,0,0,1,1,0},
					new int[]{0,0,1,0,-1,1}, new int[]{ROWS-1,COLS-1,0,-1,1,-1},
					new int[]{ROWS-1,0,-1,0,1,1}, new int[]{0,COLS-1,0,-1,1,1}}; 

			for(int[] pattern : patterns){
				//				ChatManager.chat(Skype.getAllBookmarkedChats()[0], "\n\n\nChecking pattern "+Arrays.toString(pattern));
				int rowi = pattern[0];
				int coli = pattern[1];
				while(rowi >= 0 && rowi < ROWS && coli >= 0 && coli < COLS){
					//					ChatManager.chat(Skype.getAllBookmarkedChats()[0], "\nStarting sweep. RowI: "+rowi+" ColI: "+coli);
					count = 0;
					int row = rowi; int col = coli;
					while(row >= 0 && row < ROWS && col >= 0 && col < COLS){
						//						ChatManager.chat(Skype.getAllBookmarkedChats()[0], "Row: "+row+" Col: "+col+" Value: "+tiles[row][col]+" Count: "+count);

						if(tiles[row][col] == tile){
							//							ChatManager.chat(Skype.getAllBookmarkedChats()[0], "Found match.");
							count++;
							if(count > 2) return true;
						} else count=0;
						row += pattern[4];
						col += pattern[5];
					}
					rowi += pattern[2];
					coli += pattern[3];
				}
			}
		}catch (Exception e){}
		return false;
	}




	public class IllegalTileException extends Exception{

	}

	public class TileIsFilledException  extends Exception {

	}
	public class BoardFullException extends Exception {

	}
}
