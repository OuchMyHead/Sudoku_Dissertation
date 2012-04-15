/**
 * @(#)Puzzle.java
 *
 * This class represents a Sudoku puzzle
 *
 * The puzzle receives a String which is then tested to
 * see if it is a puzzle or a difficulty rating
 *
 * The puzzle is saved as int[] so original puzzle can
 * still be accessed if necessary
 *
 * The puzzle solution is saved as an int[]
 *
 * current/savedState are multidimensional boolean arrays where the
 * first value is whether the value is set, and the other 9 are whether
 * the number at that position is possible (ie [cell][1] represents whether
 * the number one is possible). These mutlidimensional arrays are used by
 * the solver
 * 
 *
 * @ James Mathieson 
 * @ version 1.00 24/03/2010
 */

import java.util.*;

public class SudokuPuzzle {
	
	private int[] puzzle,solution;;
	private boolean puzzleValid,checkingMultipleSolutions,solutionUnique,stop;
	private boolean[][] currentState;
	private SudokuSolver sudokuSolver;
	private Stack<boolean[][]> stateStack;
	private Stack<int[]> cellStack;
	private int solCandi,solCell,randomNumber;
	private double difficultyRating;
	private ArrayList<Integer> candidateList, cellList;
	private Random random;
	private String chosenDifficulty;

	/*
	 * Constructor - receives the user input and
	 * calls methods depending on the String input
	 * by user
	 */
    public SudokuPuzzle(String userInput,int puzzleLength) {
    	stop=false;
		if(userInput.equals("Easy")||userInput.equals("Medium")||userInput.equals("Hard")){
			puzzle=new int[puzzleLength];
			stateStack = new Stack<boolean[][]>();
			chosenDifficulty=userInput;
    	}
    	else{
    		//validate puzzle
    		puzzleValid = validateUserPuzzle(userInput,puzzleLength);
    		//if it is valid then solve
    		if(puzzleValid){
    			stateStack = new Stack<boolean[][]>();
    			initiateCurrentState(puzzleLength);
    		}
    	}
    }
    
    /*
     * This method validates the puzzle input by the user
     * and returns a boolean value representing whether or
     * not the puzzle is valid
     *
     * This method also sets the puzzle[] variable whilst the
     * puzzle is valid
     *
     */
    private boolean validateUserPuzzle(String userInput,int puzzleLength){
    	//puzzle is not valid by default
    	boolean valid=false;
    	userInput=userInput.trim();							//remove any accidental leading/trailing white space
    	userInput=userInput.replaceAll(" ","");
    	//validate puzzle and assign to puzzle array
    	if(userInput.length()==puzzleLength){
    		puzzle = new int[userInput.length()];
    		userInput = userInput.replaceAll("\\.","0");						//some puzzles use full stops instead of zero's so replace those with a 0
    		try{
				for(int i=0;i<userInput.length();i++){													// for all 81 cells
					puzzle[i] = Integer.parseInt(Character.toString(userInput.charAt(i)));				// convert String to integer
					valid=true;
    			}
			}
			catch(NumberFormatException nFE){
				valid=false;
			}
    	}
    	return valid;
    }
    
    /*
     * This method calls the appropriate methods to generate a new Sudoku
     * puzzle of the players chosen difficulty
     */
    public void createNewSudoku(){
		generateSolution(puzzle.length);
		generatePuzzle(chosenDifficulty);
    }
    
    /*
     * This method initiates the currentState to reflect the
     * original state of the puzzle
     */
    private void initiateCurrentState(int puzzleLength){
    	currentState = new boolean[puzzleLength][10];
    	for(int c=0;c<puzzle.length;c++){
    		if(puzzle[c]!=0){
  				currentState[c][puzzle[c]] = true;	//set the given number to true, the rest are false - do not confirm as set yet, this is done by 1-reduction
  			}
  			else{
  				for(int i=1;i<10;i++){				// set all values as possible - except for first as this represents whether the cell has a set number (which in this case, it does not)
  					currentState[c][i]=true;		// all numbers are a possibility for now - this is reduced by 1-reduction
  				}
  			}
    	}
    }
	
