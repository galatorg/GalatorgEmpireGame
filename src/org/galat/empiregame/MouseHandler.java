package org.galat.empiregame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/*****************************************************************************\
 *                                                                           *
 * MouseHandler class                                                        *
 *                                                                           *
 * Handles events with the mouse.                                            *
 *                                                                           *
\*****************************************************************************/

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private Game gameRef; // to interact with the game
	private int currentSteps = 0; // keeps track of steps used with the mouse wheel
	
	public MouseHandler(Game game)
	{
		game.addMouseListener(this); // to capture mouse clicks
		game.addMouseMotionListener(this); // to capture mouse movement
		game.addMouseWheelListener(this); // to capture mouse wheel movement
		gameRef = game;
	}

	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }

	// the mouse wheel scrolled, currently just used for the item slot
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int steps = e.getWheelRotation(); // get the mouse wheel movement, negative(-1) is up, positive(1) is down.

		int newSlot = gameRef.activeSlot; // default is the current slot
		
		if (((steps < 0) && (currentSteps < 0)) || ((steps > 0) && (currentSteps > 0))) // if still moving in the same direction
		{
			currentSteps += steps; // add to the same direction
		}
		else // not same direction or not at a half step
		{
			currentSteps = steps; // current steps is whatever the current movement was
		}
		
		if (currentSteps > 1) // if user scrolled down far enough
		{
			newSlot = newSlot + (currentSteps/2); // move 1 slot for every 2 steps
			newSlot = newSlot%4; // to make it wrap around
		}
		if (currentSteps < -1) // if user scrolled up far enough
		{
			newSlot = newSlot + (currentSteps/2); // move 1 slot for every 2 steps
			if (newSlot < 0) newSlot += 4; // to make it wrap around
		}
		
		currentSteps = currentSteps%2; // to count "halfway" to the next slot if the user moves in the same diretion
		gameRef.activeSlot = newSlot; // set the active item slot
	}
}
