/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author osiri
 */
public class GameWindow{
    
    Game g;
    JFrame frame;
    
    Thread t;

    JButton button;
    JPanel mainGamePanel, bottomOptionsPanel, textFieldsPanel, playerOptionsPanel, newGamePanel, textPanel;
    
    JLabel winner;
    JLabel pXScore;
    JLabel pOScore;
    
    Player pX = new Player('X');
    Player pO = new Player('O');
     
    public boolean ready = true;
    
    // Default values
    private static final int GAME_WIDTH = 3;
    private static final int GAME_HEIGHT = 3;
    private static final int BUTTON_SIZE = 60;
    private static final int BUTTON_CUSHION = 10;
    private static final int FRAME_CUSHION = 50;
    private static final int OPTIONS_CUSHION = 50;
    private static final int WIN_CON = 3;
    
    // Minimize size to make sure the frame fits all of the necessary elements
    private static final int MIN_WINDOW_X = 600;
    private static final int MIN_WINDOW_Y = 100;
    
    // Game parameters
    private int game_width;
    private int game_height;
    private int button_size;
    private int button_cushion;
    private int frame_cushion;
    private int wincon;
    
    private boolean XisAI = false;
    private boolean OisAI = false;
    
    // Graphics
    private final Icon ico_x = new ImageIcon("x.png");
    private final Icon ico_o = new ImageIcon("o.png");
    private final Icon ico_xai = new ImageIcon("xai.png");
    private final Icon ico_oai = new ImageIcon("oai.png");
            
    // Default constructor initializes to default values 
    public GameWindow() {
        this(GAME_WIDTH, GAME_HEIGHT, WIN_CON, BUTTON_SIZE, BUTTON_CUSHION, FRAME_CUSHION);
    }
    
    // Initialize all available configuration values
    public GameWindow(int game_width, int game_height, int wincon, int button_size, int button_cushion, int frame_cushion){
        this.game_width = game_width;
        this.game_height = game_height;
        this.button_size = button_size;
        this.button_cushion = button_cushion;
        this.frame_cushion = frame_cushion;
        this.wincon = wincon;
        init();
        
    }
    // Initialize only frame specific configuration values
    public GameWindow(int button_size, int button_cushion, int frame_cushion){
        this(GAME_WIDTH, GAME_HEIGHT, WIN_CON, button_size, button_cushion, frame_cushion);
    }
    // Initialize only game specific configuration values
    public GameWindow(int game_width, int game_height){
        this(game_width, game_height, WIN_CON, BUTTON_SIZE, BUTTON_CUSHION, FRAME_CUSHION);
    }
       
    // Set up the game thread and the entire GUI
    private void init(){
        
        // Set up the Game object
        initgame();       
        
        // Set up the GUI frame
        initframe();

        // Set up the thread that runs the Game.step() process
        initthread();
    }
    
    // Sets up the game object
    private void initgame(){
        g = new Game(new GameBoard(getGameWidth(), getGameHeight(), getStreakToWin()), this);
        g.addPlayerX(pX);
        g.addPlayerO(pO);
    }
    
    // Sets up the GUI frame
    private void initframe(){
        int framesizex = game_width*(button_size + button_cushion)+frame_cushion;
        int framesizey = game_height*(button_size + button_cushion)+frame_cushion + OPTIONS_CUSHION;
        frame = new JFrame();
        frame.setTitle("Tic Tac Toe");
        frame.setSize(Math.max(framesizex, MIN_WINDOW_X), Math.max(framesizey, MIN_WINDOW_Y));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Sets up the main game buttons
        initMainGamePanel(frame);
        
        // Sets up the options panel
        initOptionsPanel(frame);
        
        // Sets up the panel that displays the stats and game result
        initTextPanel(frame);
        
        frame.setLayout(new FlowLayout());
        
        // Display the frame
        frame.setVisible(true);
    }
    
