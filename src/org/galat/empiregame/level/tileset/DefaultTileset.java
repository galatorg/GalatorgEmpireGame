package org.galat.empiregame.level.tileset;

import org.galat.empiregame.gfx.Colors;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.tiles.AnimatedTile;
import org.galat.empiregame.level.tiles.BasicSolidTile;
import org.galat.empiregame.level.tiles.BasicTile;
import org.galat.empiregame.level.tiles.Tile;

public class DefaultTileset extends Tileset
{
	public static final Tile VOID = new BasicSolidTile(0,  0,  0, Colors.get(000, -1, -1, -1), 0xFF000000, SpriteSheet.defaultTiles); // define VOID tile, index of 0
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xFF555555, SpriteSheet.defaultTiles); // define STONE tile, index of 1
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00, SpriteSheet.defaultTiles); // define GRASS tile, index of 2
	public static final Tile WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 1000, SpriteSheet.defaultTiles); // define WATER tile, index of 3

	public DefaultTileset()
	{
		super(256);
		this.addTile(VOID);
		this.addTile(STONE);
		this.addTile(GRASS);
		this.addTile(WATER);
	}
}
