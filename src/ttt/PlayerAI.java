/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ttt;

import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author osiri
 */
public class PlayerAI extends Player{
    PlayerAI(char c){
        super(c);
    }
    
    public Move search(GameBoard b){
        Move m = optimalMove(b);
        if(m == null) m = randomMove(b);
        return m;
    }
    
    public boolean isAI(){
        return true;
    }
    
    public char getSymbol(boolean maxagent){
        if(maxagent){
            return 'X';
        }
        else{
            return 'O';
        }
    }
    
    public Move randomMove(GameBoard b){
        ArrayList<Move> movelist = b.getLegalMoves();
        Random r = new Random();
        int index = r.nextInt(movelist.size());
        return movelist.get(index);
    }
    
    public Move optimalMove(GameBoard b){
        
        
        
        class MinimaxNode{
            int count = 0;
            int value;
            GameBoard board;
            Move move;
            ArrayList<MinimaxNode> children;
            MinimaxNode parent;
            
            
            
            
            MinimaxNode(int v, GameBoard b, Move m, MinimaxNode p){
                value = v;
                board = b;
                move = m;
                parent = p;
                children = null;
            }
            
            MinimaxNode construct(GameBoard b, int maxdepth, boolean maxagent){
                int currentdepth = 0;
                boolean winfound = false;
                
                GameBoard myboard;
                GameBoard nextboard;
                ArrayList<Move> movelist;
                ArrayList<MinimaxNode> clist;
                
                ArrayList<MinimaxNode> nodelist = new ArrayList<>();
                
                nodelist.add(this);
                while(currentdepth <= maxdepth){
                    currentdepth ++;
                    ArrayList<MinimaxNode> nextnodelist = new ArrayList<>();
                    //for(MinimaxNode currentnode : nodelist){
                    MinimaxNode currentnode;
                    
                    for(int x=0;x<nodelist.size();x++){
                        currentnode = nodelist.get(x);
                        if(winfound) return this;
                        
                        movelist = currentnode.board.getLegalMoves();
                        
                        clist = new ArrayList<>();
                        
                        for(Move m : movelist){
                            nextboard = currentnode.board.getCopy();
                            nextboard.push(m);
                            value = evaluate(nextboard);
                            /*if(!maxagent){
                                if(currentdepth%2 == 0 && value == -1){
                                    nodelist.remove(currentnode);
                                    x--;
                                    break;
                                }
                                else if(currentdepth%2 == 1 && value == 1){
                                    nodelist.remove(currentnode);
                                    x--;
                                    break;
                                }
                            }
                            else{
                                if(currentdepth%2 == 0 && value == 1){
                                    nodelist.remove(currentnode);
                                    x--;
                                    break;
                                }
                                else if(currentdepth%2 == 1 && value == -1){
                                    nodelist.remove(currentnode);
                                    x--;
                                    break;
                                }
                            }*/
                            clist.add(new MinimaxNode(value, nextboard, m, currentnode));
                            
                        }
                        currentnode.children = clist;
                        nextnodelist.addAll(clist);
                        
                    }
                    nodelist = nextnodelist;
                    
                    
                    /*if((maxagent && this.value == -1) || (!maxagent && this.value == 1)){
                        winfound = true;
                    }*/
                }
                
                traverse(this, maxagent);
                System.out.println(count + " nodes processed");
                return this;
            }
            
            void traverse(MinimaxNode node, boolean maxagent){
                if(node.children != null){
                    if(node.children.size() > 0){
                        
                        for(MinimaxNode i : node.children){
                            traverse(i, !maxagent);
                        }
                        
                        MinimaxNode n;
                        if(maxagent){
                            n = getmax(node.children);
                        }
                        else{
                            n = getmin(node.children);
                        }
                        
                        node.value = n.value;
                        if(node.parent == null){
                            node.move = n.move;
                        }
                    }
                }
            }
            
            void printtree(MinimaxNode node){
                System.out.print(node.value);
                
                if(node.children != null){
                    if(node.children.size() > 0)
                        System.out.print("| ");
                        for(MinimaxNode i : node.children){
                            System.out.print(i.value + " ");
                        }
                        System.out.println();
                        for(MinimaxNode i : node.children){
                            printtree(i);
                        }
                    }
                
            }
            
            int evaluate(GameBoard b){
                if(b.is_winning_state()){
                    b.nextTurn();
                    if(b.getTurn() == b.X){
                        //System.out.println("Win found for X");
                        return 1;
                    }
                    else{
                        //System.out.println("Win found for O");
                        return -1;
                    }
                }
                return 0;
            }
            
            MinimaxNode getmax(ArrayList<MinimaxNode> list){
                MinimaxNode m = list.get(0);
                for(MinimaxNode i : list){
                    if(i.value > m.value){
                        m = i;
                    }
                }
                return m;
            }
            MinimaxNode getmin(ArrayList<MinimaxNode> list){
                MinimaxNode m = list.get(0);
                for(MinimaxNode i : list){
                    if(i.value < m.value){
                        m = i;
                    }
                }
                return m;
            }
            
            
        }

        MinimaxNode root;
        boolean maxagent = (getSymbol() == b.X);
        root = new MinimaxNode(0, b, null, null).construct(b, 10, maxagent);
        System.out.println(root.move + " " + root.value);
        return root.move;
    }
    
}
