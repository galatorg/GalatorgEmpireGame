package org.galat.empiregame.entities;

import org.galat.empiregame.Game;
import org.galat.empiregame.InputHandler;
import org.galat.empiregame.gfx.Colors;
import org.galat.empiregame.gfx.Font;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.level.Level;
import org.galat.empiregame.net.packet.Packet02Move;

public class Player extends Mob {

	private InputHandler input;
	private int color = Colors.get(-1,  111,  145,  543);
	private int scale = 1;
	protected boolean isSwimming = false;
	private int tickCount = 0;
	private String username;
	
	public Player(Level level, int x, int y, InputHandler input, String username) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.username = username;
	}

	public void tick() {
		int xa = 0;
		int ya = 0;

		if (input!=null) {			
			if (input.up.isPressed()) {
				ya--;
			}
			if (input.down.isPressed()) {
				ya++;
			}
			if (input.left.isPressed()) {
				xa--;
			}
			if (input.right.isPressed()) {
				xa++;
			}
		}
		
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
			
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
			packet.writeData(Game.game.socketClient);
			
		} else {
			isMoving = false;
		}
		if (level.getTile(this.x>>5, this.y>>5).getId() == 3) {
			isSwimming = true;
		}
		if (isSwimming && level.getTile(this.x>>5,  this.y>>5).getId() != 3) {
			isSwimming = false;
		}
		tickCount++;
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 29;
		int walkingSpeed = 4;
		int flipIt = (numSteps >> walkingSpeed) & 1;
		
		
		if (movingDir == 1) // if down 
		{
			xTile += 1;
		} else if (movingDir > 1) // if left or right
		{
			int walkFrame = ((numSteps >> walkingSpeed) % 4);
			if (walkFrame == 3)
			{
				xTile = 3;
			} else {
				xTile += 2 + walkFrame;
			}
			flipIt = (movingDir - 1) % 2; // 0 for left, 1 for right
		}
		
		int modifier = 32 * scale;
		int xOffset = x - modifier/2 + 16;
		int yOffset = y - modifier/2;
		
		if (!isSwimming) {
			screen.render(xOffset, yOffset, xTile + yTile * 32, color, flipIt, scale);
		} else {
			int waterColor = 0;
			
			if (tickCount%60 < 15) {
				waterColor = Colors.get(-1,  -1, 225, -1);
			} else if (15 <= tickCount%60 && tickCount%60 < 30) {
				waterColor = Colors.get(-1,  225, 115, -1);
				yOffset -= 1;
			} else if (30 <= tickCount%60 && tickCount%60 < 45) {
				waterColor = Colors.get(-1,  115, -1, 225);
				yOffset -= 2;
			} else {
				waterColor = Colors.get(-1,  225, 115, -1);
				yOffset -= 1;
			}
			screen.render(xOffset, yOffset, xTile + yTile * 32, color, flipIt, scale);
			screen.render(xOffset,  yOffset, 28 * 32, waterColor, 0x00, 1);	
		}
		
		if (username != null) {
			Font.render(username, screen, xOffset - ((username.length()-1)/2 * 32), yOffset-20, Colors.get(-1,  -1, -1, 555), 1);
		}
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 10;
		int xMax = 20;
		int yMin = -5;
		int yMax = 13;

	    if (xa != 0) {
	            for(int x = xMin; x <= xMax; x++) {
	                    if (isSolidTile(xa, ya, x, yMin+1) || isSolidTile(xa, ya, x, yMax-1)) {
	                            return true;
	                    }
	            }
	    }
	                   
	    if (ya != 0) {
	            for(int y = yMin; y <= yMax; y++) {
	                    if (isSolidTile(xa, ya, xMin+1, y) || isSolidTile(xa, ya, xMax-1, y)) {
	                            return true;
	                    }
	            }
	    }

		return false;
	}
	
	public String getUsername() {
		return this.username;
	}
}
