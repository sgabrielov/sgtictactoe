/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;

import java.util.ArrayList;
import java.lang.Cloneable;
/**
 *
 * @author osiri
 */
public class GameBoard implements Cloneable{
    private int board_width;
    private int board_height;
    private int wincon;
    private char turn;
    private int moveCounter;
    
    private Move lastMove = null;
    
    /*private Player pX;
    private Player pO;*/
    
    public final char X = 'X';
    public final char O = 'O';
    public final char EMPTY = ' ';
    
    private boolean won_state, drawn_state;
    
    private static final int MAXWIDTH = 10;
    private static final int MAXHEIGHT = 10;
    
    private char[][] board = new char[MAXWIDTH][MAXHEIGHT];
    
    GameBoard(){
        this(3,3,3);
    }
    GameBoard(GameBoard toCopy){
        this.board_width = toCopy.getBoardWidth();
        this.board_height = toCopy.getBoardHeight();
        this.board = toCopy.copyBoard();
        this.turn = toCopy.getTurn();
        this.wincon = toCopy.wincon;
        if(toCopy.lastMove != null)
            this.lastMove = new Move(toCopy.lastMove.getx(), toCopy.lastMove.gety(), toCopy.lastMove.getPlayer());
        moveCounter = toCopy.moveCounter;
        won_state = toCopy.is_winning_state();
        drawn_state = toCopy.is_draw_state();
    }
    GameBoard(int board_width, int board_height, int wincon){
        this.board_width = board_width;
        this.board_height = board_height;
        this.wincon = wincon;
        this.turn = X;
        moveCounter = 0;
        won_state =false;
        drawn_state =false;
        initboard();
    }
    
    @Override
    protected Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    public GameBoard getCopy(){
        GameBoard newBoard = null;
        try{
            newBoard = (GameBoard)this.clone();
        }
        
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        newBoard.board = this.copyBoard();
        return newBoard;
    }
    
    
    public void push(Move m){
        if(board[m.getx()][m.gety()] == EMPTY){
            board[m.getx()][m.gety()] = m.getPlayer();
            moveCounter++;
            lastMove = m;
            nextTurn();
        }
    }
    public void initboard(){
        for(int x=0;x<board_width;x++){
            for(int y=0;y<board_height;y++){
                board[x][y]=EMPTY;
            }
        }
    }
    public int getBoardWidth(){
        return board_width;
    }
    
    public int getBoardHeight(){
        return board_height;
    }
    
    public char[][] getBoard(){
        return board;
    }
    public char[][] copyBoard(){
        char[][] copy = new char[MAXWIDTH][MAXHEIGHT];
        
        for(int x=0;x<board_width;x++){
            for(int y=0;y<board_height;y++){
                copy[x][y] = board[x][y];
            }
        }
        
        return copy;
    }
    public char getCharAt(int x, int y){
        return board[x][y];
    }
    
    public char getTurn(){
        return turn;
    }
    
    public void nextTurn(){
        if(turn == X){
            turn = O;
        }
        else{
            turn = X;
        }
        //printBoard();
    }
    
    public void printBoard(){
        for(int y = 0; y < board_height; y++){
            System.out.print(y+": ");
            for(int x=0;x<board_width;x++){
                System.out.print(getCharAt(x,y));
            }
            System.out.println();
        }
    }
    public boolean is_draw_state(){
        if(!drawn_state){
            if(lastMove == null) return false;
            else{
                drawn_state = moveCounter >= board_width * board_height && !is_winning_state();
            }
        }
        return drawn_state;
    }
    /*
    public boolean is_winning_state(){
        int x,y;
        for(x = 0; x<board_width; x++){
            for(y = 0; y<board_height; y++){
                if(check_winning_sequence(O, wincon, x, y) || check_winning_sequence(X, wincon, x, y))
                    return true;
            }
        }
        return false;
    }*/
    
    public boolean is_winning_state(){
        if(!won_state){
            if(lastMove == null) return false;
            else{ 
                won_state = check_winning_sequence(lastMove.getPlayer(), wincon, lastMove.getx(), lastMove.gety());
            }
        }
        return won_state;
    }
    
    public boolean check_winning_sequence(char c, int s, int x, int y){
        return check_vertical_sequence(c,s,x,y) || check_horizontal_sequence(c,s,x,y) || check_diagonal_sequence(c,s,x,y);
    }
    
    public boolean check_vertical_sequence(char c, int s, int x, int y){
        
        boolean downstreakbroken = false;
        boolean upstreakbroken = false;
        int upstreak = 0;
        int downstreak = 0;
        for(int i=1;i<=s;i++){
            if(y + i < board_width & !downstreakbroken){
                if(board[x][y+i] == c){
                    downstreak++;
                }
                else{
                    downstreakbroken = true;        
                }
            }
            if(y - i >= 0){
                if(board[x][y-i] == c & !upstreakbroken){
                    upstreak ++;
                }
                else{
                    upstreakbroken = true;
                }
            }
            if(downstreak + upstreak + 1 >=s){
                return true;
            }
        }
        
        return false;
        
    }
    
    
    public boolean check_horizontal_sequence(char c, int s, int x, int y){    
        boolean leftstreakbroken = false;
        boolean rightstreakbroken = false;
        int leftstreak = 0;
        int rightstreak = 0;
        for(int i=1;i<=s;i++){
            if(x + i < board_width && !rightstreakbroken){
                if(board[x+i][y] == c){
                    rightstreak++;
                }
                else{
                    rightstreakbroken = true;      
                }
            }
            if(x - i >= 0 && !leftstreakbroken){
                if(board[x-i][y] == c){
                    leftstreak ++;
                }
                else{
                    leftstreakbroken = true;
                }
            }
            if(leftstreak + rightstreak + 1 >= s){
                return true;
            }
        }
        
        return false;
        
    }
    
    public boolean check_diagonal_sequence(char c, int s, int x, int y){
        
        boolean upleftstreakbroken = false;
        boolean uprightstreakbroken = false;      
        boolean downleftstreakbroken = false;
        boolean downrightstreakbroken = false;    
        int upleftstreak = 0;
        int uprightstreak = 0;
        int downleftstreak = 0;
        int downrightstreak = 0;
        for(int i=1;i<=s;i++){
            if(x - i >= 0 && y - i >= 0 && !upleftstreakbroken){
                if(board[x-i][y-i] == c){
                    upleftstreak++;
                }
                else{
                    upleftstreakbroken = true;        
                }
            }
            if(x + i < board_width && y - i >= 0 && !uprightstreakbroken){
                if(board[x+i][y-i] == c){
                    uprightstreak++;
                }
                else{
                    uprightstreakbroken = true;        
                }
            }
            if(x - i >= 0 && y + i < board_height && !downleftstreakbroken){
                if(board[x-i][y+i] == c){
                    downleftstreak++;
                }
                else{
                    downleftstreakbroken = true;        
                }
            }
            if(x + i < board_width && y + i < board_height && !downrightstreakbroken){
                if(board[x+i][y+i] == c){
                    downrightstreak++;
                }
                else{
                    downrightstreakbroken = true;        
                }
            }
            if(upleftstreak + downrightstreak + 1 >= s || uprightstreak + downleftstreak + 1 >= s){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Move> getLegalMoves(){
        
        
        ArrayList<Move> list = new ArrayList<>();
        
        if(!drawn_state && !won_state){
            for(int x=0;x<board_width;x++){
                for(int y=0;y<board_height;y++){
                    if(board[x][y]==' '){
                        list.add(new Move(x,y,turn));
                    }
                }
            }
        }
        return list;
    }
}
