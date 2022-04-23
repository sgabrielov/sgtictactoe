/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
/**
 *
 * @author osiri
 */
public class Game implements ActionListener{
    private GameBoard board;
    private Player pX;
    private Player pO;
    private boolean isOver;
    
    private boolean waitingForMove = true;
    
    Move playerMove;
    
    GameWindow gui;
    
    Game(GameBoard b, GameWindow in){
        gui = in;
        board = b;
        isOver = false;
    }
    
    public void addPlayerX(Player p){
        pX = p;
    }
    
    public void addPlayerO(Player p){
        pO = p;
    }
    
    public Player getPlayerX(){
        return pX;
    }
    
    public Player getPlayerO(){
        return pO;
    }
    
    public GameBoard getBoard(){
        return board;
    }
    
    public void setBoard(GameBoard b){
        board = b;
    }
    
    public void reset(GameBoard b){
        board = b;
        end();
    }
    public void pXWins(){
        pX.win();
        pO.lose();
        end();
    }
    public void pOWins(){
        pX.lose();
        pO.win();
        end();
    }
    
    public void draw(){
        pX.draw();
        pO.draw();
        end();
    }
    
    public void winner(char c){
        if(c == pX.getSymbol()){
            pXWins();
        }
        else if(c == pO.getSymbol()){
            pOWins();
        }
        
    }
    
    
    public boolean isOver(){
        return isOver;
    }
    
    public Player currentTurn(){
        if(board.getTurn() == board.X){
            return pX;
        }
        else{
            return pO;
        }
    }
    public void end(){
        isOver = true;
    }
    public void step(){
        if(currentTurn().isAI()){
            Move m =((PlayerAI)currentTurn()).search(getBoard());
            System.out.println("AI playing move " + m.toString());
            getBoard().push(m);
            gui.updateMove(m);   
        }

        else if(!waitingForMove){
            playerMove.setPlayer(getBoard().getTurn());
            System.out.println("Player playing move " + playerMove.toString());
            getBoard().push(playerMove);
            gui.updateMove(playerMove);
            waitingForMove = true;
        }
        if(getBoard().is_winning_state()){
            getBoard().nextTurn();
            winner(getBoard().getTurn());
            gui.setWinnerLabel("Player " + getBoard().getTurn() + " wins!");
            gui.setpXScore("Player X | " + getPlayerX().toString());
            gui.setpOScore("Player O | " + getPlayerO().toString());
        }
        else if(getBoard().is_draw_state()){
            draw();
            gui.setWinnerLabel("Draw!");
            gui.setpXScore("Player X | " + getPlayerX().toString());
            gui.setpOScore("Player O | " + getPlayerO().toString());
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton g = (JButton)e.getSource();
        if(!isOver() && !currentTurn().isAI() && g.getName() == " "){
            playerMove = new Move(getIndexFromPosition(g.getLocation().x),getIndexFromPosition(g.getLocation().y), ' ');
            waitingForMove = false;
        }
    }
    
    private int getIndexFromPosition(int pos){
        return pos / (gui.getButtonSize());
    }
}
