/**
 * @(#)GUI.java
 *
 * Graphical User Interface
 *
 *
 * @James Mathieson 
 * @version 1.00 15/03/2010
 */
 
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BorderFactory; 
import java.awt.*;
import java.awt.event.*;


import javax.swing.SwingWorker;

public class GUI extends JFrame implements ActionListener{
	
	private JMenuBar menuBar = new JMenuBar();
	private JLabel difficultyRating;
	private String timeCount = "00:00";
	private static SudokuGrid sudokuGrid;
	private SudokuPuzzle sudokuPuzzle;
	private JTextArea outputScreen;
	private int puzzleLength=81;
	private HelpTools helpTools;
	
	/*
     * Constructor
     * creates GUI and menu options
     */
    public GUI() {
    	//set details for JFrame
		setTitle("Sudoku Generator and Helper");
    	setSize(715,590);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	setResizable(false);
    	
    	//get menu bar contents
    	getFileMenu();
    	getEditMenu();
    	getHelpMenu();
    	
    	//create a smaller JPanel so content does no reach edges of JFrame
    	JPanel content = new JPanel();
    	setPreferredSize(new Dimension(695,570));
    	
		// add menu bar and content to JFrame
    	setJMenuBar(menuBar);
    	content.add(getContent());
    	
    	//add content to JFrame
        setContentPane(content);
        
        //create popUp menu - this is added after the content so the Sudoku Grid has been drawn
    	createSudokuPopUpMenu();

    	//set visible last to ensure everything is added
    	setVisible(true);
    }
    
    /*
     * This method returns the file menu
     */
    private void getFileMenu(){
    	//create file menu and add items, including any key shortcuts
    	JMenu file = new JMenu("File");
    	file.setMnemonic(KeyEvent.VK_F);
    	menuBar.add(file);
    	JMenu newPuzzle = new JMenu("New Puzzle");
    	file.add(newPuzzle);
    		//add sub menu for chosing puzzle difficulty
    		JMenuItem newPuzzleEasy = new JMenuItem("Easy");
    		newPuzzleEasy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
    		newPuzzleEasy.addActionListener(this);
    		newPuzzle.add(newPuzzleEasy);
    		JMenuItem newPuzzleMedium = new JMenuItem("Medium");
    		newPuzzleMedium.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
    		newPuzzleMedium.addActionListener(this);
    		newPuzzle.add(newPuzzleMedium);
    		JMenuItem newPuzzleHard = new JMenuItem("Hard");
    		newPuzzleHard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
    		newPuzzleHard.addActionListener(this);
    		newPuzzle.add(newPuzzleHard);
    		newPuzzle.addSeparator();
    		JMenuItem newPuzzleCustom = new JMenuItem("Create My Own");
    		newPuzzleCustom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
    		newPuzzleCustom.addActionListener(this);
    		newPuzzle.add(newPuzzleCustom);
    	file.addSeparator();
    	JMenuItem exit = new JMenuItem("Exit");
    	exit.addActionListener(this);
    	file.add(exit);
    }
    
    /*
     * This method provides the options menu
     */
    private void getEditMenu(){
    	JMenu edit= new JMenu("Edit");
    	edit.setMnemonic(KeyEvent.VK_E);
    	menuBar.add(edit);
    	JMenuItem undo = new JMenuItem("undo");
    	undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    	undo.addActionListener(this);
    	edit.add(undo);
    	JMenuItem redo = new JMenuItem("redo");
    	redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    	redo.addActionListener(this);
    	edit.add(redo);
    	edit.addSeparator();
    	JMenuItem clearGrid = new JMenuItem("Clear Grid");
    	clearGrid.addActionListener(this);
    	edit.add(clearGrid);
    	JMenuItem clearOutput = new JMenuItem("Clear Help Box");
    	clearOutput.addActionListener(this);
    	edit.add(clearOutput);
    	JMenuItem solvePuzzle = new JMenuItem("Solve Puzzle");
    	solvePuzzle.addActionListener(this);
    	edit.add(solvePuzzle);
    	edit.addSeparator();
    	JMenuItem restart = new JMenuItem("Restart Puzzle");
    	restart.addActionListener(this);
    	edit.add(restart);
    }
    
