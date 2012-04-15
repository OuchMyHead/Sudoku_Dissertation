/**
 * @(#)SudokuGrid.java
 *
 * This class creates the Sudoku grid for the GUI and any content
 * that should be displayed within the grid
 *
 * @ James Mathieson
 * @ version 1.00 16/03/2010
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SudokuGrid extends  GUI_Extension {
	
	private int selectedCellX=0,selectedCellY=10,selectedCell=-1;
	private int[] currentPuzzleState;
	private int[] startingPuzzleState;
	private SudokuPuzzle sudokuPuzzle;
	private HelpTools helpTools;
	
	/*
	 * Constructor - sets preferred size for JPanel
	 * and applies reference to the helpTools object
	 * that has been created in the GUI
	 */
	public SudokuGrid(HelpTools ref){
		setPreferredSize(new Dimension(360,360));
		helpTools = ref;
	}

	/*
	 * Create paint component and call methods to draw grid and content
	 */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawGrid(g);					//call method to draw Sudoku puzzle
        if(startingPuzzleState!=null){	//if no puzzle has been assigned then do not do these things
        	drawSelectedCell(g);			//add dotted line to b
        	drawCurrentPuzzleState(g);
        }
    }
    
    
    /*
	 * This method draws the sudoku grid
	 *
	 */    
    private void drawGrid(Graphics g){
    	int spacing = 2;						// spacing between subgrids
        int x = 0;								// starting x-coordinate
        int y = 10;								// starting y-coordinate
        int size = 114;							// size of subgrid
        for(int i=1;i<=9;i++){					// draw sub grids	
        	int x2=x;
        	int y2=y;
        	int size2=38;
        	for(int c=1;c<=9;c++){				// draw individual squares	
        		g.setColor(Color.WHITE);
        		g.fillRect(x2, y2, size2, size2);
        		g.setColor(Color.lightGray);
        		g.drawRect(x2, y2, size2, size2);
        		x2 += size2;						//increase the size of x2
        		if(c%3==0){							// if count is divisible by 3 then increase y and reset x
        			y2 += size2;					
        			x2 = x;	
        		}
        	}
        	g.setColor(Color.GRAY);			// draw subgrids second so they
        	g.drawRect(x, y, size, size);		// appear above the individual cells
        	x += size+spacing;					// increase x
        	if(i%3==0){							// if count is divisible by 3 then increase y and reset x
        		y += size+spacing;
        		x = 0;	
        	}
        }	
    }
    
    /*
     * This method draws two broken line rectangles within
     * the cell the user has selected
     */
    private void drawSelectedCell(Graphics g){
    	//declare variables for the two squares to be drawn
    	if(selectedCell!=-1){
    		int x=selectedCellX+1, y=selectedCellY+1,height=36, width=36;
    		int x2=selectedCellX+2, y2=selectedCellY+2,height2=34, width2=34;
	    	//create new 2D object
	    	Graphics2D g2 = (Graphics2D) g;
	    	//define the metric spacing between each dash
	    	float dash[] = { 1.0f };
	    	//set new stroke and set colour
	    	g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
			g2.setPaint(new Color(0,0,255));
			//create the two new rectanges
			Rectangle r = new Rectangle(x,y,height,width);
			Rectangle r2 = new Rectangle(x2,y2,height2,width2);
			//draw the rectangles
			g2.draw(r);
			g2.draw(r2);
    	}
    }
    
    /*
     * This method draws the current puzzle state to the Sudoku grid
     */
    private void drawCurrentPuzzleState(Graphics g){
    	//variables for defining squares
    	int x=0, y=10, height=38, width=38, spacing=2;
    	//create font for numbers
    	Font numberFont = new Font("Arial", Font.BOLD, 16);
    	for(int i=1;i<=81;i++){
    		g.setFont(numberFont);
    		//set font for drawing numbers
    		// check if number is user input or given number and colour appropriately
    		if(currentPuzzleState[i-1]!=startingPuzzleState[i-1]){
    			g.setColor(new Color(0,0,255));
    		}
    		else{
    			g.setColor(Color.BLACK);
    		}
    		//check a number is not zero - if it is then don't draw it
    		if(currentPuzzleState[i-1]!=0){
    			//draw number
        		g.drawString(Integer.toString(currentPuzzleState[i-1]), x+16, y+27);
    		}
    		// if it is a zero then get the cells candidates from the helpTools
    		else{
    			drawCandidates(g,x,y,i);
    		}
    		//change values for x and y where appropriate
    		x+=width;
    		if(i%3==0){
        		x+=spacing;
    		}
    		if(i%9==0){
    			x=0;
    			y=y+height;
    		}
    		if(i%27==0){
    			y+=spacing;
    		}
    	}
    }
    
    /*
     * This method draws the candidates within the current
     * Sudoku grid in accordance to the users current answers
     */
    private void drawCandidates(Graphics g, int rX, int rY, int cell){
    	//variable for defining squares holding candidates
    	int x=rX, y=rY, height=12, width=12;
    	//array for holding candidates
    	boolean[] candidates;
    	//create font for candidates
    	Font candidateFont = new Font("Arial", Font.PLAIN, 10);
    	//draw candidates
    	//set font for drawing candidates
    	g.setFont(candidateFont);
    	g.setColor(new Color(102,102,102));
    	candidates = helpTools.getCandidates(currentPuzzleState,cell);		//send the puzzles current state and the cell candidates are wanted for
    	for(int c=0;c<candidates.length;c++){
    		if(candidates[c]==true){
    			//draw number
        		g.drawString(Integer.toString(c+1), x+4, y+12);
    		}
    		//change values for x and y where appropriate
    		x+=width;
    		if((c+1)%3==0){
    			x=rX;
    			y=y+height;
    		}
    	}
    }
    
    /*
     * This method calculate which cell was selected.
     */
    protected void calculatePosition(int clickX, int clickY){
    	//variables for calculating position
		int x=0, y=10,height=38, width=38, spacing = 2;
    	for(int i=1;i<=81;i++){
    		if((clickX>=x)&&(clickX<=x+width)&&(clickY>=y)&&(clickY<=y+height)){				//check if the click was in x and y region of drawn number
    			if((startingPuzzleState!=null)&&(startingPuzzleState[i-1]==0)){			//check cell does not hold given number - if it does then it cannot be accessed as we do not want it changed
    				selectedCellX=x;							//assign value for currently selected cell's x co-ordinate
    				selectedCellY=y;							//assign value for currently selected cell's y co-ordinate
    				selectedCell=i;								//assign value for currently selected cell
    				repaint();									//repaint
    			}
    			break;									//break out of loop
    		}
    		//change values for x and y where appropriate
    		x+=width;
    		if(i%3==0){
        		x+=spacing;
    		}
    		if(i%9==0){
    			x=0;
    			y+=height;
    		}
    		if(i%27==0){
    			y+=spacing;
    		}
    	}
     }
    
     /*
      * This method receives and sets the original starting state for the puzzle
      * A copy of the original state is made and passed to the current
      * state of the puzzle
      */
     public void setPuzzle(SudokuPuzzle sp){
     	//set reference for sudokuPuzzle
     	sudokuPuzzle = sp;
     	//create new current/starting puzzle states
     	startingPuzzleState = sudokuPuzzle.getPuzzle();
     	currentPuzzleState = sudokuPuzzle.getPuzzle();
     	//repaint so new puzzle is displayed
     	repaint();
     }
     
     /*
      * This method receives a value to add to selected cell
      * This value is added to the currentPuzzleState array which
      * reflects the steps the player has taken to solve the puzzle
      *
      * The method then returns whether all cells contain numbers
      */
     public boolean addToCurrentState(int value){
     	boolean complete = false;
     	if(selectedCell!=-1){
     		//add new move to undo in help tools
     		helpTools.addToUndo(deepCopy(currentPuzzleState));
     		helpTools.clearRedoStack();
     		complete=true;
     		currentPuzzleState[selectedCell-1] = value;
     		//repaint so any changes are reflected
     		repaint();
     		//check if all cells contain a number - if they do then the puzzle has been completed by the player (but is it right?)
     		for(int cell:currentPuzzleState)
     		if(cell==0)
     			complete=false;
     	}
     	return complete;
     }
     
     /*
      * This method returns whether the players solution
      * is the correct solution
      */
     public boolean foundSolution(){
     	boolean found=true;
     	for(int i=0;i<currentPuzzleState.length;i++)
     		if(currentPuzzleState[i]!=sudokuPuzzle.getSolution()[i])
     			found=false;
		return found;
     }
     
     /*
      * This method sets the value of the selected cell
      * This removes any visual representation from the Sudoku
      * Grid
      */
     public void removeFromCurrentState(){
     	currentPuzzleState[selectedCell-1] = 0;
     	//repaint so any changes are reflected
     	repaint();	
     }
     
     /*
      * This method sets the currentPuzzleState to the
      * puzzle received
      */
     public void setCurrentState(int[] puzzle, String s){
     	if(s.equals("undo")){
     		//add current state to redo in help tools
     		helpTools.addToRedo(deepCopy(currentPuzzleState));
     	}
     	else if(s.equals("redo")){
     		//add current state to redo in help tools
     		helpTools.addToUndo(deepCopy(currentPuzzleState));
     	}
     	currentPuzzleState=puzzle;
     	repaint();
     }
     
     /*
	 * This method recieves an an int[]
	 * and returns a deep copy of this array
	 */
	private static int[] deepCopy(int[] original){
		int[] copy = new int[original.length];			//create new multidimensional array the same length as original	
		for(int c=0;c<original.length;c++){
			copy[c] = original[c];						//copy values to new array
		}
		return copy;
	}
     
}// end of SudokuGrid class