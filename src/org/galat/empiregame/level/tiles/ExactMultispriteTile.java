package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.gfx.Screen.colorStyle;

public class ExactMultispriteTile extends BasicMultispriteTile
{

	public ExactMultispriteTile(int id, int numSprites, int x, int y, int levelColor, SpriteSheet sheet)
	{
		super(id, numSprites, x, y, 0, levelColor, sheet);
	}

	public void render(Screen screen, int x, int y, int xOffset, int yOffset)
	{
		int newId = (this.xTile + xOffset) + (this.yTile * this.tileSheet.width);
		screen.render(x, y, newId, 0, colorStyle.DIRECTCOPY, 0x00, 1, tileSheet);
	}
}
