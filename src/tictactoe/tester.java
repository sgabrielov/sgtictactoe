package tictactoe;
import javax.swing.*;

public class tester{
    
    public static void main(String [] args){
        
        // invokeLater makes sure that the execution of the game loop doesn't
        //     cause the GUI to become unresponsive
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                // Initialize the GUI and subsequently the main game
                new GameWindow();
            }
        });
    }
}