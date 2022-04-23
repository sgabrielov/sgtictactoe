/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe;

/**
 *
 * @author osiri
 */
public class Move {
    
    // Position of the move
    private int x,y;
    
    // Player making the move
    private char c;
    
    // Initialize all values
    Move(int x, int y, char c){
        this.x=x;
        this.y=y;
        this.c=c;
    }
    
    // Accessor methods
    public int getx(){
        return x;
    }
    public int gety(){
        return y;
    }
    public char getPlayer(){
        return c;
    }
    
    // Mutator methods
    public void setPlayer(char c){
        this.c = c;
    }
    
    // toString
    public String toString(){
        return getPlayer() + "(" + getx() + ", " + gety() + ")";
    }
}
