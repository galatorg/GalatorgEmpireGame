package org.galat.empiregame.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.galat.empiregame.Game;
import org.galat.empiregame.entities.PlayerMP;
import org.galat.empiregame.net.packet.Packet;
import org.galat.empiregame.net.packet.Packet.PacketTypes;
import org.galat.empiregame.net.packet.Packet00Login;
import org.galat.empiregame.net.packet.Packet01Disconnect;
import org.galat.empiregame.net.packet.Packet02Move;

/*****************************************************************************\
 *                                                                           *
 * GameServer, threaded                                                      *
 *                                                                           *
 * Class to perform the operations of a server.  Clients connect to it and   *
 * send packets to it which get processed                                    *
 *                                                                           *
 * SPLIT INTO IT'S OWN APPLICATION?                                          *
 *                                                                           *
\*****************************************************************************/

public class GameServer extends Thread
{
	private DatagramSocket socket; // socket to accept packets
	private int listeningPort = 1331; // port to listen on
	private Game game; // to reference the game instance
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>(); // list of PlayerMP instances for each connected player - change to tree?
	
	// constructor
	public GameServer(Game game) 
	{
		this.game = game;
		try {
			this.socket = new DatagramSocket(listeningPort); // try to create a socket on the listening port to receive client packets
		} catch (SocketException e) // if it could not
		{
			e.printStackTrace();
		}
	}
	
	// when the thread is started
	public void run() 
	{
		while (true) // continue checking 
		{
			byte[] data = new byte[1024]; // create a byte[] to store a 1k packet
			DatagramPacket packet = new DatagramPacket(data, data.length); // create a 1k packet to store the data
			try {
				socket.receive(packet); // try to receive the data
			} catch (IOException e) { // if it had a problem receiving the data
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort()); // parse the packet if it received the data
		}
	}
	
	// function to parse data packets that have been received
	private void parsePacket(byte[] data, InetAddress address, int port) 
	{
		String message = new String(data).trim(); // convert to a string and trim the spaces
		PacketTypes types = Packet.lookupPacket(message.substring(0,2)); // lookup the packet type that was received based on the first two characters
		Packet packet = null; // create a null packet to be used to store and process the data
		switch (types) // check the type of packet
		{
			default:
			case INVALID: // if it was an INVALID packet or none of the other types, do not process it
				break;
			case LOGIN:
				packet = new Packet00Login(data); // create a 00 packet based on the data
				handleLogin((Packet00Login)packet, address, port); // typecast to a Packet00Login and pass into a function to process it
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data); // create a 01 packet based on the data
				handleDisconnect((Packet01Disconnect)packet, address, port); // typecast to a Packet01Disconnect and pass into a function to process it
				break;
			case MOVE:
				packet = new Packet02Move(data); // create a 02 packet based on the data
				this.handleMove((Packet02Move)packet); // typecast to a Packet02Move and pass into a function to process it
		}
	}

	// process a 00-LOGIN packet
	private void handleLogin(Packet00Login packet, InetAddress address, int port)
	{
		System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login)packet).getUsername() + " has connected");
		PlayerMP player = new PlayerMP(game.level, 100, 100, ((Packet00Login)packet).getUsername(), address, port); // create a PlayerMP to represent the player that connected
		this.addConnection(player, (Packet00Login)packet); // add the player to the list of connected players
		
	}
	
	// process a 01-DISCONNECT packet
	private void handleDisconnect(Packet01Disconnect packet, InetAddress address, int port)
	{
		System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + " has left");
		this.removeConnection((Packet01Disconnect)packet); // remove the player from the list of connected players
	}
	
	// process a 02-MOVE packet
	private void handleMove(Packet02Move packet) 
	{
		if(getPlayerMP(packet.getUsername()) != null) // if there is a username in the packet
		{
			int index = getPlayerMPIndex(packet.getUsername()); // get the player's index in the list of connected players
			PlayerMP player = this.connectedPlayers.get(index); // reference the player
			player.x = packet.getX(); // set the player's x coordinate
			player.y = packet.getY(); // set the player's y coordinate
			player.setMoving(packet.isMoving()); // set whether the player is moving or not
			player.setMovingDir(packet.getMovingDir()); // set the direction the player is moving
			player.setNumSteps(packet.getNumSteps()); // set the player's number of steps
			packet.writeData(this); // send this packet to all the clients
		}
	}

	// add the player to the list of connected players and let the other players know a new player has connected
	public void addConnection(PlayerMP player, Packet00Login packet) 
	{
		boolean alreadyConnected = false; // flag to check if the player is already in the list of connected player
		for (PlayerMP p : this.connectedPlayers) // for each playermp in the list of connected players
		{
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) // if a player by this name is here in the list
			{
				if (p.ipAddress == null) // if the ipaddress is not set
				{
					p.ipAddress = player.ipAddress; // set the ipaddress
				}
				
				if (p.port == -1) // if the port is not set
				{
					p.port = player.port; // set the port
				}
				alreadyConnected = true; // set the flag to signal that the player is already in the list of connected players
			}
			else // if the player's name doesn't match, meaning it's another player in the list
			{
				sendData(packet.getData(), p.ipAddress, p.port); // inform this player in the list that a new player connected
				packet = new Packet00Login(p.getUsername(), p.x, p.y); // construct new login packet for this player in the list
				sendData(packet.getData(), player.ipAddress, player.port); // inform the new player of this player in the list
			}
		}
		if (!alreadyConnected) // if the player was not already in the list of connected players
		{
			this.connectedPlayers.add(player); // add the player to the list of connected players
		}
	}
	
	public void removeConnection(Packet01Disconnect packet) 
	{
		this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
		packet.writeData(this);
	}
	
	public PlayerMP getPlayerMP(String username) 
	{
		for (PlayerMP player : this.connectedPlayers){
			return player;
		}
		return null;
	}
	
	public int getPlayerMPIndex(String username) 
	{
		int index = 0;
		for (PlayerMP player : this.connectedPlayers)
		{
			if (player.getUsername().equals(username)) 
				break;
			index++;
		}
		return index;
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) 
	{
		if (!game.isApplet)
		{ 
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
			try 
			{
				socket.send(packet);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	// sends a packet to all the players that are connected
	public void sendDataToAllClients(byte[] data) 
	{
		for (PlayerMP p : connectedPlayers) // for each playermp in the list of connected players
			sendData(data, p.ipAddress, p.port); // send the data to the current playermp in the loop
	}
}
