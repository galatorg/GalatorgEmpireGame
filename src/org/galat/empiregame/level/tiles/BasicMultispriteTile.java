package org.galat.empiregame.level.tiles;

import java.util.Random;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.gfx.Screen.colorStyle;

public class BasicMultispriteTile extends BasicTile
{
	private int numSprites;
	
	public BasicMultispriteTile(int id, int numSprites, int x, int y, int tileColor, int levelColor, SpriteSheet sheet)
	{
		super(id, x, y, tileColor, levelColor, sheet);
		this.numSprites = numSprites;
		this.domTileId = -2;
		this.appearanceUpdates = true;
	}

	public void render(Screen screen, int x, int y, int xOffset, int yOffset)
	{
		int newId = (this.xTile + xOffset) + (this.yTile * this.tileSheet.width);
		screen.render(x, y, newId, 0, colorStyle.BASIC4, 0x00, 1, tileSheet);
	}
	
	public int randomizeTile()
	{
		Random newOffset = new Random();
		return newOffset.nextInt(numSprites);
	}
}