    /*
     * This method reutrns the help menu
     */
    private void getHelpMenu(){
    	JMenu help = new JMenu("Help");
    	help.setMnemonic(KeyEvent.VK_H);
    	menuBar.add(help);
    	JMenuItem howToPlay = new JMenuItem("How do I use Sudoku Generator & Helper?");
    	howToPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.SHIFT_MASK));
    	howToPlay.addActionListener(this);
    	help.add(howToPlay);
    	help.addSeparator();
    	JMenuItem about = new JMenuItem("About Sudoku Generator & Helper");
    	about.addActionListener(this);
    	help.add(about);
    }
    
    public void createSudokuPopUpMenu() {
        //Create the popup menu.
        JPopupMenu popUpMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("1");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("2");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("3");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("4");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("5");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("6");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("7");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("8");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("9");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        menuItem = new JMenuItem("Clear");
        menuItem.addActionListener(this);
        popUpMenu.add(menuItem);
        //Add listener to the Sudoku Grid
        MouseListener popupListener = new PopupListener(popUpMenu);
        sudokuGrid.addMouseListener(popupListener);
    }
    
    /*
     * This method returns a JPanel holding the content for the GUI
     */
    private JPanel getContent(){
    	//Create new JPanel and set layout
    	JPanel content = new JPanel();
    	content.setLayout(new BorderLayout());
    	//create a box to add space between the top half and the bottom half of the GUI
    	Dimension minSize = new Dimension(0, 17);
		Dimension prefSize = new Dimension(0, 17);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
		//add content to JPanel
        content.add(getTopHalf(),BorderLayout.NORTH);
        content.add(new Box.Filler(minSize, prefSize, maxSize),BorderLayout.CENTER);
        content.add(getBottomHalf(),BorderLayout.SOUTH);
    	return 	content;
    }
    
    /*
     * This method returns a JPanel containing the top
     * helf of the GUI
     */
    private JPanel getTopHalf() {
    	//create new JPanel and set layout
        JPanel topHalf = new JPanel();
        topHalf.setLayout(new BorderLayout());
        //create a box to add padding - move away from the top of the JPanel
    	Dimension minSize = new Dimension(0, 8);
		Dimension prefSize = new Dimension(0, 8);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
		//add content to JPanel by calling appropriate methods
    	topHalf.add(new Box.Filler(minSize, prefSize, maxSize), BorderLayout.NORTH);
    	//adding the puzzle aids first so the HelpTools object is created and the reference can be sent to the SudokuGrid object
    	topHalf.add(getPuzzleAids(),BorderLayout.EAST);
        topHalf.add(getSudokuGrid(),BorderLayout.CENTER);
        return topHalf;
    }
    
    /*
     * This method returns a JPanel containing the Sudoku
     * grid
     */
    private JPanel getSudokuGrid(){
    	//create new JPanel and pass the
    	JPanel grid = new JPanel();
    	//create new SudokuGrid object and pass reference to the helptools
    	sudokuGrid = new SudokuGrid(helpTools);
    	grid=sudokuGrid;
    	//pass the location of the sudokuGrid to the HelpTools object
    	helpTools.setSudokuGridRef(sudokuGrid);
    	return grid;
    }
    
    /*
     * This method returns a JPanel containg the top right
     * side of the GUI
     */
    private JPanel getPuzzleAids(){
    	//create new JPanel and set layout
    	JPanel aids = new JPanel();
    	aids.setLayout(new BorderLayout());
    	//call methods to get JPanels for the respective parts of the GUI and add to JPanel
    	aids.add(getNumbers(),BorderLayout.NORTH);
    	aids.add(getHelpToolOptions(),BorderLayout.CENTER);
    	aids.add(getGameDetails(),BorderLayout.SOUTH);
    	return aids;
    }
    
    /*
     * This method returns a JPanel that displays numbers
     * the user can choose to enter into the Sudoku puzzle
     */
    private JPanel getNumbers(){
    	//create new JPanel
    	JPanel numbers = new JPanel();
    	//create new NumberedGrid object and pass the reference to the new JPanel
    	NumberedGrid ng = new NumberedGrid(this);
    	numbers=ng;
    	return numbers;
    }
    
    /*
     * This method creates and returns the visual representation
     * of the solver help tools
     */
    private JPanel getHelpToolOptions(){
    	//create main JPanel, set layout and add border
    	JPanel helpToolOptions = new JPanel();
		helpTools = new HelpTools(this);
		helpToolOptions = helpTools;
    	return helpToolOptions;
    }
    
    /*
     * This method returns a JPanel that holds the game details
     */
    private JPanel getGameDetails(){
    	//create JPanel, set layout and set border
    	JPanel gameDetails = new JPanel();
    	gameDetails.setLayout(new BoxLayout(gameDetails, BoxLayout.PAGE_AXIS));
    	gameDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray),"Puzzle Details"));
    	//create new JLables, set Fonts and align to the right of the JPanel
    	difficultyRating = new JLabel("Difficulty: unset");
    	difficultyRating.setFont(difficultyRating.getFont().deriveFont(12.0f));
    	difficultyRating.setFont(difficultyRating.getFont().deriveFont(Font.PLAIN));
    	difficultyRating.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	JLabel timer = new JLabel(timeCount);
    	timer.setFont(timer.getFont().deriveFont(20.0f));
    	timer.setFont(timer.getFont().deriveFont(Font.PLAIN));
    	timer.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	//add game details to the JPanel
    	gameDetails.add(difficultyRating);
    	//create and add box for padding (brings the Jlabel away from the edge of the JPanel)
    	Dimension minSize = new Dimension(3, 0);
		Dimension prefSize = new Dimension(3, 0);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 3);
		gameDetails.add(new Box.Filler(minSize, prefSize, maxSize));
		//add timer JLabel
    	gameDetails.add(timer);
    	return gameDetails;
    }
    
    /*
     * This method creates and returns the JPanel that
     * holds the lower half of the GUI
     */
    private JPanel getBottomHalf(){
    	//create JPanel and set layout
    	JPanel bottomHalf = new JPanel();
    	bottomHalf.setLayout(new BorderLayout());
    	//create a text area for displaying solver tool messages to user
    	outputScreen = new JTextArea("",8,60);
    	//add a border to help it stand out, also set uneditable and wrap text (no need for horizontal scroll bars then)
    	outputScreen.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY),new EmptyBorder(5,5,0,5)));
    	outputScreen.setEditable(false);
        outputScreen.setLineWrap(true);
        outputScreen.setWrapStyleWord(true);
        //add text area to JPanel
    	bottomHalf.add(outputScreen, BorderLayout.CENTER);
    	return bottomHalf;
    }
    
    /*
     * This method delas with any actions performed;
     * within this class it will be choices from the
     * menu bar
     */
    public void actionPerformed(ActionEvent e){
	   	try{
	        JMenuItem source = (JMenuItem)(e.getSource());
	        //deals with main menu items
	        String input = source.getText();
	        //generate random puzzle of requested difficulty
	        if((input.equals("Easy"))||(input.equals("Medium"))||(input.equals("Hard"))){
	        	int confirm=0;
	        	if(sudokuPuzzle!=null){
	        		Toolkit.getDefaultToolkit().beep();
	        		confirm=JOptionPane.showConfirmDialog(null, "We have detected a game has already beeen commenced.\nAre you sure you want to start a new one?","",JOptionPane.YES_NO_OPTION);
	        	}
	        	if(confirm==0){
	        		if(sudokuPuzzle!=null){
	        			sudokuPuzzle.stopRunning();
	        			sudokuPuzzle=null;
	        			outputScreen.setText("");
	        		}
        			sudokuPuzzle = new SudokuPuzzle(input,puzzleLength);
	        		(new generateNewPuzzle()).execute();
	        	}
	        }
	        //allow user to input their own puzzle
	        else if(input.equals("Create My Own")){
	        	int confirm=0;
	        	if(sudokuPuzzle!=null){
	        		Toolkit.getDefaultToolkit().beep();
	        		confirm=JOptionPane.showConfirmDialog(null, "We have detected a game has already beeen commenced.\nAre you sure you want to start a new one?","",JOptionPane.YES_NO_OPTION);
	        	}
	        	if(confirm==0){
	        		if(sudokuPuzzle!=null){
	        			sudokuPuzzle.stopRunning();
	        			sudokuPuzzle=null;
	        			outputScreen.setText("");
	        		}
	    			Object[] possibilities = null;				//create empty input box in JOptionPane
					String s = (String)JOptionPane.showInputDialog(this,"Please enter your Sudoku puzzle below","Create My Own",JOptionPane.PLAIN_MESSAGE,null,possibilities,"");
	        		if(s!=null)
	        			newPuzzle(s);
	        	}
	        }
			//undo/redo
			else if(input.equals("undo")||input.equals("redo")){
				helpTools.undoRedo(input);
			}
	        //close application
	        else if(input.equals("Exit")){
	        	Toolkit.getDefaultToolkit().beep();
	        	int cl=JOptionPane.showConfirmDialog(null, "Are you sure you wish to exit?","",JOptionPane.YES_NO_OPTION);
	        	if(cl==0){System.exit(0);}
	        }
	        //remove user inputs from Sudoku Grid
	        else if(input.equals("Clear Grid")){
	        	JOptionPane.showMessageDialog(this,input);
	        }
	        //remove help tips from help box
	        else if(input.equals("Clear Help Box")){
	        	JOptionPane.showMessageDialog(this,input);
	        }
	        //remove all user inputs and reset timer
	        else if(input.equals("Restart Puzzle")){
	        	JOptionPane.showMessageDialog(this,input);
	        }
	        //remove all user inputs and reset timer
	        else if(input.equals("Solve Puzzle")){
	        	JOptionPane.showMessageDialog(this,input);
	        }
	        //help on how to use the Sudoku and Generator Helper program
	        else if(input.equals("How do I use Sudoku Generator & Helper?")){
	        	JOptionPane.showMessageDialog(this,input);
	        }
	        //details about the Sudoku Generator and Helper
	        else if(input.equals("About Sudoku Generator & Helper")){
	        	String details = "Sudoku Generator and Helper \n Version 1.0 \n James Mathieson \n 07020996 \n Northumbria University";
	        	JOptionPane.showMessageDialog(this,details);
	        }
	        //deal with pop up menu items
	        //clear cell of user input value
	        else if(input.equals("Clear")){
	        	this.addToPuzzle(0);
	        }
	        //check whether action was numerical input - if it was then add to puzzle
			else{
				try{
					int value = Integer.parseInt(input);
					this.addToPuzzle(value);
				}
				catch(NumberFormatException nFE){
					//do nothing
				}
			}
	    }
	    catch(Exception ex){
	    	System.out.println("actionPerformed "+ex);
	    }
    }
     
	/*
	 * This method passes the user input value to the SudokuGrid
	 * to be drawn
	 * The move is also sent to the helpTools so it can be undone
	 * if requested by the player
	 */
    public void addToPuzzle(int i){
    	boolean complete;
    	int value = i;
    	complete = sudokuGrid.addToCurrentState(i);
    	if(complete){
    		if(sudokuGrid.foundSolution())
    			outputScreen.setText("Puzzle Complete");
    		else
    			outputScreen.setText("Not quite");
    	}		
     }
     
     /*
      * This method adds text of a certain
      * colour to the outputScreen
      * Any text that was previously in there will be lost
      */
     public void outputMessage(String message, Color colour){
     	outputScreen.setForeground(colour);
        outputScreen.setText(message);
     }
     
     private void newPuzzle(String s){
     	//show loading cursor whlst waiting for Sudoku puzzle to be validated
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //create a new Sudoku puzzle and pass the input String
        sudokuPuzzle = new SudokuPuzzle(s,puzzleLength);
        if(sudokuPuzzle.isValid()){
        	sudokuPuzzle.solvePuzzle();
        	//check Sudoku puzzle is valid
        	if(sudokuPuzzle.isValid()){
        		outputScreen.setEditable(true);
        		outputScreen.setForeground(new Color(0,119,0));
        		outputScreen.setText("Checking for multiple solutions...");
        		//send sudokupuzzle reference to the sudokuGrid
        		sudokuGrid.setPuzzle(sudokuPuzzle);
        		difficultyRating.setText("Difficulty: "+sudokuPuzzle.getDifficulty());
        		(new checkUnique()).execute();
        		outputScreen.setEditable(false);
        	}
        	else{
        		outputScreen.setForeground(new Color(51,51,51));
        		outputScreen.setText("Want to starty playing Sudoku?\n\n");
        		outputScreen.append("You can generate a random Sudoku puzzle using the File->New Puzzle option at the top of this window.\n");
        		outputScreen.append("Just select how hard you like your puzzle and start playing!!");
        		Toolkit.getDefaultToolkit().beep();
        		JOptionPane.showMessageDialog(this,"Sorry, but this puzzle does not have a solution.\nWhy not try one of ours?");
        		sudokuPuzzle=null;
        	}
     	}
     	else{
     		outputScreen.setForeground(new Color(51,51,51));
     		outputScreen.setText("Want to starty playing Sudoku?\n\n");
        	outputScreen.append("You can generate a random Sudoku puzzle using the File->New Puzzle option at the top of this window.\n");
        	outputScreen.append("Just select how hard you like your puzzle and start playing!!");
     		Toolkit.getDefaultToolkit().beep();
        	JOptionPane.showMessageDialog(this,"Invalid Puzzle. \nRead more about valid Suodku Puzzles in our help section.");
        	sudokuPuzzle=null;
     	}
     	//set back to default cursor
     	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
     }
     
     /*
      * This thread class checks whether a Sudoku puzzle is unique. (it has been made a class so it can be run multiple times)
      * This has been done in a thread as it can take a while to check all solutions
      * on very difficult puzzles. Using this thread means players do not get a frozen
      * screen while the puzzle is being checked and players can start to complete the puzzle
      */
     class checkUnique extends SwingWorker {
		public String doInBackground() {
			try {
				if(sudokuPuzzle.uniqueSolution()){
					outputScreen.setEditable(true);
					outputScreen.setForeground(Color.BLUE);
					outputScreen.setText("This puzzle has a unique solution.\n");
					outputScreen.setEditable(false);
				}
				else{
					outputScreen.setEditable(true);
					outputScreen.setForeground(new Color(204,0,0));
					outputScreen.setText("Warning: This puzzle does not have a unique solution!\n");
					outputScreen.append("\nThis is not a valid Sudoku puzzle, so the Sudoku helper may not operate as expected as our solution may be different to yours.");
					outputScreen.append("For more information, please read our help section.");
					outputScreen.setEditable(false);
				}
			} 
			catch (Exception e) {
				System.out.println("Check Unique: "+e);
				//do nothing
			}
			return null;
		}
		public void done() {
			if(sudokuPuzzle==null){
				outputScreen.setText("");
			}
		}
	}
	
	/*
     * This thread class creates a new Sudoku puzzle.
     * It is currently time consuming so it is nrun in the
     * background to avoid a frozen screen.
     */
     class generateNewPuzzle extends SwingWorker {
		public String doInBackground() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				outputScreen.setForeground(new Color(0,119,0));
        		outputScreen.setText("We are currently generating your new sudoku puzzle.\n");
        		outputScreen.append("This could take up to a minute depending on the speed of your computer.\n");
				sudokuPuzzle.createNewSudoku();
				sudokuGrid.setPuzzle(sudokuPuzzle);
				outputScreen.setText("");
				difficultyRating.setText("Difficulty: "+sudokuPuzzle.getDifficulty());
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} 
			catch (Exception e) {
				System.out.println("New Puzzle: "+e);
				//do nothing
			}
			return null;
		}
		public void done() {
			if(sudokuPuzzle==null){
				outputScreen.setText("");
			}
		}
	}
     
	/*
     * Create a class that deals with the popup
     * options menu
     */
	class PopupListener extends MouseAdapter {
		JPopupMenu popUpMenu;
       
		PopupListener(JPopupMenu popupMenu) {
			popUpMenu = popupMenu;
       	}

		//once right mouse button has been pressed - show pop up
       	public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

        private void showPopup(MouseEvent e) {
        	//update location of mouse click on Sudoku grid
        	sudokuGrid.calculatePosition(e.getX(),e.getY());
			//show pop up in location of mouse click if a sudokuPuzzle has been created
            if (e.isPopupTrigger()&&sudokuPuzzle!=null) {
            	popUpMenu.show(e.getComponent(),e.getX(), e.getY());
            }
        }
	}//end of PopupListener class


}// end of GUI class