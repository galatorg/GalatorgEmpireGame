package org.galat.empiregame.level.tileset;

import org.galat.empiregame.gfx.Colors;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.tiles.AnimatedTile;
import org.galat.empiregame.level.tiles.BasicSolidTile;
import org.galat.empiregame.level.tiles.BasicTile;
import org.galat.empiregame.level.tiles.Tile;

/*****************************************************************************\
 *                                                                            *
 * DefaultTileset class                                                       *
 *                                                                            *
 * The default tileset used in the game, hard-coded into the game              *
 *                                                                            *
\*****************************************************************************/

public class DefaultTileset extends Tileset
{
	public static final Tile VOID = new BasicSolidTile(0,  0,  0, Colors.get(000, -1, -1, -1), 0xFF000000, SpriteSheet.defaultTiles); // define VOID tile, index of 0
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xFF555555, SpriteSheet.defaultTiles); // define STONE tile, index of 1
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00, SpriteSheet.defaultTiles); // define GRASS tile, index of 2
	public static final Tile WATER = new AnimatedTile(3, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 1000, SpriteSheet.defaultTiles); // define WATER tile, index of 3

	// constructor, just call it to setup the default set of tiles to use
	public DefaultTileset()
	{
		super(4); // set the size of the tileset
		this.addTile(VOID); // add the VOID tile to the set
		this.addTile(STONE); // add the STONE tile to the set
		this.addTile(GRASS); // add the GRASS tile to the set
		this.addTile(WATER); // add the WATER tile to the set
	}
}
