/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;
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
    
    private static final int GAME_WIDTH = 3;
    private static final int GAME_HEIGHT = 3;
    private static final int BUTTON_SIZE = 60;
    private static final int BUTTON_CUSHION = 10;
    private static final int FRAME_CUSHION = 100;
    private static final int OPTIONS_CUSHION = 50;
    private static final int WIN_CON = 3;
    
    private static final int MIN_WINDOW_X = 600;
    private static final int MIN_WINDOW_Y = 500;
    
    private int game_width;
    private int game_height;
    private int button_size;
    private int button_cushion;
    private int frame_cushion;
    private int wincon;
    
    private boolean XisAI = false;
    private boolean OisAI = false;
    
    private final Icon ico_x = new ImageIcon("x.png");
    private final Icon ico_o = new ImageIcon("o.png");
    private final Icon ico_xai = new ImageIcon("xai.png");
    private final Icon ico_oai = new ImageIcon("oai.png");
            
     
    public GameWindow() {
        this(GAME_WIDTH, GAME_HEIGHT, WIN_CON, BUTTON_SIZE, BUTTON_CUSHION, FRAME_CUSHION);
    }
    
    public GameWindow(int game_width, int game_height, int wincon, int button_size, int button_cushion, int frame_cushion){
        this.game_width = game_width;
        this.game_height = game_height;
        this.button_size = button_size;
        this.button_cushion = button_cushion;
        this.frame_cushion = frame_cushion;
        this.wincon = wincon;
        init();
        
    }
    public GameWindow(int button_size, int button_cushion, int frame_cushion){
        this(GAME_WIDTH, GAME_HEIGHT, WIN_CON, button_size, button_cushion, frame_cushion);
    }
    public GameWindow(int game_width, int game_height){
        this(game_width, game_height, WIN_CON, BUTTON_SIZE, BUTTON_CUSHION, FRAME_CUSHION);
    }
   
    
    public void setGame(Game g){
        this.g = g;
        init();
    }
    
    
    private void init(){
        
        g = new Game(new GameBoard(getGameWidth(), getGameHeight(), getStreakToWin()), this);
        g.addPlayerX(pX);
        g.addPlayerO(pO);
        int framesizex = game_width*(button_size + button_cushion)+frame_cushion;
        int framesizey = game_height*(button_size + button_cushion)+frame_cushion + OPTIONS_CUSHION;
        frame = new JFrame();
        frame.setTitle("Tic Tac Toe");
        frame.setSize(Math.max(framesizex, MIN_WINDOW_X), Math.max(framesizey, MIN_WINDOW_Y));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        System.out.println("init frames");
        initMainGamePanel(frame);
        initOptionsPanel(frame);
        initTextPanel(frame);
        
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        

        initthread();
    }
    public void initthread(){
        class GameThread implements Runnable{
            public void run(){
                while (!g.isOver()){
                    try{                   
                        g.step();
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        final GameThread ut = new GameThread();
        t = new Thread(ut);
        t.start();
    }
    public void reset(){
        g.end();
        frame.dispose();
        init();
    }
    
    private void initMainGamePanel(JFrame frame){
        GridLayout buttonRowLayout = new GridLayout(game_height, 1);
        GridLayout buttonLayout = new GridLayout(game_height, game_width);
        GridLayout mainGamePanelLayout = new GridLayout(1, game_width);
        mainGamePanelLayout.setHgap(button_cushion);
        buttonRowLayout.setVgap(button_cushion);
        mainGamePanel = new JPanel();
        mainGamePanel.setLayout(mainGamePanelLayout);
        mainGamePanel.setSize(game_height*(button_size + button_cushion), game_width*(button_size + button_cushion));
        
        initButtonGrid(mainGamePanel, buttonLayout);
        /*for(int y=0;y<game_width;y++){
            initButtonRow(mainGamePanel, buttonRowLayout);   
        }*/
        frame.add(mainGamePanel);
        
    }
    
    /*private void initButtonRow(JPanel parent, GridLayout layout){
        JPanel p = new JPanel();
        p.setLayout(layout);
        p.setSize(game_height*(button_size + button_cushion), (button_size + button_cushion));
        //panel.setLocation(0, (y * button_size + button_cushion));

        for(int x=0;x<game_height;x++){
            initButton(p);
        }
        parent.add(p);
    }*/
    
    private void initButton(JPanel parent){
        JButton newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size,button_size));
        newButton.addActionListener(g);
        //newButton.setBackground(Color.BLACK);
        newButton.setHorizontalAlignment(SwingConstants.CENTER);
        newButton.setName(" ");
        parent.add(newButton);
    }
    
    private void initButtonGrid(JPanel parent, GridLayout layout){
        JPanel p = new JPanel();
        p.setLayout(layout);
        //p.setSize(game_height*(button_size + button_cushion), (button_size + button_cushion));
        for(int x=0;x<game_height * game_width;x++){
            initButton(p);
        }
        parent.add(p);
        
    }
    
    private void initOptionsPanel(JFrame frame){
        bottomOptionsPanel = new JPanel();
        bottomOptionsPanel.setLayout(new FlowLayout());
        bottomOptionsPanel.setSize(100, 300);
        initOptions(bottomOptionsPanel);
        frame.add(bottomOptionsPanel);
    }
    
    private void initOptions(JPanel parent){
        
        //JLabel txt;
        JButton newButton;
        
        /*textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(3,2));
        // Field size options
        // Num rows
        txt = new JLabel("Num Rows:");
        txt.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldsPanel.add(txt);
        JTextField rowsTextField = new JTextField(10);
        rowsTextField.setName("R");
        textFieldsPanel.add(rowsTextField);
        
        // Num cols
        txt = new JLabel("Num Cols:");
        txt.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldsPanel.add(txt);
        JTextField colsTextField = new JTextField(10);
        colsTextField.setName("C");
        textFieldsPanel.add(colsTextField);
        
        // Win con
        txt = new JLabel("# Match to Win:");
        txt.setHorizontalAlignment(SwingConstants.RIGHT);
        textFieldsPanel.add(txt);
        JTextField winConTextField = new JTextField(10);
        winConTextField.setName("W");
        textFieldsPanel.add(winConTextField);
        
        
        parent.add(textFieldsPanel);*/
        
        playerOptionsPanel = new JPanel();
        playerOptionsPanel.setLayout(new GridLayout(2,1));
        
        
        newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size, button_size));
        newButton.setBackground(Color.BLACK);
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
        
        newButton = new JButton();
        newButton.setPreferredSize(new Dimension(button_size, button_size));
        newButton.setBackground(Color.BLACK);
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
        newButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                newGame();
            }
        });
        newButton.setName("N");
        newButton.setText("New Game");
        newGamePanel.add(newButton);
        
        parent.add(newGamePanel);
        //parent.add(textFieldsPanel);
        parent.add(playerOptionsPanel);
       
    }
    
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
    
    public void setWinCon(int win_condition){
        wincon = win_condition;
    }
    public void setGameHeight(int gameheight){
        game_height = gameheight;
    }
    public void setGameWidth(int gamewidth){
        game_width = gamewidth;
    }
       
    public boolean XisAI(){
        return XisAI;
    }
    
    public boolean OisAI(){
        return OisAI;
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
    
    public void setX(JButton b){
        b.setName("X");
        b.setIcon(ico_x);
        b.setBackground(Color.BLACK);
    }
    public void setO(JButton b){
        b.setName("O");
        b.setIcon(ico_o);
        b.setBackground(Color.BLACK);
    }
    public void clear(JButton b){
        b.setName(" ");
        b.setIcon(null);
    }
    public void newGame(){
        /*Component[] c = textFieldsPanel.getComponents();
        String s;
        s = ((JTextField)c[1]).getText();
        if(!s.isBlank()) setGameHeight(Integer.parseInt(s));
        s = ((JTextField)c[3]).getText();
        if(!s.isBlank()) setGameWidth(Integer.parseInt(s));
        s = ((JTextField)c[5]).getText();
        if(!s.isBlank()) setWinCon(Integer.parseInt(s));*/
        
        reset();
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
    public void updateMove(Move m){
        Component[] c = mainGamePanel.getComponents();
        JPanel cc = (JPanel)c[0];
        Component[] ccc = cc.getComponents();
        JButton b = (JButton)ccc[m.gety() * game_height + m.getx()];
        if(m.getPlayer() == 'X') setX(b);
        else if(m.getPlayer() == 'O') setO(b);
    }
    
    
}
