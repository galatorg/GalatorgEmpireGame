package org.galat.empiregame.gfx;

/*****************************************************************************\
 *                                                                           *
 * Font class                                                                *
 *                                                                           *
 * Used to render text to the screen                                         *
 * TODO: Replace with something that takes in a spritesheet(when changed)    *
 *                                                                           *
\*****************************************************************************/

public class Font
{
	// The order of the characters in the spritesheet
	private static String chars = ""+
	"abcdefghijklmnopqrstuvwxyz01234."+
	"ABCDEFGHIJKLMNOPQRSTUVWXYZ56789 ";
	
	// Renders text(msg) to the screen at x,y
	public static void render(String msg, Screen screen, int x, int y, int color, int scale)
	{
		// for each letter in the message
		for (int i = 0; i<msg.length(); i++)
		{
			int charIndex = chars.indexOf(msg.charAt(i)); // get the index of the current character in chars
			if (charIndex >= 0) screen.render(x + (i*32), y, charIndex + 30*32, color, 0x00, scale); // if the character was found, render it
		}
	}
}
