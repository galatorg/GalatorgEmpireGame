package org.galat.empiregame.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.galat.empiregame.gfx.Screen.colorStyle;

/*****************************************************************************\
 *                                                                           *
 * SpriteSheet class                                                         *
 *                                                                           *
 * Processes a graphics image so that it can be used as a sprite sheet for   *
 * displaying tiles on the game area.  Can do spritesheets that are 2x2,     *
 * 4x4, 8x8, 16x16, 32x32, 64x64,...                                         *
 *                                                                           *
\*****************************************************************************/

public class SpriteSheet
{
	public final int bitsNeeded;
	public final String path;
	public final int width, height, tileSize, tilesOnX;
	public int[] pixels; // holds the image data
	
	// predefined spritesheets
	public static SpriteSheet defaultTiles = new SpriteSheet("/spritesheets/default/defaultTiles.png", 32, colorStyle.BASIC4);
	public static SpriteSheet defaultPlayer = new SpriteSheet("/spritesheets/default/defaultPlayer.png", 32, colorStyle.BASIC4);
	public static SpriteSheet defaultFont = new SpriteSheet("/spritesheets/default/defaultFont.png", 32, colorStyle.BASIC4);
	public static SpriteSheet defaultColorTiles = new SpriteSheet("/spritesheets/default/defaultTiles.png", 32, colorStyle.DIRECTCOPY);
	public static SpriteSheet defaultHUD = new SpriteSheet("/spritesheets/default/defaultHUD.png", 32, colorStyle.DIRECTCOPY);
	//public static SpriteSheet defaultGrassToDirt = new SpriteSheet("/spritesheets/default/transitionDirtGrass.png", 32, colorStyle.BASIC4);
	
	// constructor for a spritesheet (you can replace the 4 colors with whatever color you want)
	public SpriteSheet(String path, int size, colorStyle colorMode)
	{
		BufferedImage image = null; // variable to read the image data into
		
		try // attempt to read in the image data
		{
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path)); // open the file and read the contents into image
		}
		catch (IOException e) // if there was a problem opening the file and reading it
		{
			e.printStackTrace();
		}
		
		if (image == null) // if there is no data in the variable
		{
			this.path = "";
			this.height = 0;
			this.width = 0;
			this.tileSize = 0;
			this.bitsNeeded = 0;
			this.tilesOnX = 0;
			return; // do not continue
		}
		
		// since the data read into the image variable, we'll store the rest of the details
		this.path = path;
		this.tileSize = size;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.tilesOnX = this.width / this.tileSize; // calculate the number of tiles across
		
		int j=0;
		int i=1;
		while (tileSize > i)
		{
			i *= 2; // increase by a power of 2
			j++; // count 1 bit - 2^j = i
		}
		this.bitsNeeded = j;

		pixels = image.getRGB(0, 0, width, height, null, 0, width); // turn the image into an int[] of 0xARGB

		if (colorMode == colorStyle.BASIC4)
		{
			for (i = 0; i < pixels.length; i++) // process each pixel
			{
				pixels[i] = (pixels[i] & 0xff)/64; // cuts off the alpha channel and trims the blue channel down to 2 bits(4 colors in the spritesheet that can be used)
			}
		}
	}
}
