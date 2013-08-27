package org.galat.empiregame.gfx;

public class Font {

	private static String chars = ""+
	"abcdefghijklmnopqrstuvwxyz01234."+
	"ABCDEFGHIJKLMNOPQRSTUVWXYZ56789 ";
	
	public static void render(String msg, Screen screen, int x, int y, int color, int scale){
		for (int i = 0; i<msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if (charIndex >= 0) screen.render(x + (i*32), y, charIndex + 30*32, color, 0x00, scale);
		}
	}
}
