package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

public class BasicTransitionTile extends BasicTile
{
	
	public BasicTransitionTile(int id, int dominantTileId, int x, int y, int tileColor, int levelColor, SpriteSheet sheet)
	{
		super(id, x, y, tileColor, levelColor, sheet);
		domTileId = dominantTileId;
		appearanceUpdates = true;
	}
}
