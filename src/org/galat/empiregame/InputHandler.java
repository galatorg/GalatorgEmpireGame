package org.galat.empiregame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*****************************************************************************\
 *                                                                           *
 * InputHandler class                                                        *
 *                                                                           *
 * Handles events with the keyboard.                                         *
 *                                                                           *
\*****************************************************************************/

public class InputHandler implements KeyListener{

	public InputHandler(Game game)
	{
		game.addKeyListener(this);
	}
	
	// internal class used to define keys and retain whether they are pressed or not
	public class Key
	{
		public boolean pressed = false;
		
		public boolean isPressed()
		{
			return pressed;
		}
		
		public void toggle(boolean isPressed)
		{
			pressed = isPressed;
		}
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public void keyPressed(KeyEvent e)
	{
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e)
	{
		toggleKey(e.getKeyCode(), false);		
	}

	public void keyTyped(KeyEvent e) { }

	// retain the state of the key that way changed
	public void toggleKey(int keyCode, boolean isPressed)
	{
		if (keyCode == KeyEvent.VK_W)
		{
			up.toggle(isPressed); 
		}
		if (keyCode == KeyEvent.VK_S)
		{
			down.toggle(isPressed); 
		}
		if (keyCode == KeyEvent.VK_A)
		{
			left.toggle(isPressed); 
		}
		if (keyCode == KeyEvent.VK_D)
		{
			right.toggle(isPressed); 
		}
	}
}
