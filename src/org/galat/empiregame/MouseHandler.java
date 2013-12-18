package org.galat.empiregame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener
{
	public MouseHandler(Game game)
	{
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
	}

	public void mouseClicked(MouseEvent arg0) { }
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent arg0) {	}
	public void mouseReleased(MouseEvent arg0) { }
	public void mouseDragged(MouseEvent arg0) {	}
	public void mouseMoved(MouseEvent arg0) { }
}
