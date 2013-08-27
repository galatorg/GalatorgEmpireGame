package org.galat.empiregame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.galat.empiregame.net.packet.Packet01Disconnect;

public class WindowHandler implements WindowListener {

	private final Game game;
	
	public WindowHandler(Game game) {
		this.game = game;
		if (!game.isApplet){ 
			this.game.frame.addWindowListener(this);
		}
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (!game.isApplet){ 
			Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
			packet.writeData(this.game.socketClient);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

}
