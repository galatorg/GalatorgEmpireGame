package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

public class ExactMultispriteSolidTile extends ExactMultispriteTile
{

	public ExactMultispriteSolidTile(int id, int numSprites, int x, int y, int levelColor, SpriteSheet sheet)
	{
		super(id, numSprites, x, y, levelColor, sheet);
		this.solid = true;
	}

}
