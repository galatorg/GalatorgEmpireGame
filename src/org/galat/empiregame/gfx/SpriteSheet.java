package org.galat.empiregame.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*****************************************************************************\
 *                                                                           *
 * SpriteSheet class                                                         *
 *                                                                           *
 * Processes a graphics image so that it can be used as a sprite sheet for   *
 * displaying tiles on the game area.  TODO: Upgrade to handle multiple      *
 * types of sprite sheets.  Make a sprite class?                             *
 *                                                                           *
\*****************************************************************************/

public class SpriteSheet
{

	public String path;
	public int width;
	public int height;
	public int[] pixels; // holds the image data
	
	// constructor for a basic 4 color spritesheet (you can replace the 4 colors with whatever color you want)
	public SpriteSheet(String path)
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
			return; // do not continue
		}
		
		// since the data read into the image variable, we'll store the rest of the details
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width); // turn the image into an int[] of 0xARGB
		
		for (int i = 0; i < pixels.length; i++) // process each pixel
		{
			pixels[i] = (pixels[i] & 0xff)/64; // cuts off the alpha channel and trims the blue channel down to 2 bits(4 colors in the spritesheet that can be used)
		}
	}
}
