/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;

/**
 *
 * @author osiri
 */
public class Move {
    private int x,y;
    private char c;
    
    Move(int x, int y, char c){
        this.x=x;
        this.y=y;
        this.c=c;
    }
    
    public int getx(){
        return x;
    }
    public int gety(){
        return y;
    }
    public char getPlayer(){
        return c;
    }
    public void setPlayer(char c){
        this.c = c;
    }
    
    public String toString(){
        return getPlayer() + "(" + getx() + ", " + gety() + ")";
    }
}
