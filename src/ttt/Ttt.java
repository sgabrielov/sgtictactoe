package ttt;
import javax.swing.*;

public class Ttt
{
    
    public static void main(String [] args){
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new GameWindow();
            }
        });
    }
}