package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;

/*****************************************************************************\
 *                                                                           *
 * Tile, abstract class                                                      *
 *                                                                           *
 * Class template for various types of tiles to inherit.  For storing info   *
 * about the tiles that get rendered.  Stores common data about tile types   *
 *                                                                           *
\*****************************************************************************/

public abstract class Tile
{
	protected short id; // stores the index of the instance of this Tile in the tiles Tile[] - short can allow up to 65536 different tile types if negative values used
	protected boolean solid; // if the player can pass through the tile
	protected boolean emitter; // unused? if block emits light?
	private int levelColor; // color that represents it in the level bitmap
	public boolean appearanceUpdates = false;
	public int domTileId = 0; // used in transition tiles currently to determine the dominant tile
	
	// constructor
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) 
	{
		this.id = (short) id;
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColor = levelColor;
	}

	public short getId() 
	{
		return id;
	}
	
	public int getLevelColor() 
	{
		return levelColor;
	}

	public boolean isSolid() 
	{
		return solid;
	}
	
	public boolean isEmitter()
	{
		return emitter;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen, int x, int y, int xOffset, int yOffset);

}
