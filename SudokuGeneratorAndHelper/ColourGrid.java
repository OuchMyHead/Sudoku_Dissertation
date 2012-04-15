/**
 * @(#)ColourGrid.java
 * 
 * This class creates a grid that holds the
 * the colours that the user can use to help
 * solve the puzzle
 * 
 *
 * @James Mathieson
 * @version 1.00 19/03/2010
 */

import javax.swing.*;
import java.awt.*;

public class ColourGrid extends GUI_Extension {
	
	//an array of colour objects
	private Color[] colours;
	
	//gui object that is holding the colourgrid
	private HelpTools helpTools;
	
	/*
	 * Constructor - sets preferred size for JPanel
	 * and assign reference for object holding grid
	 */
    public ColourGrid(HelpTools ref) {
    	helpTools=ref;
    	setPreferredSize(new Dimension(47,47));
    }
    
    /*
     * This paintComponent creates a 3*3 grid holding 9 different colours
     */
    public void paintComponent(Graphics g)
    {
    	//create an array of colours and add colours to array
    	colours = new Color[9];
    	colours[0]= new Color(255,204,255);
    	colours[1]= new Color(255,255,0);
    	colours[2]= new Color(102,255,255);
    	colours[3]= new Color(255,153,204);
    	colours[4]= new Color(255,204,0);
    	colours[5]= new Color(102,204,255);
    	colours[6]= new Color(255,51,204);
    	colours[7]= new Color(255,153,0);
    	colours[8]= new Color(102,153,255);
    	//set gerid variables and draw grid
    	int x=0, y=0,height=15, width=15;
        super.paintComponent(g);
        for(int i=1;i<=9;i++){
        	Color colour = colours[i-1];
        	g.setColor(colour);
        	g.fillRect(x,y,width,height);
        	g.setColor(Color.GRAY);
        	g.drawRect(x,y,width,height);
        	x+=width;
        	if(i%3==0){							// if count is divisible by 3 then increase y and reset x
        		y += height;
        		x = 0;	
        	}
        }
    }
    
    /*
	 * This method calculates which colour (if any) was selected
	 * - if a colour is selected then this is sent to the appropriate method in
	 * the associated helpTools object
	 */
	protected void calculatePosition(int clickX, int clickY){
    	//variables for calculating position
		int x=0, y=0,height=15, width=15;
    	int value = -1;
    	for(int i=1;i<=9;i++){
    		if((clickX>=x)&&(clickX<=x+width)){				//check if the click was in x region of drawn colour
    			if((clickY>=y)&&(clickY<=y+height)){			//check if the click was in y region of drawn colour
    				value=i-1;								//assign value for number clicked (subtract one as it is an array)
    				break;									//break out of loop
    			}
    		}
    		x+=width;
    		if(i%3==0){							// if count is divisible by 3 then increase y and reset x
        		y+=height;
        		x=0;
        	}
    	}
    	// if colour was selected then send colour to help tool method - colourHelp()
    	if(value!=-1)
     		helpTools.colourHelp(colours[value]);
     }
    
    
}// end of ColourGrid Class