    // Set up the thread that runs the Game.step() process
    public void initthread(){
        class GameThread implements Runnable{
            public void run(){
                
                // Exits when the Game ends
                while (!g.isOver()){
                    try{
                        // Run one step of the main loop
                        g.step();
                        
                        // But only once every 100 milliseconds
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        
        // Add the runnable object to a new thread process and start
        final GameThread ut = new GameThread();
        t = new Thread(ut);
        t.start();
    }
    
    // Restart the game and redraw the GUI
    public void reset(){
        g.end();
        frame.dispose();
        init();
    }
    
    // Sets up the main game buttons
    private void initMainGamePanel(JFrame frame){
        
        // Configure the layout
        GridLayout buttonRowLayout = new GridLayout(game_height, 1);
        GridLayout buttonLayout = new GridLayout(game_height, game_width);
        GridLayout mainGamePanelLayout = new GridLayout(1, game_width);
        mainGamePanelLayout.setHgap(button_cushion);
        buttonRowLayout.setVgap(button_cushion);
        
        // Initialize the panel
        mainGamePanel = new JPanel();
        mainGamePanel.setLayout(mainGamePanelLayout);
        mainGamePanel.setSize(game_height*(button_size + button_cushion), game_width*(button_size + button_cushion));
        
        // Sets up an entire grid of gameboard buttons
        initButtonGrid(mainGamePanel, buttonLayout);
        
        // Add the panel to the GUI
        frame.add(mainGamePanel);
        
    }
    
    // Sets up a single gameboard button
    private void initButton(JPanel parent){
        JButton newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size,button_size));
        newButton.addActionListener(g);
        newButton.setHorizontalAlignment(SwingConstants.CENTER);
        newButton.setName(" ");
        parent.add(newButton);
    }
    
    // Sets up an entire grid of gameboard buttons
    private void initButtonGrid(JPanel parent, GridLayout layout){
        JPanel p = new JPanel();
        p.setLayout(layout);
        for(int x=0;x<game_height * game_width;x++){
            // Sets up a single gameboard button
            initButton(p);
        }
        // Add the buttons to the main game panel 
        parent.add(p);
        
    }
    
    // Sets up the options panel
    private void initOptionsPanel(JFrame frame){
        bottomOptionsPanel = new JPanel();
        bottomOptionsPanel.setLayout(new FlowLayout());
        bottomOptionsPanel.setSize(100, 300);
        
        // Sets up the objects inside the options panel
        initOptions(bottomOptionsPanel);
        frame.add(bottomOptionsPanel);
    }
    
    // Sets up the objects inside the options panel
    private void initOptions(JPanel parent){
        
        JButton newButton;

        playerOptionsPanel = new JPanel();
        playerOptionsPanel.setLayout(new GridLayout(2,1));
        
        // Add the button to toggle AI for player X.
        newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size, button_size));
        newButton.setBackground(Color.BLACK);
        
        
        // The ActionListener updates the button graphic and replaces Player X
        //     with PlayerAI X
        newButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                xai((JButton)e.getSource());
            }
        });
        playerOptionsPanel.add(newButton);
        newButton.setName("XAI");
        if(XisAI) newButton.setIcon(ico_xai);
        else newButton.setIcon(ico_x);
        playerOptionsPanel.add(newButton);
        
        // Add the button to toggle AI for player O.
        newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size, button_size));
        newButton.setBackground(Color.BLACK);
        // The ActionListener updates the button graphic and replaces Player O
        //     with PlayerAI O
        newButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                oai((JButton)e.getSource());
            }
        });
        newButton.setName("OAI");
        if(OisAI) newButton.setIcon(ico_oai);
        else newButton.setIcon(ico_o);
        playerOptionsPanel.add(newButton);
        
        
        
        // Start new game button
        newGamePanel = new JPanel();
        newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size*3,button_size));
        // The ActionListener triggers the Game thread to end and the frame to 
        //     redraw
        newButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                newGame();
            }
        });
        newButton.setName("N");
        newButton.setText("New Game");
        newGamePanel.add(newButton);
        
        
        // Add the options buttons to the options panel
        parent.add(playerOptionsPanel);
        parent.add(newGamePanel);
       
    }
    
    // Sets up the panel that displays the stats and game result
    private void initTextPanel(JFrame parent){
        JPanel p;
        textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());
        p = new JPanel();
        p.setLayout(new GridLayout(2,1));
        pXScore = new JLabel();
        p.add(pXScore);
        pOScore = new JLabel();
        p.add(pOScore);
        textPanel.add(p);
        winner = new JLabel();
        winner.setFont(new Font("Calibri", Font.PLAIN, 48));
        textPanel.add(winner);
        parent.add(textPanel);
    }
    
    // Accessor methods
    public int getGameWidth(){
        return game_width;
    }
    
    public int getGameHeight(){
        return game_height;
    }
    
    public int getStreakToWin(){
        return wincon;
    }
    
    public int getButtonSize(){
        return button_size;
    }
    
    public int getButtonCushion(){
        return button_cushion;
    }
    
    public boolean XisAI(){
        return XisAI;
    }
    
    public boolean OisAI(){
        return OisAI;
    }
    
    // Mutator methods
    public void setWinCon(int win_condition){
        wincon = win_condition;
    }
    public void setGameHeight(int gameheight){
        game_height = gameheight;
    }
    public void setGameWidth(int gamewidth){
        game_width = gamewidth;
    }

    public void toggleXasAI(JButton b){
        if(XisAI){
            b.setIcon(ico_x);
        }
        else{
            b.setIcon(ico_xai);
        }
        XisAI = !XisAI;
    }
    public void toggleOasAI(JButton b){
        if(OisAI){
            b.setIcon(ico_o);
        }
        else{
            b.setIcon(ico_oai);
        }
        OisAI = !OisAI;
    }
    public void setWinnerLabel(String s){
        winner.setText(s);
    }
    
    public void setpXScore(String s){
        pXScore.setText(s);
    }
    
    public void setpOScore(String s){
        pOScore.setText(s);
    }
    
    // Process the ActionEvent for clicking on the AI toggle for player X
    public void xai(JButton b){
        if(!XisAI()){
            pX = new PlayerAI(g.getBoard().X);
            g.addPlayerX(pX);
        }
        else{
            pX = new Player(g.getBoard().X);
            g.addPlayerX(pX);
        }
        toggleXasAI(b);
        System.out.println("XAI");
    }
    // Process the ActionEvent for clicking on the AI toggle for player O
    public void oai(JButton b){
        if(!OisAI()){
            pO = new PlayerAI(g.getBoard().O);
            g.addPlayerO(pO);
        }
        else{
            pO = new Player(g.getBoard().O);
            g.addPlayerO(pO);
        }
        toggleOasAI(b);
        System.out.println("OAI");
    }
    
    // Changes a button on the Game Board to display an X
    public void setX(JButton b){
        b.setName("X");
        b.setIcon(ico_x);
        b.setBackground(Color.BLACK);
    }
    // Changes a button on the Game Board to display an O
    public void setO(JButton b){
        b.setName("O");
        b.setIcon(ico_o);
        b.setBackground(Color.BLACK);
    }
    // Clears the button on the Game Board
    public void clear(JButton b){
        b.setName(" ");
        b.setIcon(null);
    }
    // Restart the game thread and redraw the GUI
    public void newGame(){     
        reset();
    }
    
    // Update the game board based on the x,y and Player values of the Move
    public void updateMove(Move m){
        Component[] c = mainGamePanel.getComponents();
        JPanel cc = (JPanel)c[0];
        Component[] ccc = cc.getComponents();
        JButton b = (JButton)ccc[m.gety() * game_height + m.getx()];
        if(m.getPlayer() == 'X') setX(b);
        else if(m.getPlayer() == 'O') setO(b);
    }
    
    
}
