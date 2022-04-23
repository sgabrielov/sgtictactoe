/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;

/**
 *
 * @author osiri
 */
public class Player {
    private int wins, losses, draws, totalgames;
    private char symbol;
    
    Player(char c){
        wins = 0;
        losses = 0;
        draws = 0;
        totalgames = 0;
        symbol = c;
    }
    
    public void win(){
        wins++;
        totalgames++;
    }
    public void lose(){
        losses++;
        totalgames++;
    }
    public void draw(){
        draws++;
        totalgames++;
    }
    public char getSymbol(){
        return symbol;
    }
    public String toString(){
        return "Wins: " + wins + " | Losses: " + losses + " | Draws: " + draws;
    }
    public boolean isAI(){
        return false;
    }
}
