package org.galat.empiregame.net.packet;

import org.galat.empiregame.net.GameClient;
import org.galat.empiregame.net.GameServer;

/*****************************************************************************\
 *                                                                           *
 * Packet, abstract class                                                    *
 *                                                                           *
 * Class template for various new packet types to inherit.  For reading and  *
 * writing packets in multiplayer.                                           *
 *                                                                           *
\*****************************************************************************/


public abstract class Packet
{
	// enumeration of the various packet types for easy reference
	public static enum PacketTypes 
	{
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);
		
		private int packetId;  // reference id for this type 
		private PacketTypes(int packetId) // enumeration constructor
		{
			this.packetId = packetId;
		}
		
		public int getId()
		{
			return packetId;
		}
	}
	
	public byte packetId; // stores the id of the packet type
	public Packet(int packetId) // Packet constructor takes in the Id of the packet type
	{
		this.packetId = (byte) packetId; // typecast the id to a byte and set the class variable to it
	}
	
	public abstract void writeData(GameClient client); // send a packet of this type to the server
	public abstract void writeData(GameServer server); // send a packet of this type to the clients
	
	// take in the raw byte data and return the main data as a string 	
	public String readData(byte[] data) 
	{
		String message = new String(data).trim(); // convert byte[] to string and trim off any extra space
		return message.substring(2); // return everything after the packetId
	}
	
	// getData is to construct a packet of that type
	public abstract byte[] getData();
	
	// lookup the packet type where the packetId is a string
	public static PacketTypes lookupPacket(String packetId) 
	{
		try // try/catch in case the string passed doesn't parse to an int
		{
			return lookupPacket(Integer.parseInt(packetId)); // pass the packetId as in int to lookupPacket(int)
		} catch(NumberFormatException e) // if the packetId is not a number
		{
			return PacketTypes.INVALID; // it is an invalid packet type
		}
	}
	
	// lookup the packet type where the packetId is an int
	public static PacketTypes lookupPacket(int id)
	{
		for (PacketTypes p : PacketTypes.values()) // check each packet type in the enumeration
		{
			if (p.getId() == id) // if the packetId passed in matches the id of this type in the enumeration
				return p; // return the PacketType we are on
		}
		return PacketTypes.INVALID; // all PacketTypes were checked, none matched, so it's invalid
	}
}
