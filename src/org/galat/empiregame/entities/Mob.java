package org.galat.empiregame.entities;

import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;
import org.galat.empiregame.level.tiles.Tile;

/*****************************************************************************\
 *                                                                           *
 * Mob, extends Entity                                                       *
 *                                                                           *
 * Base class for other classes to inherit to represent various mob entities *
 * in a level, such as a player or creature.                                 *
 *                                                                           *
\*****************************************************************************/

public abstract class Mob extends Entity {

	protected String name; // mob name/type
	protected int speed; // mob speed
	protected int numSteps = 0; // number of steps taken, move to player? remove?
	protected boolean isMoving; // if the mob is moving
	protected int movingDir = 1; // direction of movement, 0=up, 1=down, 2=left, 3=right
	protected int scale = 1; // scale size of the mob
	
	// mob constructor
	public Mob(Level level, String name, int x, int y, int speed, SpriteSheet sheet)
	{
		super(level, sheet);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	// move
	public void move(int xa, int ya)
	{
		if (xa != 0 && ya != 0) // if told to move both in the x and y directions
		{
			move(xa, 0); // move in the x direction first
			move(0, ya); // then move in the y direction
			numSteps--;  // compensate for the extra step
			return; // already moved by splitting up x and y movement, skip everything else
		}
		numSteps++;
		if (!hasCollided(xa, ya)) // check to make sure the player isn't being stopped by a solid tile
		{
			if (ya < 0) movingDir = 0; // up
			if (ya > 0) movingDir = 1; // down
			if (xa < 0) movingDir = 2; // left
			if (xa > 0) movingDir = 3; // right
			
			x += xa * speed; // increment x and compensate for speed
			y += ya * speed; // increment y and compensate for speed
		}
	}
	
	public abstract boolean hasCollided(int xa, int ya); // to check whether the mob will be stopped by something  
	
	// function to check if a tile in the level is solid or not, for use with hasCollided
	protected boolean isSolidTile(int xa, int ya, int x, int y)
	{
		if (level == null) // if the level does not exist
		{
			return false; // then there is nothing to check, default false(not solid)
		}
		
		Tile lastTile = level.levelTileset.tiles[level.getTileId(((this.x + x)>>level.tilesSheet.bitsNeeded), ((this.y +y)>>level.tilesSheet.bitsNeeded))]; // the tile you are coming from
		Tile newTile = level.levelTileset.tiles[level.getTileId(((this.x + x + xa)>>level.tilesSheet.bitsNeeded), ((this.y + y + ya)>>level.tilesSheet.bitsNeeded))]; // the tile you are going to
		
		if (!lastTile.equals(newTile) && newTile.isSolid()) // if the mob has not changed tiles and the tile you are moving to is solid
		{
			return true; // mob will collide with a solid tile
		}
		
		return false; // no collision detected with a solid tile
	}
	
	public String getName()
	{
		return name;
	}

	public void setNumSteps(int numSteps)
	{
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving)
	{
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir)
	{
		this.movingDir = movingDir;
	}
}