/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
/**
 *
 * @author osiri
 */
public class Game implements ActionListener{
    
    private GameBoard board;
    
    // Current players
    private Player pX;
    private Player pO;
    
    // Whether or not the game has ended
    private boolean isOver;
    
    // Waiting for the user to input their move
    private boolean waitingForMove = true;
    
    // Stores the move the GUI has received for the player
    Move playerMove;
    
    // The GUI that will provide user input
    GameWindow gui;
    
    // Default constructors
    Game(GameBoard b, GameWindow in){
        gui = in;
        board = b;
        isOver = false;
    }
    
    Game(GameBoard b, Object in){
        gui = (GameWindow)in;
        board = b;
        isOver = false;
    }
    
    // Accessor methods
    public Player getPlayerX(){
        return pX;
    }
    
    public Player getPlayerO(){
        return pO;
    }
    public GameBoard getBoard(){
        return board;
    }
    public boolean isOver(){
        return isOver;
    }
    
    // Mutator methods
    public void addPlayerX(Player p){
        pX = p;
    }
    
    public void addPlayerO(Player p){
        pO = p;
    }
    
    public void setBoard(GameBoard b){
        board = b;
    }
    
    public void end(){
        isOver = true;
    }
    
    // Trigger a win condition for Player pX
    public void pXWins(){
        pX.win();
        pO.lose();
        end();
    }
    // Trigger a win condition for Player pO
    public void pOWins(){
        pX.lose();
        pO.win();
        end();
    }
    // Trigger the game to end in a tie
    public void draw(){
        pX.draw();
        pO.draw();
        end();
    }
    
    // Determine the winner
    public void winner(char c){
        if(c == pX.getSymbol()){
            pXWins();
        }
        else if(c == pO.getSymbol()){
            pOWins();
        }
        
    }

    // Returns the player whose move it is right now
    public Player currentTurn(){
        if(board.getTurn() == board.X){
            return pX;
        }
        else{
            return pO;
        }
    }
    
    // Process one step of the main game loop
    public void step(){
        
        // If the Player on turn is AI, get the move from PlayerAI.search
        if(currentTurn().isAI()){
            Move m =((PlayerAI)currentTurn()).search(getBoard());
            System.out.println("AI playing move " + m.toString());
            getBoard().push(m);
            gui.updateMove(m);   
        }
        
        // If the player is human and a move has been received, process the move
        else if(!waitingForMove){
            playerMove.setPlayer(getBoard().getTurn());
            System.out.println("Player playing move " + playerMove.toString());
            getBoard().push(playerMove);
            gui.updateMove(playerMove);
            waitingForMove = true;
        }
        
        // Check for a win
        if(getBoard().is_winning_state()){
            getBoard().nextTurn();
            winner(getBoard().getTurn());
            gui.setWinnerLabel("Player " + getBoard().getTurn() + " wins!");
            gui.setpXScore("Player X | " + getPlayerX().toString());
            gui.setpOScore("Player O | " + getPlayerO().toString());
        }
        
        // Check for a tie
        else if(getBoard().is_draw_state()){
            draw();
            gui.setWinnerLabel("Draw!");
            gui.setpXScore("Player X | " + getPlayerX().toString());
            gui.setpOScore("Player O | " + getPlayerO().toString());
        }
    }
    
    // Receives an ActionEvent from the game buttons, gets their x,y coordinaes 
    //    and updates the board according to whose turn it was
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton g = (JButton)e.getSource();
        if(!isOver() && !currentTurn().isAI() && g.getName().equals(" ")){
            playerMove = new Move(getIndexFromPosition(g.getLocation().x),getIndexFromPosition(g.getLocation().y), ' ');
            waitingForMove = false;
        }
    }
    // Calculates the x,y indices of the button from the position based on the button's size
    private int getIndexFromPosition(int pos){
        return pos / (gui.getButtonSize());
    }
}
