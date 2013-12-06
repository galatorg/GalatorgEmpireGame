package org.galat.empiregame.entities;

import java.net.InetAddress;

import org.galat.empiregame.InputHandler;
import org.galat.empiregame.level.Level;

/*****************************************************************************\
 *                                                                           *
 * PlayerMP, extends Player, extends Mob, extends Entity                     *
 *                                                                           *
 * More functional Player class for handling multiplayer characters.  Merge  *
 * this into the Player class?                                               *
 *                                                                           *
\*****************************************************************************/

public class PlayerMP extends Player
{
	public InetAddress ipAddress;
	public int port;

	// constructor for the user's player
	public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port)
	{
		super(level, x, y, input, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	// constructor for other players
	public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port)
	{
		super(level, x, y, null, username); // null used for no input
		this.ipAddress = ipAddress;
		this.port = port;
	}	
	
	@Override
	public void tick()
	{
		super.tick();
	}
}
