/**
 * @(#)Sudoku_Solver.java
 *
 * This class is used to recursively solve a 
 * Sudoku puzzle (brute force)
 * 
 * The puzzle is input as an int[]
 *
 * @ James Mathieson 
 * @ version 1.00 27/03/2010
 */

import java.util.*;

public class SudokuSolver {
	
	private int totalCells;
	private SudokuPuzzle puzzle;
  	private static final int INCONSIS = -1, SOLVED = 0;
	
	/*
	 * Constructor
	 * Receives reference to the Sudoku puzzle and the number
	 * of cells within the Sudoku puzzle
	 */
    public SudokuSolver(int cells, SudokuPuzzle ref) {
    	puzzle=ref;
    	totalCells=cells;
    }
    
    /*
     * This method returns whether a puzzle is impossible (has no solution)
     * The method is deemed such if any cells have no candidates
     * (an impossible state)
     */
    protected boolean impossible() {
    	boolean impossible=false;					//set as possible by default
    	for(int i=0;i<totalCells;i++){
    		int[] candidates = puzzle.getCandidates(i);	
    		if(candidates.length==0){					//if any candidates are of 0 length
    			impossible=true;						//then the puzzle is impossible
    			break;									//break out of loop
    		}
    	}
    	return impossible;
  	}
  	
  	/*
  	 * This method solves the Sudoku puzzle by trial and error
  	 * Individual candidates are tried for each cell until the
  	 * correct candidate for each cell is found
  	 */
  	public int solve(){
  		reduction();				    //confirm cells with singleton possibilities
  		if(impossible())
  			return INCONSIS;				//puzzle has a zero value somewhere so it is impossible to solve
  		//check if solved
  		boolean allConfirmed=true;
  		for(int i=0;i<totalCells;i++){
  			allConfirmed = allConfirmed && puzzle.isConfirmed(i);
  		}
  		if(allConfirmed)
  			return SOLVED;
  		
  		//find a cell that is not confirmed and has the minimal candidates
  		int cell=-1, p=10;
  		int[] candidate;
  		for(int i=0;i<totalCells;i++){
  			if(!puzzle.isConfirmed(i)){
  				candidate = puzzle.getCandidates(i);
  				if(candidate.length<p){
  					cell=i;
  					p=candidate.length;
  				}
  			}
  		}
  		/* 
  		 * try all the possibilities (except last as by process of elimintation, 
  		 * since if we have reached here then this must be the value).
  		 * 
  		 * Save state in stack on recursively call solve() until SOLVED is returned
  		 * If INCONSIS is returned then pop the stack (restore state) and try next
  		 * possibility
  		 * 
  		 */
  		int result=-2;
  		candidate = puzzle.getCandidates(cell);
  		for(int c=0;c<candidate.length-1;c++){
  			//save state
  			puzzle.push();
  			//apply candidate to cell
  			puzzle.confirmCell(cell,candidate[c]);
  			//check for naked/hidden singles
  			reduction();
  			//check is solved
  			result = solve();
  			if(result==INCONSIS){
  				try{
  					puzzle.pop();
  				}
  				catch(EmptyStackException est){
  					//do nothing
  				}
  			}
			else{
  				break;			//a consistent state has been found so drop out of for loop
  			}
  		}
  		if(result!=INCONSIS)
			return result;
  		//if we have reached here then we are at the last possible value for the candidates so confirm candidate in cell
  		puzzle.confirmCell(cell,candidate[candidate.length-1]);
		return solve(); //recursive call
  	}//end of solve method
  	
  	/*
     * This method implements 1-Reduction:
     * Removes a candidate from buddy cells if that 
     * cell must hold that candidate as it's value
     * (finds singleton possibilities)
     */
	public void reduction(){
		int count;
		int value;
		
     	boolean reducedNkd,reducedSgl;
     	do{
			reducedNkd=nakedReduction();							//find naked singles
     		reducedSgl=hiddenReduction();							//find hidden singles
		}while(reducedNkd||reducedSgl);								//repeat until no more cells have been reduced
	}//end of reduction function
	
	/*
	 * This method checks for naked singles
	 * and returns whether any were found
	 */
	public boolean nakedReduction(){
		int[] candidates;
		boolean reduced=false;
		for(int i=0;i<totalCells;i++){				//check cell is not set
			if(!puzzle.isConfirmed(i)){
     			candidates = puzzle.getCandidates(i);			//get the cells candidates
     			if(candidates.length==1){				//if the cell has only one candidate
     				puzzle.confirmCell(i,candidates[0]);		//then this candidate can be confirmed in the cell
     				reduced=true;						//and a reduction has taken place
     			}
     		}
		}
		return reduced;
	}
	
	/*
	 * This method checks for hidden singles
	 * and returns whether any were found
	 */
	public boolean hiddenReduction(){
		int[] candidates;
		boolean reduced=false;
		for(int cell=0;cell<totalCells;cell++){
     		if(!puzzle.isConfirmed(cell)){						//make sure cell has not already been set
     			candidates = puzzle.getCandidates(cell);						//get the cells candidates
     			for(int candi:candidates){
     				int rowCount=1,colCount=1,subCount=1;
     				for(int i=0;i<totalCells;i++){
     				int[] buddyCandi=puzzle.getCandidates(i);						//check all cells
						if(i==cell){continue;}										//ignore self
						if(puzzle.getRow(i+1)==puzzle.getRow(cell+1)){
							for(int budc:buddyCandi)
								if(budc==candi)										//if cell shares row and potential candidate
									rowCount++;
						}
						if(puzzle.getColumn(i+1)==puzzle.getColumn(cell+1)){
							for(int budc:buddyCandi)
								if(budc==candi)										//if cell shares row and potential candidate
									colCount++;
						}
						if(puzzle.getSubGrid(i+1)==puzzle.getSubGrid(cell+1)){
							for(int budc:buddyCandi)
								if(budc==candi)										//if cell shares row and potential candidate
									subCount++;
						}
     				}
     				if(rowCount==1||colCount==1||subCount==1){				//if there is a row/col/subgrid where the number only occurs once then
						puzzle.confirmCell(cell,candi);						//the candidate can be confirmed within the cell being checked
						reduced=true;										//and a reduction has taken palace
					}
     			}
			}
		}
		return reduced;
	}
    
}//end of Sudoku_Solver class