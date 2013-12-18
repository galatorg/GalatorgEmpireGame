package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

public class ExactSolidTile extends ExactTile
{
	// constructor
	public ExactSolidTile(int id, int x, int y, int levelColor, SpriteSheet sheet) 
	{
		super(id, x, y, levelColor, sheet); // same thing as a BasicTile
		this.solid = true; // but the solid boolean gets set to true
	}
}
