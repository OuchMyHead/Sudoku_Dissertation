/**
 * @(#)HelpTools.java
 *
 * This class provides the functionality
 * of the help tools available to the
 * user
 *
 * @James Mathieson
 * @version 1.00 20/03/2010
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Arrays;

public class HelpTools extends GUI_Extension {
	
	private JLabel markUp, hint, step, validate, undo;
	private JPanel helpToolsSelection1, colourSolverTool;
	private JRadioButton colourRadioButton, ultraColourRadioButton;
	private boolean showCandidates;
	private Stack<int[]> undoStack, redoStack;
	private int[] puzzle;
	private GUI gui;
	
	//sudokuGrid object that is using the helptools
	private SudokuGrid sudokuGrid;
	
	/*
	 * The constructor
	 * Adds the border and sets the layout
	 */
	public HelpTools(GUI ref){
		gui=ref;
		this.setPreferredSize(new Dimension(293,206));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray),"Help Tools"));
    	this.setLayout(new GridLayout(2,0));
    	this.add(getTopHalf());
    	this.add(getBottomHalf());
    	//show candidates is false by default
    	showCandidates=false;
	}
	
	/*
	 * This method sets a reference to the sudokuGrid
	 * using the help tools so it the redraw method can
	 * be called from here.
	 */
	public void setSudokuGridRef(SudokuGrid ref){
		sudokuGrid=ref;
	}
	
	 /*
     * This method returns a JPanel that displays the
     * first four help tools the user can choose from
     */
	private JPanel getTopHalf(){
		//create JPanel to hold first group of help tools
    	helpToolsSelection1 = new JPanel();
    	helpToolsSelection1.setLayout(new GridLayout()); 
    	//create image icons and get images that represent solver tools and add to JLabels- set cursor to hand tool when going over
    	ImageIcon icon = new ImageIcon("images/markUp.img.png", "description");
    	markUp = new JLabel(icon);
    	markUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	icon = new ImageIcon("images/hint.img.png", "description");
    	hint = new JLabel(icon);
    	hint.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	icon = new ImageIcon("images/step.img.png", "description");
    	step = new JLabel(icon);
    	step.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	icon = new ImageIcon("images/validate.img.png", "description");
    	validate = new JLabel(icon);
    	validate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//add first group of help tools
    	helpToolsSelection1.add(markUp);
    	helpToolsSelection1.add(hint);
    	helpToolsSelection1.add(step);
    	helpToolsSelection1.add(validate);
    	return helpToolsSelection1;
	}
	
	 /*
     * This method returns a JPanel that displays displays
     * the second group of help tools the user can choose from
     */
	private JPanel getBottomHalf(){
		//create JPanel to hold second group of help tools
    	JPanel helpToolsSelection2 = new JPanel();
    	helpToolsSelection2.setLayout(new FlowLayout(FlowLayout.CENTER,0,23));
    	//create image icons and get images that represent solver tools and add to JLabels- set cursor to hand tool when going over
    	ImageIcon icon = new ImageIcon("images/undo.img.png", "description");
    	undo = new JLabel(icon);
    	undo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//add second group of help tools
    	helpToolsSelection2.add(getColourSolver());
    	helpToolsSelection2.add(undo);
    	return helpToolsSelection2;
	}
	
	 /*
     * This method returns a JPanel that contains the colour solver
     * help tool
     */
	private JPanel getColourSolver(){
		//create new JPanel to hold the colour grid and the radio buttons (also add padding box)
    	colourSolverTool = new JPanel(new FlowLayout());
    	//create new colourGrid object and pass object reference
    	JPanel colour = new ColourGrid(this);
    	colour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	//create radio buttons
    	colourRadioButton = new JRadioButton("Colour");
    	colourRadioButton.setSelected(true);
    	ultraColourRadioButton = new JRadioButton("Ultra Colour");
    	//group radio buttons
    	ButtonGroup group = new ButtonGroup();
    	group.add(colourRadioButton);
    	group.add(ultraColourRadioButton);
    	//create JPanel to hold radio buttons
    	JPanel radioButtons = new JPanel();
    	radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.PAGE_AXIS));
    	radioButtons.add(colourRadioButton);
    	radioButtons.add(ultraColourRadioButton);
    	//create a box to add padding
    	Dimension minSize = new Dimension(56, 0);
		Dimension prefSize = new Dimension(56, 0);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
		//add content coloursolver toold
    	colourSolverTool.add(colour);
    	colourSolverTool.add(radioButtons);
    	colourSolverTool.add(new Box.Filler(minSize, prefSize, maxSize));
    	return colourSolverTool;
	}
	
	/*
	 * This method calculates which help tool was selected 
	 */
	protected void calculatePosition(int clickX, int clickY){
		//check if markUp tool was selected
		if((clickX>=markUp.getX())&&(clickX<=markUp.getX()+markUp.getWidth())){
    		if((clickY>=markUp.getY())&&(clickY<=markUp.getY()+markUp.getHeight())){
    			markUp();
    		}
		}
		//check if hint tool was selected
		if((clickX>=hint.getX())&&(clickX<=hint.getX()+hint.getWidth())){
    		if((clickY>=hint.getY())&&(clickY<=hint.getY()+hint.getHeight())){
    			hint();
    		}
		}
		//check if step tool was selected
		if((clickX>=step.getX())&&(clickX<=step.getX()+step.getWidth())){
    		if((clickY>=step.getY())&&(clickY<=step.getY()+step.getHeight())){
    			JOptionPane.showMessageDialog(this,"Step");
    		}
		}
		//check if validate tool was selected
		if((clickX>=validate.getX())&&(clickX<=validate.getX()+validate.getWidth())){
    		if((clickY>=validate.getY())&&(clickY<=validate.getY()+validate.getHeight())){
    			JOptionPane.showMessageDialog(this,"Validate");
    		}
		}
		//check if undo tool was selected
		int width = undo.getWidth();
		int height = helpToolsSelection1.getHeight()+(colourSolverTool.getHeight()-undo.getHeight());	// *get height of top half JPanel and add the height difference between the colourSOlver JPanel and the undo JLabel
		//detect whcih half of the JLabel was clicked 
		//(undo)
		if((clickX>=undo.getX())&&(clickX<=undo.getX()+(undo.getWidth()/2)+5)){							
    		if((clickY>=undo.getY()+height)&&(clickY<=undo.getY()+undo.getHeight()+height))					//*-and add these onto the y co-ordinates
    				undoRedo("undo");
		}
		//(redo)
		else if((clickX>=undo.getX())&&(clickX<=undo.getX()+undo.getWidth())){
			if((clickY>=undo.getY()+height)&&(clickY<=undo.getY()+undo.getHeight()+height))
    				undoRedo("redo");
		}
     }

	/*
	 * This method sets whether or not candidates should be shown
	 */
    private void markUp(){
    	if(showCandidates==true)
    		showCandidates=false;
    	else
    		showCandidates=true;
    	try{
    		//try repainting the SudokuGrid
    		sudokuGrid.repaint();
    	}
    	catch(NullPointerException npe){
    		//do nothing
    	}
    }
    
    /*
     * This method receives a puzzle and a cell and returns the
     * potential candidates for the received cell within the puzzle
     * 
     */
    public boolean[] getCandidates(int[] currentState, int cell){
    	boolean[] candidates = new boolean[9];
    	//this method is called every time the the sudoku grid is redrawn so the puzzle variable is always up to date
    	puzzle = currentState;		
    	//if candidates are to be shown then work them out
    	if(showCandidates&&puzzle[cell-1]==0){
    		//set all candidates as possible
    		Arrays.fill(candidates,true);
    		for(int i=0;i<puzzle.length;i++){
    			//check if cell is a buddy. If it is, then check it's value and remove that value from candidate collection
    			//check if row shared
    			if((cell-1)/9==(i/9)){
    				if(puzzle[i]!=0)
    					candidates[puzzle[i]-1]=false;
    			}
    			//check column
    			else if((cell-1)%9==(i%9)){
    				if(puzzle[i]!=0)
    					candidates[puzzle[i]-1]=false;
    			}
    			//check subgrid
    			else if((((cell-1)/9)/3==((i/9)/3))&&(((cell-1)%9)/3==((i%9)/3))){
    				if(puzzle[i]!=0)
    					candidates[puzzle[i]-1]=false;
    			}
    		}
    	}
    	return candidates;
    }
    
    /*
     * This method uses human solving logic
     * to return hints
     */
    private void  hint(){
    	int cell;
    	String message="";
    	Color colour = Color.BLUE;
    	//turn show candidates on
    	showCandidates=true;
    	//check for naked singles
    	cell=nakedSingles();
    	System.out.println(cell);
    	if(cell!=-1){
			message="There is a naked single in row "+Integer.toString(getRow(cell))+" column "+Integer.toString(getColumn(cell));
    	}
    	else{
    		//check for hidden singles
    		cell=hiddenSingles();
    		if(cell!=-1){
				//not yet assigned
    		}
    		else{
    			message="Sorry, we could not find a hint for you!";
    		}
    	}
    	//turn show candidates off
    	showCandidates=false;
    	gui.outputMessage(message,colour);
    }
    
    /*
     * This method returns naked singles
     */
    private int nakedSingles(){
    	boolean[] candi;
    	int cell=-1;
    	int count;
    	for(int i=0;i<puzzle.length;i++){
    		count=0;
    		candi=getCandidates(puzzle,i+1);
    		for(boolean can:candi){
    			if(can==true)
    				count++;
    		}
    		if(count==1){
    			cell=i+1;
    			break;
    		}
    	}
    	return cell;
    }
    
    /*
     * This method returns hidden singles
     */
    private int hiddenSingles(){
    	int b = 1;
    	return b;
    }
    
    /*
	 * This method returns the given cells
	 * row number
	 */
	public int getRow(int i){
		int row;
		if(i%9!=0){
			row=(i/9)+1;
		}
		else{
			row=(i/9);
		}
		return row;
	}
	
	/*
	 * This method returns the given cells
	 * column number
	 */
	public int getColumn(int i){
		int column;
		if(i%9!=0){
			column=i%9;
		}
		else{
			column=(i%9)+9;
		}
		return column;
	}
    
    public void step(){
    	
    }
    
    public void validate(){
    	
    }
    
    public void colourHelp(Color colour){
    	if(colourRadioButton.isSelected()){
    		JOptionPane.showMessageDialog(this,"Cell colour is selected and the colour is "+colour);
    	}
    	else if(ultraColourRadioButton.isSelected()){
    		JOptionPane.showMessageDialog(this,"Candidate colour is selected and the colour is "+colour);
    	}
    	
    }
    
    /*
     * This method sets the sudokuGrid to the requested puzzle state
     */
    public void undoRedo(String input){
    	try{
    		if(undoStack!=null&&input.equals("undo")){
    			sudokuGrid.setCurrentState(undoStack.pop(),input);
    		}
    		else if(redoStack!=null&&input.equals("redo")){
    			sudokuGrid.setCurrentState(redoStack.pop(),input);
    			System.out.println(redoStack.size());
    		}
    	}
    	catch(EmptyStackException ese){
    		//do nothing
    		System.out.println("undoRedo: "+ese);
    	}
    }
    
    /*
     * This method adds a puzzle onto the undo stack
     */
    public void addToUndo(int[] puzzle){
    	if(undoStack==null)
    		undoStack = new Stack<int[]>();
    	undoStack.push(puzzle);
    }
    
    /*
     * This method adds a puzzle onto the redo stack
     */
    public void addToRedo(int[] puzzle){
    	if(redoStack==null)
    		redoStack = new Stack<int[]>();
    	redoStack.push(puzzle);
    }
    
    /*
     * This method clears the redo stack
     */
    public void clearRedoStack(){
    	if(redoStack!=null)
    		redoStack.clear();
    }
    
}
//end of HelpTools class