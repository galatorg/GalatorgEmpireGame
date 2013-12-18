package org.galat.empiregame.gfx;

import org.galat.empiregame.gfx.Screen.colorStyle;

/*****************************************************************************\
 *                                                                           *
 * Font class                                                                *
 *                                                                           *
 * Used to render text to the screen                                         *
 *                                                                           *
\*****************************************************************************/

public class Font
{
	// The order of the characters in the spritesheet
	private static String chars;
	private static SpriteSheet fontSheet;
	public static final Font defaultFont = new Font("abcdefghijklmnopqrstuvwxyz01234.ABCDEFGHIJKLMNOPQRSTUVWXYZ56789 ", SpriteSheet.defaultFont);
	
	public Font(String charset, SpriteSheet sheet)
	{
		if (charset==null) charset = "abcdefghijklmnopqrstuvwxyz01234.ABCDEFGHIJKLMNOPQRSTUVWXYZ56789 ";
		chars = charset;
		if (sheet==null) sheet = SpriteSheet.defaultFont;
		fontSheet = sheet;
	}
	
	// Renders text(msg) to the screen at x,y
	public void render(String msg, Screen screen, int x, int y, int color, int scale)
	{
		// for each letter in the message
		for (int i = 0; i<msg.length(); i++)
		{
			int charIndex = chars.indexOf(msg.charAt(i)); // get the index of the current character in chars
			if (charIndex >= 0) screen.render(x + (i*fontSheet.tileSize), y, charIndex, color, colorStyle.BASIC4, 0x00, scale, fontSheet); // if the character was found, render it
		}
	}
	
	public int getFontSize()
	{
		return fontSheet.tileSize;
	}
}