    /*
     * This method creates a new SudokuSolver Object
     * and calls the solve method on this puzzle and
     * returns a boolean value whether the puzzle could
     * be solved
     */
	public boolean solvePuzzle(){
		int state;
		sudokuSolver = new SudokuSolver(puzzle.length,this);
		state = sudokuSolver.solve();
		if(state==0){
			applySolution();
			puzzleValid=true;
			return true;
		}
		else{
			puzzleValid=false;
			return false;
		}
    }
     
	/*
	 * This method confirms the received cell
	 * as confirmed and removes the final value
	 * of said cell from buddy cells
	 */
	public void confirmCell(int cell, int value){
		//loop through cell, find out which is the final value
		for(int candidate=1;candidate<10;candidate++){
			if(candidate==value){continue;}
			currentState[cell][candidate]=false;
		}
		//remove this candidate from the buddy cells
		for(int i=0;i<puzzle.length;i++){
			if(i==cell){continue;}
			if(getRow(i+1)==getRow(cell+1)){currentState[i][value]=false;}
			if(getColumn(i+1)==getColumn(cell+1)){currentState[i][value]=false;}
			if(getSubGrid(i+1)==getSubGrid(cell+1)){currentState[i][value]=false;}
		}
		//set cell as confirmed
		currentState[cell][0]=true;
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
	
	/*
	 * This method returns the given cells
	 * subGrid number
	 */
	public int getSubGrid(int i){
		int subgrid=1, col=1, row =1;						// inititiate column, row and subgrid numbers
		for(int c=0;c<puzzle.length;c++){					// for all 81 cells	
			if(c==i-1){break;}								// if c=i then end search and return subgrid value
			if(col==9){										// Check whether count has reached 9th column
				if(row%3!=0){subgrid-=3;}					// subtract three from subgrid if row is not divisible by 3
				col=0;row++;								// increment row and reset column
			}
			if(col%3==0){subgrid++;}						// increment subgrid if col is divisble by 3
			col++;											// incremement column number
		}
		return subgrid;
	}
	
	/*
	 * Once solved, this method is called to record
	 * the solution in the solution[] variable. This
	 * solution is derived from the current state
	 */
	 private void applySolution(){
	 	solution = new int[puzzle.length];
	 	for(int i=0;i<solution.length;i++){
	 		for(int c=1;c<10;c++){
	 			if(currentState[i][c]==true){			//the current state will have only one true value here when solved 
					solution[i]=c;						//so record this
	 			}
	 		}
	 	}
	 }
	 
	/*
	 * This method recieves an int representing a cell
	 * and returns the numerical values for the potential 
	 * candidates of that cell
	 */
	public int[] getCandidates(int i){
		int[] candidates;
		int pos;
		candidateList = new ArrayList<Integer>();
		//add candidates to arrayList
		for(Integer c=1;c<10;c++){
			if(currentState[i][c]==true)
				candidateList.add(c);
		}
		//if checking for multiple solutions then remove the candidate
		//that are used in the current solution
		if(checkingMultipleSolutions&&i==solCell){
			pos=candidateList.indexOf(solCandi);
			if(pos!=-1)								//check candidate has been found
				candidateList.remove(pos);
		}
		//create new int[] to return candidates
		candidates=new int[candidateList.size()];
		//pass arrayList content to int[]
		for(int c=0;c<candidates.length;c++){
			candidates[c]=candidateList.get(c);
		}
		return candidates;
	}
	
	/* this method calls the uniqueSolution method within
     * the solver and returns a boolean value representing
     * whether the puzzle has a unique solution or not
     */
     public boolean uniqueSolution(){
     	sudokuSolver = new SudokuSolver(puzzle.length,this);
     	boolean unique=true;
     	checkingMultipleSolutions=true;
     	try{
     		for(int i=0;i<solution.length;i++){
     			initiateCurrentState(solution.length);
     			solCell=i;
     			solCandi=solution[i];
     			if(sudokuSolver.solve()==0){
     				unique=false;
     				break;
     			}
     			//allows the stop the uniqueSolution if necessary
     			else if(stop==true){
     				break;
     			}
     		}
     	}
     	catch(NullPointerException npe){
     		//if a null pointer exception has been thrown then it must be because the puzzle has not been solved - so solve puzzle and call again
     		solvePuzzle();
     		uniqueSolution();
     	}
     	checkingMultipleSolutions=false;
     	//set whether this puzzle is unique or not
     	solutionUnique=unique;
     	return unique;
     }
	
	/*
	 * This method receives an int representing a cell
	 * and returns whether the cell has a confirmed value
	 */
	public boolean isConfirmed(int i){
		return currentState[i][0];
	}
	
	/*
	 * This method adds a deep copy of the current state
	 * to the stateStack (so it can be restored if necessary)
	 * - a deep copy is added so it is not changed by the solver
	 */
	public void push(){
		boolean[][] copy = deepCopy(currentState);
		stateStack.push(copy);
	}
	
	/*
	 * This method retrieves the previous state and assigns this
	 * ass the current state
	 */
	public int pop() throws EmptyStackException{			//catch error in case stack is empty
		currentState=stateStack.pop();
		return stateStack.size();
	}
	
	/*
	 * This method recieves a multidimensional array representing the
	 * puzzles current state and returns a deep copy of this array
	 */
	private static boolean[][] deepCopy(boolean[][] original){
		boolean[][] copy = new boolean[original.length][];			//create new multidimensional array the same length as original
		for(int i=0;i<original.length;i++){							
			copy[i] = new boolean[original[i].length];				//create boolean array the same length as second dimension for each cell in first dimension of array	
			for(int c=0;c<original[i].length;c++){
				copy[i][c] = original[i][c];						//copy values to new array
			}
		}
		return copy;
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
	
	/*
	 * This method generates a random sudoku solution
	 */
    private void generateSolution(int puzzleLength){
    	initiateCurrentState(puzzle.length);
    	int[] candidates;
    	int pos;
    	boolean success=true;
    	//seed random number generator with timestamp
    	random = new Random(new Date().getTime());
    	sudokuSolver = new SudokuSolver(puzzleLength,this);
    	solution = new int[puzzleLength];
    	cellList = new ArrayList<Integer>();
    	candidateList = new ArrayList<Integer>();
    	//add the total number of cells the the arrayList
    	for(int i=0;i<puzzleLength;i++){
    		cellList.add(i);
    	}
    	int count =0;
    	//while there are still cells to add
    	while(cellList.size()!=0){
    		count++;
    		//a count here in case this goes wrong
    		if(count==200){
    			success=false;
    			break;
    		}
    		//save previous state
    		push();
    		//select random cell
    		randomNumber = random.nextInt(cellList.size());
    		solCell=cellList.get(randomNumber);
    		//get candidates for cell and add to cellList
    		if(!isConfirmed(solCell)){
    			candidates = getCandidates(solCell);
    			for(int candi:candidates){
    				candidateList.add(candi);
    			}
    			//select random candidate
    			randomNumber = random.nextInt(candidateList.size());
    			solCandi=candidateList.get(randomNumber);
    			//confirm cell and reduce
    			confirmCell(solCell,solCandi);
    			sudokuSolver.reduction();
    		}
    		pos=cellList.indexOf(solCell);
    		//check puzzle has not become impossible - if it hasn't then the cell can be removed
			if(sudokuSolver.impossible())
				pop();
			else if(pos!=-1)								//check cell has been found
				cellList.remove(pos);
    	}
    	//apply as solution
    	if(success)
    		applySolution();
    	else
    		generateSolution(puzzleLength);
    }
    
    /*
     * This method creates a random sudoku puzzle
     *
     * This method takes the sudoku solution and removes
     * a cell value randomly and checks the puzzle still
     * has a unique solution. This is done till the puzzle
     * is of the requested difficulty.
     */
    private void generatePuzzle(String difficulty){
    	puzzle=getSolution();
    	cellList = new ArrayList<Integer>();
    	sudokuSolver=new SudokuSolver(puzzle.length,this);
    	//seed random number generator with timestamp
    	random = new Random(new Date().getTime());
 		int randomNumber;
		//create stack for holding cells that have not been set as 0
    	cellStack = new Stack<int[]>();
    	//add the total number of cells the the arrayList
    	for(int i=0;i<puzzle.length;i++){
    		cellList.add(i);
    	}
    	int count=0;
    	//do this while difficulty is not as requested
    	while(getDifficulty()!=difficulty){
			System.out.println(getDifficulty());
    		if(stop==true)
    			break;
    		//copy the puzzle before removal
    		cellStack.push(deepCopy(puzzle));
    		//get random number within the range of the cellList size
    		randomNumber = random.nextInt(cellList.size());
    		//get the value of the cell you want to set as zero
    		solCell=cellList.get(randomNumber);
    		//set as zero
    		puzzle[solCell]=0;
    		//check puzzle is still unique
    		if(!uniqueSolution())
    			puzzle=cellStack.pop();				//if it isn't then restore puzzle to previous state then remove cell from cellList,
    		cellList.remove(randomNumber);		//else, remove cell from cellList
    		//if the cellList becomes zero then it is not possible to make a puzzle of the chosen difficulty with the cells removed so try again
    		if(cellList.size()==0){
    			generatePuzzle(difficulty);
				break;
    		}
    	}
    }
    
    /*
     * This method returns the difficulty of the
     * puzzle - based on Chen's Sudoku Entropy
     * (Chen,2009,P.4)-Please see accompanying documentation
     * for reference list
     */
    public String getDifficulty(){
    	String difficulty;
    	double entropy=0;
    	double cand=0;
    	//initiatate the current state for the puzzle
    	initiateCurrentState(puzzle.length);
    	//remove naked singles
    	sudokuSolver=new SudokuSolver(puzzle.length,this);
    	sudokuSolver.nakedReduction();
    	int count=0;
    	//calculate the average number of candidates per unset cell
    	for(int i=0;i<puzzle.length;i++){
    		cand+=getCandidates(i).length;
    		if(getCandidates(i).length!=1)
    			count++;
    	}
    	cand-=count;
    	//get the average number of candidates
    	cand=cand/(81-count);
    	//calculate the entropy
    	entropy=Math.log10(1d/81d)*Math.log10(1d/cand);
    	//now we have the entropy, work out what category the entropy falls into
    	if(entropy>=0.5&&entropy<=1.3)
    		difficulty="Easy";
    	else if(entropy>1.3&&entropy<=1.7)
    		difficulty="Medium";
    	else if(entropy>1.7)
    		difficulty="Hard";
    	else
    		difficulty="Easier than easy";
    	return difficulty;
    }
     
	/*
	 * This method returns whether or not the
     * puzzle is valid
     */
    public boolean isValid(){
    	return puzzleValid;
    }
    
    /*
     * This is method returns a deep copy of the original
     * puzzle
     */
    public int[] getPuzzle(){
    	return deepCopy(puzzle);
    }
    
    /*
     * This method returns a deap copy of the solution
     */
    public int[] getSolution(){
    	return deepCopy(solution);
    }
    
    /*
     * This method is called to stop the object checking for multiple
     * solutions
     */
    public void stopRunning(){
    	stop=true;
    }

}//end of SudokuPuzzle class