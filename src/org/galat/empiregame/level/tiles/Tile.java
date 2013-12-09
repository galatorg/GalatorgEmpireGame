package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;

/*****************************************************************************\
 *                                                                           *
 * Tile, abstract class                                                      *
 *                                                                           *
 * Class template for various types of tiles to inherit.  For storing info   *
 * about the tiles that get rendered. TODO: Make a loadTiles() that can load *
 * new tile definitions over the defaults(boolean to retain the old and just *
 * overwrite certain ones or add new.  Also, set new tilesheet?              *
 *                                                                           *
\*****************************************************************************/

public abstract class Tile
{
	public final SpriteSheet tileSheet;
	protected short id; // stores the index of the instance of this Tile in the tiles Tile[] - short can allow up to 65536 different tile types if negative values used
	protected boolean solid; // if the player can pass through the tile
	protected boolean emitter; // unused? if block emits light?
	private int levelColor; // color that represents it in the level bitmap
	
	// constructor
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor, SpriteSheet sheet) 
	{
		if (sheet==null) sheet=SpriteSheet.defaultTiles;
		tileSheet = sheet;
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
	
	public abstract void render(Screen screen, Level level, int x, int y);
}
