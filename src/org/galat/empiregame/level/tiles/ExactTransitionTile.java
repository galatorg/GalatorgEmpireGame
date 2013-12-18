package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

public class ExactTransitionTile extends ExactTile
{

	public ExactTransitionTile(int id, int dominantTileId, int x, int y, int levelColor, SpriteSheet sheet)
	{
		super(id, x, y, levelColor, sheet);
		domTileId = dominantTileId;
		appearanceUpdates = true;
	}
}
