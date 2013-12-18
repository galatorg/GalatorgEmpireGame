package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.gfx.Screen.colorStyle;

public class ExactTile extends Tile
{
	public final SpriteSheet tileSheet;
	protected int tileId;
	protected int xTile, yTile;

	
	// constructor
	public ExactTile(int id, int x, int y, int levelColor, SpriteSheet sheet) 
	{
		super(id, false, false, levelColor); // set as not solid and not an emitter
		if (sheet==null) sheet=SpriteSheet.defaultColorTiles;
		tileSheet = sheet;
		this.xTile = x;
		this.yTile = y;
		this.tileId = x + y * tileSheet.tileSize; // x coord/column + (y coord/row * number of tiles in a row)
	}

	public void tick() 
	{ }

	@Override
	public void render(Screen screen, int x, int y, int xOffset, int yOffset) 
	{
		int currentTileId = this.tileId;
		
		if (this.appearanceUpdates)
		{
			currentTileId = (this.xTile+xOffset) + ((this.yTile+yOffset) * tileSheet.tileSize);
		}
		
		screen.render(x, y, currentTileId, 0, colorStyle.DIRECTCOPY, 0x00, 1, tileSheet);
	}
}
