package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.Screen.colorStyle;
import org.galat.empiregame.gfx.SpriteSheet;

/*****************************************************************************\
 *                                                                           *
 * BasicTile, extends Tile                                                   *
 *                                                                           *
 * Basic tile that a player can pass through                                 *
 *                                                                           *
\*****************************************************************************/

public class BasicTile extends Tile
{
	public final SpriteSheet tileSheet;
	protected int tileId;
	protected int tileColor;
	protected int xTile, yTile;
	
	// constructor
	public BasicTile(int id, int x, int y, int tileColor, int levelColor, SpriteSheet sheet) 
	{
		super(id, false, false, levelColor); // set as not solid and not an emitter
		if (sheet==null) sheet=SpriteSheet.defaultTiles;
		tileSheet = sheet;
		this.xTile = x;
		this.yTile = y;
		this.tileId = x + y * tileSheet.tileSize; // x coord/column + (y coord/row * number of tiles in a row)
		this.tileColor = tileColor; // define the colors on the tile
	}
 
	public void tick() 
	{ }
	
	public void render(Screen screen, int x, int y, int xOffset, int yOffset) 
	{
		int currentTileId = this.tileId;
		
		if (this.appearanceUpdates)
		{
			currentTileId = (this.xTile+xOffset) + ((this.yTile+yOffset) * tileSheet.tileSize);
		}
		
		screen.render(x, y, currentTileId, tileColor, colorStyle.BASIC4, 0x00, 1, tileSheet);
	}
}
