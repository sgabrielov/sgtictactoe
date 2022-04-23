/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe;

/**
 *
 * @author osiri
 */
public class Player {
    
    // The players stats
    private int wins, losses, draws, totalgames;
    
    // Whether the player is X or O
    private char symbol;
    
    // Initialize all values
    Player(char c){
        wins = 0;
        losses = 0;
        draws = 0;
        totalgames = 0;
        symbol = c;
    }
    
    // Mutator methods
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
    
    // Accessor methods
    public char getSymbol(){
        return symbol;
    }
    public boolean isAI(){
        return false;
    }
    
    // toString
    public String toString(){
        return "Wins: " + wins + " | Losses: " + losses + " | Draws: " + draws;
    }
}
