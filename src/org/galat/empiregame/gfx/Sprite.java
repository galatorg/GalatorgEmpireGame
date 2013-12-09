package org.galat.empiregame.gfx;

// not used, added for reference mostly.  may use this while upgrading to multiple spritesheets
// the current rendering system is likely more efficient, this will load the sprites into a
// new section of memory - wasting some memory and processing speed. look into it...could
// be efficient, useful, and needed to make things more modular

public class Sprite
{
	private SpriteSheet sheet;
	private final int SIZE;
	private int x, y;
	public int[] pixels;
	
	public static Sprite grass = new Sprite(1, 32, 3, 0, SpriteSheet.defaultTiles);
	
	public Sprite(int id, int size, int x, int y, SpriteSheet sheet)
	{
		if (sheet==null) sheet = SpriteSheet.defaultTiles;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * SIZE; // tile ID to pixel ID 
		this.y = y * SIZE; // tile ID to pixel ID
		this.sheet = sheet;
		load();
	}
	
	private void load()
	{
		for (int y = 0; y < SIZE; y++)
		{
			for (int x = 0; x < SIZE; x++)
			{
				pixels[x+y*SIZE] = sheet.pixels[(x+this.x) + (y+this.y)*sheet.width];
			}
		}
	}
	
	public int getSize()
	{
		return SIZE;
	}
}
