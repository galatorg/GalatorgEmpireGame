package org.galat.empiregame.net.packet;

import org.galat.empiregame.net.GameClient;
import org.galat.empiregame.net.GameServer;

/*****************************************************************************\
 *                                                                           *
 * Packet02Move, extends Packet                                              *
 *                                                                           *
 * Packet type 02, MOVE, used to signal that a player has moved              *
 *                                                                           *
 * Packet data contains username, x coordinate, y coordinate, number of      *
 * steps taken, if the player is moving, and the direction they are moving   *
 *                                                                           *
\*****************************************************************************/

public class Packet02Move extends Packet
{
	private String username; // to store the username for the packet
	private int x,y; // to store the x and y cordinates for the packet
	private int numSteps = 0; // to store the number of steps for the packet
	private boolean isMoving; // to store if the player is moving for the packet
	private int movingDir = 1; // to store the player's direction for the packet
	
	// constructor for byte[]
	public Packet02Move(byte[] data) 
	{
		super(02); // this packet will be a MOVE type
		String[] dataArray = readData(data).split(","); // split the data portion in the byte[] into a String[] containing
		this.username = dataArray[0]; // the username in the first chunk
		this.x = Integer.parseInt(dataArray[1]); // the x coordinate in the second chunk
		this.y = Integer.parseInt(dataArray[2]); // the y coordinate in the third chunk
		this.numSteps = Integer.parseInt(dataArray[3]); // number of steps taken in the fourth chunk
		this.isMoving = Integer.parseInt(dataArray[4]) == 1; // if the player is moving in the fifth chunk
		this.movingDir = Integer.parseInt(dataArray[5]); // the direction the player is moving in the sixth chunk
	}

	// constructor that takes in arguments for each variable in the class
	public Packet02Move(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) 
	{
		super(02); // this packet will be a MOVE type
		this.username = username;
		this.x = x;
		this.y = y;
		this.numSteps = numSteps;
		this.isMoving = isMoving;
		this.movingDir = movingDir;
	}	
	
	// construct the packet and send it to the server
	// FUNCTION FOR CLIENT
	@Override
	public void writeData(GameClient client) 
	{
		client.sendData(getData());
	}

	// construct the packet and send it to all the clients
	// FUNCTION FOR SERVER
	@Override
	public void writeData(GameServer server) 
	{
		server.sendDataToAllClients(getData());
	}
	
	//construct this packet
	@Override
	public byte[] getData() 
	{
		return("02" + this.username + "," + this.x + "," + this.y + "," + this.numSteps + "," + (isMoving?1:0) + "," + this.movingDir).getBytes();
	}

	public String getUsername() 
	{
		return username;
	}
	
	public int getX() 
	{
		return this.x;
	}
	
	public int getY() 
	{
		return this.y;
	}

	public int getNumSteps() 
	{
		return numSteps;
	}

	public boolean isMoving() 
	{
		return isMoving;
	}

	public int getMovingDir() 
	{
		return movingDir;
	}
}
