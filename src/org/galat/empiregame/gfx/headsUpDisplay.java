package org.galat.empiregame.gfx;

import org.galat.empiregame.Game;

/*****************************************************************************\
 *                                                                           *
 * HeadsUpDisplay class                                                      *
 *                                                                           *
 * Handles and renders the HUD into the pixels int[] for the screen.         *
 * TODO: allow the HUD to be dynamic and moved around                        *
 *                                                                           *
\*****************************************************************************/

public class HeadsUpDisplay
{
	private int screenWidth, screenHeight; // to know how the pixel int[] is setup
	private SpriteSheet sheet; // the HUD spritesheet
	private Game gameRef; // reference to the main game
	
	public HeadsUpDisplay(int width, int height, SpriteSheet sheet, Game game)
	{
		this.screenWidth = width;
		this.screenHeight = height;
		this.sheet = sheet;
		this.gameRef = game;
	}
	
	// render the HUD(on top of everything)
	public void render(int[] pixels)
	{
		renderItemSlots(pixels); // render the item slots
	}
	
	public void renderItemSlots(int[] pixels)
	{
		int actSlot = gameRef.activeSlot;
		
		if (actSlot == 0)
		{
			renderActiveItemSlot(32, 12, pixels); // render slot 1
		}
		else
		{
			renderItemSlot(32, 12, pixels); // render slot 1			
		}
		
		if (actSlot == 1)
		{
			renderActiveItemSlot(80, 12, pixels); // render slot 2
		}
		else
		{
			renderItemSlot(80, 12, pixels); // render slot 2
		}
		
		if (actSlot == 2)
		{
			renderActiveItemSlot(128, 12, pixels); // render slot 3
		}
		else
		{
			renderItemSlot(128, 12, pixels); // render slot 3
		}
		
		if (actSlot == 3)
		{
			renderActiveItemSlot(176, 12, pixels); // render slot 4
		}
		else
		{
			renderItemSlot(176, 12, pixels); // render slot 4
		}
	}
	
	// renders the active item slot sprite starting at xPos, yPos on the screen(in the pixels int[])
	public void renderActiveItemSlot(int xPos, int yPos, int[] pixels)
	{
		renderSprite(1, 0, xPos, yPos, pixels); // the active item slot is xTile-1, yTile-0
	}
	
	// renders the inactive item slot sprite starting at xPos, yPos on the screen(in the pixels int[])
	public void renderItemSlot(int xPos, int yPos, int[] pixels)
	{
		renderSprite(0, 0, xPos, yPos, pixels);	// the inactive item slot is xTile-0, yTile-0
	}
	
	public void renderSprite(int xSprite, int ySprite, int xPos, int yPos, int[] pixels)
	{
		int tileOffset = (xSprite<<this.sheet.bitsNeeded) + (ySprite<<this.sheet.bitsNeeded) * this.sheet.width; // first pixel of the current tile

		for (int y = 0; y < sheet.tileSize; y++) // along the height of the sprite, going down
		{
			if (yPos + y >= screenHeight) return; // if it's off the screen, below it, stop rendering(rending direction is down)
			if (yPos + y < 0) continue; // if it's off the screen, above it, go to the next line

			for (int x = 0; x < sheet.tileSize; x++) // along the width of the sprite, going right
			{
				if (xPos + x >= screenWidth) break; // if it's off the screen, to the right, no more rendering this line
				if (xPos + x < 0) continue; // if it's off the screen, to the left, the next one may be on the screen(rendering is to the right)

				int col = this.sheet.pixels[x + y * this.sheet.width + tileOffset]; // get the color at this pixel on the spritesheet
				if (col != 0xffff00ff) // if not the transparent color
				{
					pixels[(xPos+x) + ((yPos + y) * this.screenWidth)] = col; // set this pixel as this color
				}
			}
		}
	}
}
