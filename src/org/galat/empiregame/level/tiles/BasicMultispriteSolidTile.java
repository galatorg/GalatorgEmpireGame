package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

public class BasicMultispriteSolidTile extends BasicMultispriteTile
{
	public BasicMultispriteSolidTile(int id, int numSprites, int x, int y, int tileColor, int levelColor, SpriteSheet sheet)
	{
		super(id, numSprites, x, y, tileColor, levelColor, sheet); // same thing as a multisprite tile
		this.solid = true; // but the solid boolean gets set to true
	}
}