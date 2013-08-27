package org.galat.empiregame.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.galat.empiregame.Game;
import org.galat.empiregame.entities.PlayerMP;
import org.galat.empiregame.net.packet.Packet;
import org.galat.empiregame.net.packet.Packet.PacketTypes;
import org.galat.empiregame.net.packet.Packet00Login;
import org.galat.empiregame.net.packet.Packet01Disconnect;
import org.galat.empiregame.net.packet.Packet02Move;

/*****************************************************************************\
 *                                                                           *
 * GameClient, threaded                                                      *
 *                                                                           *
 * Class to connect to a server.  Used to send data to the server.  It also  *
 * receives data from the server and processes it.                           *
 *                                                                           *
\*****************************************************************************/

public class GameClient extends Thread 
{

	private InetAddress ipAddress; // stores the ip address of the server
	private int port = 1331; // stores the port to connect to the server on
	private DatagramSocket socket; // used to make a UDP connection to the server
	private Game game; // game reference
	
	// constructor
	public GameClient(Game game, String ipAddress) 
	{
		this.game = game;
		try 
		{
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) // couldn't make a new DatagramSocket 
		{
			e.printStackTrace();
		} catch (UnknownHostException e) // ipAddress was invalid
		{
			e.printStackTrace();
		}
	}
	
	// when the thread is started
	public void run() 
	{
		while (true) // keep running the thread running
		{
			byte[] data = new byte[1024]; // use 1k packets
			DatagramPacket packet = new DatagramPacket(data, data.length); // packet object 1k in size to receive data from the server
			try 
			{
				socket.receive(packet); // receive incoming data into the packet
			} catch (IOException e) // if it could not get the data
			{
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort()); // parse the data from the data in the packet
		}
	}
	
	// used to parse data received from the server and process it
	private void parsePacket(byte[] data, InetAddress address, int port) 
	{
		String message = new String(data).trim(); // trim off any space
		PacketTypes types = Packet.lookupPacket(message.substring(0,2)); // get the first two characters and lookup what type of packet it is
		Packet packet = null; // used to typecast the packet into it's type and pass that data to a function to process it
		switch (types) // check which type of packet it was 
		{
			default:
			case INVALID: // if the packet type was INVALID or of no other type
				break;  // don't do anything with the data
			case LOGIN: // if it was a LOGIN packet
				packet = new Packet00Login(data); // create a 00 packet based on the data
				handleLogin((Packet00Login)packet, address, port); // typecast to a Packet00Login and pass into a function to process it
				break;
			case DISCONNECT: // if it was a DISCONNECT packet
				packet = new Packet01Disconnect(data);  // create a 01 packet based on the data
				handleDisconnect((Packet01Disconnect)packet, address, port); // typecast to a Packet01Disconnect and pass into a function to process it
				break;
			case MOVE: // if it was a MOVE packet
				packet = new Packet02Move(data); // create a 02 packet based on the data
				handleMove((Packet02Move)packet); // typecast to a Packet02Move and pass into a function to process it
		}
	}

	// send a packet of data to the server
	public void sendData(byte[] data) 
	{
		// if this is not running as an applet - NEEDS TO BE FIXED EVENTUALLY
		if (!game.isApplet)
		{
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, this.port); // create a new packet to the server with the data 
			try 
			{
				socket.send(packet); // try to send the packet to the server
			} catch (IOException e)  // failed to send the packet
			{
				e.printStackTrace();
			}
		}
	}
	
	// process a 00-LOGIN packet
	private void handleLogin(Packet00Login packet, InetAddress address, int port) 
	{
		System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUsername() + " has joined the game");
		PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port); // create a new PlayerMP instance to represent this player
		game.level.addEntity(player); // add the PlayerMP just created to the list of entities in the level
	}
	
	// process a 01-DISCONNECT packet
	private void handleDisconnect(Packet01Disconnect packet, InetAddress address, int port)
	{
		System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect)packet).getUsername() + " has left the world");
		game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername()); // remove the player in the list of entities based on username		
	}
	
	// process a 02-MOVE packet
	private void handleMove(Packet02Move packet) 
	{
		// move the player based on data from the packet
		this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
	}
}
