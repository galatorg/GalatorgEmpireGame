package org.galat.empiregame;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

/*****************************************************************************\
 *                                                                           *
 * GameLauncher class                                                        *
 *                                                                           *
 * Starts up the game as an applet or application.                           *
 *                                                                           *
\*****************************************************************************/

@SuppressWarnings("serial")
public class GameLauncher extends Applet
{
	public static Game game = new Game();
	
	// initialize the game, for an applet
	@Override
	public void init()
	{
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setMinimumSize(Game.DIMENSIONS);
		setMaximumSize(Game.DIMENSIONS);
		setPreferredSize(Game.DIMENSIONS);
		game.isApplet = true;
	}
	
	@Override
	public void start()
	{
		game.start();
	}
	
	@Override
	public void stop()
	{
		game.stop();
	}
	
	public static void main(String[] args)
	{
		game.setMinimumSize(Game.DIMENSIONS);
		game.setMaximumSize(Game.DIMENSIONS);
		game.setPreferredSize(Game.DIMENSIONS);
		
		game.frame = new JFrame(Game.NAME);
		
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());
		
		game.frame.add(game, BorderLayout.CENTER);
		game.frame.pack();
		
		game.frame.setResizable(false);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}
