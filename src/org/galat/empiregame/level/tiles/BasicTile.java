package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;

/*****************************************************************************\
 *                                                                           *
 * BasicTile, extends Tile                                                   *
 *                                                                           *
 * Basic tile that a player can pass through                                 *
 *                                                                           *
\*****************************************************************************/

public class BasicTile extends Tile
{
	protected int tileId;
	protected int tileColor;
	
	// constructor
	public BasicTile(int id, int x, int y, int tileColor, int levelColor, SpriteSheet sheet) 
	{
		super(id, false, false, levelColor, sheet); // set as not solid and not an emitter
		this.tileId = x + y * tileSheet.tileSize; // x coord/column + (y coord/row * number of tiles in a row)
		this.tileColor = tileColor; // define the colors on the tile
	}

	public void tick() 
	{}
	
	public void render(Screen screen, Level level, int x, int y) 
	{
		screen.render(x, y, tileId, tileColor, 0x00, 1, tileSheet);
	}
}
