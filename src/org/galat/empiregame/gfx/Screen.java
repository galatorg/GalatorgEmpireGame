package org.galat.empiregame.gfx;

/*****************************************************************************\
 *                                                                           *
 * Screen class                                                              *
 *                                                                           *
 * Used to render anything to the main game area                             *
 *                                                                           *
\*****************************************************************************/

public class Screen
{
	public static final int MAP_WIDTH = 32;  // width of item in spritesheet?
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	public static final byte BIT_MIRROR_X = 0x01; // used to check if the X mirror bit is on
	public static final byte BIT_MIRROR_Y = 0x02; // used to check if the Y mirror bit is on
	
	public int[] pixels; // the array of pixels that get displayed on the screen
	
	public int xOffset = 0; // x offset of where the player is in the level
	public int yOffset = 0; // y offset of where the player is in the level
	
	public int width; // width of main game area to render on
	public int height; // height of main game area to render on
	
	// constructor
	public Screen(int width, int height)
	{
		this.width = width; // set the width variable
		this.height = height; // set the height variable
		
		pixels = new int[width * height]; // initialize the pixels array
	}
	
	// render something on the screen
	public void render(int xPos, int yPos, int tile, int color, int mirrorDir, int scale, SpriteSheet sheet)
	{
		xPos -= xOffset; // compensate for the xOffset
		yPos -= yOffset; // compensate for the yOffset
		
		boolean mirrorX = ( mirrorDir & BIT_MIRROR_X ) > 0; // check if the X mirror bit is on
		boolean mirrorY = ( mirrorDir & BIT_MIRROR_Y ) > 0; // check if the Y mirror bit is on
		
		int scaleMap = scale - 1;
		int xTile = tile%sheet.tilesOnX; // x position in tile grid
		int yTile = tile>>sheet.bitsNeeded; // y position in tile grid
		int tileOffset = (xTile<<sheet.bitsNeeded) + (yTile<<sheet.bitsNeeded) * sheet.width; // first pixel of the current tile
		
		for (int y = 0; y < sheet.tileSize; y++) // for each pixel in the height of the tile
		{
			int ySheet = y;	// current y coordinate in the tile
			if (mirrorY) ySheet = (sheet.tileSize-1) - y; // mirror the y coordinate in the tile if needed
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap<<sheet.bitsNeeded) / 2); // calculate what the starting y pixel coordinate on the game area taking scale into consideration
			
			for (int x = 0; x < sheet.tileSize; x++) // for each pixel in the width of the tile
			{
				int xSheet = x; // current x coordinate in the tile
				if (mirrorX) xSheet = (sheet.tileSize-1) - x; // mirror the x coordinate in the tile if needed
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap<<sheet.bitsNeeded) / 2); // calculate what the starting x pixel coordinate on the game area taking scale into consideration
				int col = (color >> (sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8))  & 255; // get the pixel/color of the current x,y pixel in the tile, &255 trims down to the blue channel that is currently using 2 bits to represent 4 colors
				
				if (col < 255) // if it's a valid color/pixel value
				{
					for (int yScale = 0; yScale < scale; yScale ++) // make this pixel render <scale> times in the y direction
					{
						if ((yPixel + yScale < 0) || (yPixel + yScale >= height)) continue; // limit the rendering to only the pixels on the screen, check on the y-axis
						for (int xScale = 0; xScale < scale; xScale++) // make this pixel render <scale> times in the x direction
						{
							if (xPixel + xScale < 0 || xPixel + xScale >= width) continue; // limit the rendering to only the pixels on the screen, check on the x-axis
							pixels[(xPixel+xScale) + (yPixel + yScale) * width] = col; // set this pixel/color
						}
					}
				}
			}
		}
	}

	// sets the x and y offset values to compensate xPos and yPos for when the player is near the edges of the level
	public void setOffset(int xOffset, int yOffset)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;	
	}
}
