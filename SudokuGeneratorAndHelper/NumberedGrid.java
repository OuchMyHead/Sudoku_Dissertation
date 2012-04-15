/**
 * @(#)numberGrid.java
 *
 * This class draws a grid
 * that is filled with numbers
 *
 * This class is used to detect numbers that
 * the user wants to add to the Sudoku Grid
 *
 * @James Mathieson 
 * @version 1.00 18/03/10
 */

import javax.swing.*;
import java.awt.*;

public class NumberedGrid extends GUI_Extension {
	
	//gui object that is holding the numbered grid
	private GUI gui;
	
	/*
	 * Constructor
	 * set JPanel details and apply reference for GUI
	 * holding the numbered grid
	 */
	public NumberedGrid(GUI ref){
		//set and request focus so key listener can be used
    	this.setFocusable(true);
    	this.requestFocus();	
		gui=ref;						
		//set border and preferred size (set prefered or border will not fit around drawn image)
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray),"SelectNumber"));
		setPreferredSize(new Dimension(308,60));
		//change cursor type
    	setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
    
    /*
     * This paintComponent creates a grid numbered containig the numbers 1-9
     */
    public void paintComponent(Graphics g){
    	int x=10, y=18,height=30, width=30, spacing = 2;
        super.paintComponent(g);
        Font numberFont = new Font("Arial", Font.BOLD, 15);
        g.setFont(numberFont);
        for(int i=1;i<=9;i++){
        	g.setColor(Color.WHITE);
        	g.fillRect(x,y,width,height);
        	g.setColor(Color.GRAY);
        	g.drawRect(x,y,width,height);
        	g.setColor(Color.BLACK);
        	g.drawString(Integer.toString(i), x+12, height+10);
        	x+=(width+spacing);
        }
    }
    /*
     * This method calculates which number was selected
     */
    protected void calculatePosition(int clickX, int clickY){
    	//variables for calculating position
		int x=10, y=18,height=30, width=30, spacing = 2;
    	int value = 0;
    	for(int i=1;i<=9;i++){
    		if((clickX>=x)&&(clickX<=x+width)){				//check if the click was in x region of drawn number
    			if((clickY>=y)&&(clickY<=y+height)){			//check if the click was in y region of drawn number
    				value=i;								//assign value for number clicked
    				break;									//break out of loop
    			}
    		}
    		x+=(width+spacing);
    	}
    	if(value!=0)
     		gui.addToPuzzle(value);
     }
     
     /*
     * This method checks which key has been pressed
     * and, if numerical, sends it to the GUI
     */
     protected void checkKeyReleased(char c){
		//check whether keypressed is numeric - if it is then send to GUI (if not it will throw an error which is dealt with)
		try{
			int value = Integer.parseInt(Character.toString(c));
			gui.addToPuzzle(value);
		}
		catch(NumberFormatException nFE){
			//do nothing
		}
     }
    
}// end of numbered grid class