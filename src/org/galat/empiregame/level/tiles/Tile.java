package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Colors;
import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.level.Level;

/*****************************************************************************\
 *                                                                           *
 * Tile, abstract class                                                      *
 *                                                                           *
 * Class template for various types of tiles to inherit.  For storing info   *
 * about the tiles that get rendered.  TODO: take in a sprite into the       *
 * constructor?                                                              *
 *                                                                           *
\*****************************************************************************/

public abstract class Tile
{

	public static final Tile[] tiles = new Tile[256]; // a Tile[] to store all the types of tiles, limit 256
	public static final Tile VOID = new BasicSolidTile(0,  0,  0, Colors.get(000, -1, -1, -1), 0xFF000000); // define VOID tile, index of 0
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xFF555555); // define STONE tile, index of 1
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00); // define GRASS tile, index of 2
	public static final Tile WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 1000); // define WATER tile, index of 3
	protected short id; // stores the index of the instance of this Tile in the tiles Tile[] - short can allow up to 65536 different tile types if negative values used
	protected boolean solid; // if the player can pass through the tile
	protected boolean emitter; // unused? if block emits light?
	private int levelColor; // color that represents it in the level bitmap
	
	// constructor
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) 
	{
		this.id = (short) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id); // if the tile id already exists in the tiles Tile[]
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColor = levelColor;
		tiles[id] = this; // put this tile in the tiles Tile[]
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
