/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe;

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
    
    // Main AI method
    // Searches the gameboard for a move using some algorithm
    // and returns the move
    public Move search(GameBoard b){
        Move m = optimalMove(b);
        if(m == null) m = randomMove(b);
        return m;
    }
    
    // Indicates this is an AI not a human player
    public boolean isAI(){
        return true;
    }
    
    // Gets the X for maxagent or O for minagent (!maxagent)
    public char getSymbol(boolean maxagent){
        if(maxagent){
            return 'X';
        }
        else{
            return 'O';
        }
    }
    
    // Algorithm that chooses a random legal move
    public Move randomMove(GameBoard b){
        ArrayList<Move> movelist = b.getLegalMoves();
        Random r = new Random();
        int index = r.nextInt(movelist.size());
        return movelist.get(index);
    }
    
    // Algorithm that uses a minimax tree search to choose the best move
    public Move optimalMove(GameBoard b){
        
        // Data structure for a Minimax search tree node
        class MinimaxNode{
            int count = 0;
            int value;
            GameBoard board;
            Move move;
            ArrayList<MinimaxNode> children;
            MinimaxNode parent;
            
            // Initialize all values
            MinimaxNode(int v, GameBoard b, Move m, MinimaxNode p){
                value = v;
                board = b;
                move = m;
                parent = p;
                children = null;
            }
            
            // Construct the search tree
            MinimaxNode construct(GameBoard b, int maxdepth, boolean maxagent){
                int currentdepth = 0;
                
                GameBoard nextboard;
                ArrayList<Move> movelist;
                ArrayList<MinimaxNode> clist;
                
                ArrayList<MinimaxNode> nodelist = new ArrayList<>();
                
                // Start with only the root node itself
                nodelist.add(this);
                
                // Breadth first, build the tree row by row
                while(currentdepth <= maxdepth){
                    currentdepth ++;
                    // Begins to store all of the children generated this iteration
                    //     so that they can be the next nodes to process in the next
                    //     iteration
                    ArrayList<MinimaxNode> nextnodelist = new ArrayList<>();
                    for(MinimaxNode currentnode : nodelist){
                        
                        // Get all legal moves from the current node
                        movelist = currentnode.board.getLegalMoves();
                        
                        // Begins to store all of the different boards that arise
                        //     from the various moves
                        // This will become the children value of currentnode
                        clist = new ArrayList<>();
                        
                        // Iterate over every move in the movelist
                        for(Move m : movelist){
                            
                            // Make a copy of the current board
                            nextboard = currentnode.board.getCopy();
                            
                            // Push a new move onto the copy
                            nextboard.push(m);
                            
                            // Calculate the evaluation of the move
                            value = evaluate(nextboard);
                            
                            // Add the move to the list of children
                            clist.add(new MinimaxNode(value, nextboard, m, currentnode));
                            
                        }
                        
                        // Assign the list to children of currentnode
                        currentnode.children = clist;
                        
                        // Add all children to nextnodelist for the next iteration
                        nextnodelist.addAll(clist);
                        
                    }
                    // Sets the list to iterate over as the next row of children
                    nodelist = nextnodelist;
                }
                
                // Solve the minimax tree
                traverse(this, maxagent);
                
                System.out.println(count + " nodes processed");
                
                // Return the root value which should now have a tree attached to it
                return this;
            }
            
            // Solve the minimax tree recursively
            void traverse(MinimaxNode node, boolean maxagent){
                // Base cases:
                // if(node.children == null) do nothing
                // if(node.children.size()==0) do nothing
                
                // Recursive cases:
                if(node.children != null){
                    if(node.children.size() > 0){
                        
                        for(MinimaxNode i : node.children){
                            // Before doing any calculations, make sure all children
                            //     are processed
                            // Alternate between maxagent and minagent between rows
                            traverse(i, !maxagent);
                        }
                        
                        MinimaxNode n;
                        // If the node is a max node, get the node with maximum 
                        //    value from its children
                        if(maxagent){
                            n = getmax(node.children);
                        }
                        // If the node is a min node, get the node with minimum 
                        //    value from its children
                        else{
                            n = getmin(node.children);
                        }
                        
                        // Sets the node value accordingly
                        node.value = n.value;
                        
                        // If the node is root, set the move so that it can be
                        //    retrieved later as the move to play.
                        if(node.parent == null){
                            node.move = n.move;
                        }
                    }
                }
            }
            
            // Print the tree recursively
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
            
            // Only 3 possible evaluations
            //  0: draw or nonterminal state
            //  1: win for X
            // -1: win for O
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
            
            // Gets the node with max value from the list
            MinimaxNode getmax(ArrayList<MinimaxNode> list){
                MinimaxNode m = list.get(0);
                for(MinimaxNode i : list){
                    if(i.value > m.value){
                        m = i;
                    }
                }
                return m;
            }
            
            // Gets the node with min value from the list
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
        
        // Runs the minimax search
        
        // Initialize the tree
        MinimaxNode root;
        
        // Determine if the AI agent is trying to maximize or minimize evaluation
        // X = max
        // O = min
        boolean maxagent = (getSymbol() == b.X);
        
        // Build the tree and assign to root
        root = new MinimaxNode(0, b, null, null).construct(b, 10, maxagent);
        System.out.println(root.move + " " + root.value);
        
        // Returns the move that the minimax tree calculates
        return root.move;
    }
    
}
