package org.galat.empiregame.net.packet;

import org.galat.empiregame.net.GameClient;
import org.galat.empiregame.net.GameServer;

/*****************************************************************************\
 *                                                                           *
 * Packet00Login, extends Packet                                             *
 *                                                                           *
 * Packet type 00, LOGIN, used to signal that a player has connected         *
 *                                                                           * 
 * Packet data contains username, x coordinate, and y coordinate             *
 *                                                                           *
\****************************************************************************/

public class Packet00Login extends Packet
{

	private String username;  // to store the username for the packet
	private int x, y; // to store x and y coordinates for the packet
	
	// constructor for byte[]
	public Packet00Login(byte[] data)
	{
		super(00); // this packet will be a LOGIN PacketType
		String[] dataArray = readData(data).split(","); // get the data portion of the data and split it into a String[] containing
		this.username = dataArray[0]; // the username in the first chunk
		this.x = Integer.parseInt(dataArray[1]); // the player's x coordinate in the second chunk
		this.y = Integer.parseInt(dataArray[2]); // the player's y coordinate in the third chunk
	}

	// constructor that takes in arguments for each variable in the class
	public Packet00Login(String username, int x, int y)
	{
		super(00); // this packet will be a LOGIN PacketType
		this.username = username; // store the username in this instance
		this.x = x; // store the player's x coordinate in this instance
		this.y = y; // store the player's y coordinate in this instance
	}	
	
	// construct the packet based on the data in the variables and send it to the server
	// FUNCTION FOR CLIENT
	@Override
	public void writeData(GameClient client)
	{
		client.sendData(getData());
	}

	// construct the packet based on the data in the variable and send it to all the clients
	// FUNCTION FOR SERVER
	@Override
	public void writeData(GameServer server)
	{
		server.sendDataToAllClients(getData());
	}
	
	// construct this packet
	@Override
	public byte[] getData()
	{
		return("00" + this.username + "," + getX() + "," + getY()).getBytes();
	}

	public String getUsername()
	{
		return username;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
