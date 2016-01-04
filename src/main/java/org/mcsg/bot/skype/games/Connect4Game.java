package org.mcsg.bot.skype.games;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;

public class Connect4Game {

    public static final int ROWS = 6;
    public static final int COLS = 7;

    private String player1;
    private String player2;
    private Tile p1Tile;
    private Tile p2Tile;

    private int lastRow, lastCol;
    private int move = 0;

    private ChatMessage msg;
    private int lastMessage;

    private Connect4AI AI;

    public Connect4Game(Chat chat, String p1, String p2, Tile p1Tile, Tile p2Tile) {
        this.player1 = p1;
        this.player2 = p2;

        this.p1Tile = p1Tile;
        this.p2Tile = p2Tile;

        try {
            AI = Connect4Manager.getInstance().getAI(p2, chat, p2Tile, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public ChatMessage getMsg() {
        return msg;
    }

    public void setMsg(ChatMessage msg) {
        this.msg = msg;
    }

    public int getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(int lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isMove(String player) {
        return move == 0 && player.equals(player1) || move == 1 && player.equals(player2);
    }

    public String getMover() {
        return (move == 0) ? player1 : player2;
    }

    public Tile getTile(String player) {
        if (player.equals(player1)) {
            return p1Tile;
        } else {
            return p2Tile;
        }
    }

    public enum Tile {
        SQUARE("✔"), CIRCLE("✕");
        // SQUARE("■"), CIRCLE("○");

        public String symbol;

        private Tile(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    private Tile[][] tiles = new Tile[ROWS][COLS];

    public Tile[][] getTiles() {
        return tiles;
    }

    public boolean makeMove(Tile tile, int col) throws IllegalColumnException, ColumnFullException, BoardFullException {
        if (col < 0 || col > tiles[0].length) {
            throw new IllegalColumnException("Invalid Column: " + col);
        }
        if (tiles[0][col] != null) {
            throw new ColumnFullException();
        }
        boolean full = true;
        for (Tile tilee : tiles[0]) {
            if (tilee == null)
                full = false;
        }
        if (full)
            throw new BoardFullException();

        int row = getFirstRow(col);
        tiles[row][col] = tile;
        lastCol = col;
        lastRow = row;
        return checkForVictory(tile, tiles);
    }

    public int doAiMove() {
        if (AI != null) {
            return (AI.makeMove()) ? 1 : 0;
        }
        return -1;
    }

    public void nextMove() {
        move = Math.abs(move - 1);
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getLastCol() {
        return lastCol;
    }

    private int getFirstRow(int col) {
        return getFirstRow(tiles, col);
    }

    protected int getFirstRow(Tile[][] tiles, int col) {
        for (int a = 5; a > -1; a--) {
            if (tiles[a][col] == null) {
                return a;
            }
        }
        return -1;
    }

    public Tile checkForVictory(Tile[][] tiles) {
        return (checkForVictory(Tile.CIRCLE, tiles)) ? Tile.CIRCLE
                : (checkForVictory(Tile.SQUARE, tiles)) ? Tile.SQUARE : null;
    }

    public boolean checkForVictory(Tile tile, Tile[][] tiles) {
        int count = 0;
        try {
            // x, y, rowdelt, coldelt, rowinc, colinx;

            // ChatManager.chat(Skype.getAllBookmarkedChats()[0],
            // "Checking for tile: "+tile);

            int[][] patterns = { new int[] { 0, 0, 1, 0, 0, 1 }, new int[] { 0, 0, 0, 1, 1, 0 },
                    new int[] { 0, 0, 1, 0, -1, 1 }, new int[] { ROWS - 1, COLS - 1, 0, -1, 1, -1 },
                    new int[] { ROWS - 1, 0, -1, 0, 1, 1 }, new int[] { 0, COLS - 1, 0, -1, 1, 1 } };

            for (int[] pattern : patterns) {
                // ChatManager.chat(Skype.getAllBookmarkedChats()[0],
                // "\n\n\nChecking pattern "+Arrays.toString(pattern));
                int rowi = pattern[0];
                int coli = pattern[1];
                while (rowi >= 0 && rowi < ROWS && coli >= 0 && coli < COLS) {
                    // ChatManager.chat(Skype.getAllBookmarkedChats()[0],
                    // "\nStarting sweep. RowI: "+rowi+" ColI: "+coli);
                    count = 0;
                    int row = rowi;
                    int col = coli;
                    while (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        // ChatManager.chat(Skype.getAllBookmarkedChats()[0],
                        // "Row: "+row+" Col: "+col+" Value: "+tiles[row][col]+" Count: "+count);

                        if (tiles[row][col] == tile) {
                            // ChatManager.chat(Skype.getAllBookmarkedChats()[0],
                            // "Found match.");
                            count++;
                            if (count > 3)
                                return true;
                        } else
                            count = 0;
                        row += pattern[4];
                        col += pattern[5];
                    }
                    rowi += pattern[2];
                    coli += pattern[3];
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public class IllegalColumnException extends Exception {
        public IllegalColumnException(String str) {
            super(str);
        }
    }

    public class ColumnFullException extends Exception {

    }

    public class BoardFullException extends Exception {

    }
}
