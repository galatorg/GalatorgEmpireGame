package org.galat.empiregame.net.packet;

import org.galat.empiregame.net.GameClient;
import org.galat.empiregame.net.GameServer;

/*****************************************************************************\
 *                                                                           *
 * Packet01Disconnect, extends Packet                                        *
 *                                                                           *
 * Packet type 01, DISCONNECT, used to signal that a player has disconnected *
 *                                                                           *
 * Packet data contains username                                             *
 *                                                                           *
\*****************************************************************************/

public class Packet01Disconnect extends Packet 
{
	private String username; // to store the username for the packet
	
	// constructor for byte[]
	public Packet01Disconnect(byte[] data) 
	{
		super(01); // this packet will be a DISCONNECT type
		this.username = readData(data); // store the username from the data portion of the byte[]
	}

	// constructor for String
	public Packet01Disconnect(String username) 
	{
		super(01); // this packet will be a DISCONNECT type
		this.username = username;
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
	
	// construct the packet
	@Override
	public byte[] getData() 
	{
		return("01" + this.username).getBytes();
	}

	public String getUsername()
	{ 
		return username; 
	}
}
