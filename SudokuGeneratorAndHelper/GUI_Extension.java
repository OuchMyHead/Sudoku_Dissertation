/**
 * @(#)GUI_Extension.java
 *
 * This is a super class that the JPanels
 * included in the GUI inherit from.
 *
 * This is used as each JPanel implement the MouseListener,
 * keyListener and/or actionListener in some way.
 *
 * @ James Mathieson
 * @ version 1.00 23/03/2010
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI_Extension extends JPanel implements MouseListener, KeyListener, ActionListener {
	
	
	/*
	 * Constructor
	 */
    public GUI_Extension() {
    	//add mouse listener to JPanel
    	this.addMouseListener(this);
    	//add key listener to JPanel
    	this.addKeyListener(this);
    }
    
    /*
     * These methods deals with the mouse events
     */
    public void mouseClicked(MouseEvent m){
    	if(m.getButton()==1)							//if the left button is clicked then call the following method
    		calculatePosition(m.getX(), m.getY());
    }
    public void mouseEntered(MouseEvent m){}
    public void mouseExited(MouseEvent m){}
    public void mouseReleased(MouseEvent m){}
    public void mousePressed(MouseEvent m){}
    
    
    /*
     * This method determines the position of the mouse
     * click in relation to the items within the JPanek
     */
     protected void calculatePosition(int x, int y){
     	// this method is overridden in subclasses
     }
    
    /*
     * These methods deal with the key events
     */
     public void keyReleased(KeyEvent k){
     	//set delete and backspace to 0 so they can be used to delete numbers from Sudoku grid
     	if(k.getKeyCode()==127||k.getKeyCode()==8){
     		String zero="0";
     		k.setKeyChar(zero.charAt(0));
     	}
     	checkKeyReleased(k.getKeyChar());
     }
     public void keyPressed(KeyEvent k){};
     public void keyTyped(KeyEvent k){};
    
    /*
     * This method checks which key has been pressed
     * and takes appropriate action
     */
     protected void checkKeyReleased(char c){
     	// this method is overridden in subclasses
     }
    
    /*
     * This method delas with any actions performed;
     * for example mouse clicks and keys entered
     */
     public void actionPerformed(ActionEvent e){
     	//this method is overridden in subclasses if used
     }

}//end of GUI_Extension class


