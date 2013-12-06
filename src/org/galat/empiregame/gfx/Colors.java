package org.galat.empiregame.gfx;

/*****************************************************************************\
 *                                                                           *
 * Colors class                                                              *
 *                                                                           *
 * Class that helps with decoding the colors used.  Uses an int to represent *
 * 4 colors.  Each color can have RGB values of 0-5                          *
 *                                                                           *
\*****************************************************************************/

public class Colors
{
	// puts the 4 colors together into 8 byte segments and returns it as an int
	public static int get(int color1, int color2, int color3, int color4)
	{
		return (get(color4)<<24) + (get(color3)<<16) + (get(color2)<<8) + get(color1); // get color value of each color and put the 4 colors together into one int 
	}
	
	// converts the int passed in with values [0-5][0-5][0-5], 000-555 into an int that can fit into 8 bytes, 216 colors
	private static int get(int color)
	{
		if (color<0) return 255; // default black
		int r = color/100%10; // first number
		int g = color/10%10; // second number
		int b = color%10; // third number
		return (r * 36) + (g * 6) + b;
	}
}
