package org.galat.empiregame.level.tileset;

import org.galat.empiregame.level.tiles.Tile;

public abstract class Tileset {

	public final Tile[] tiles; // a Tile[] to store all the types of tiles
	private int tilesetSize;
	
	public Tileset(int tilesetSize)
	{
		this.tiles = new Tile[tilesetSize];
		this.tilesetSize = tilesetSize;
	}
	
	public void addTile(Tile newTile)
	{
		if (tiles[newTile.getId()] != null) throw new RuntimeException("Duplicate tile id on " + newTile.getId()); // if the tile id already exists in the tiles Tile[]
		tiles[newTile.getId()] = newTile; // put this tile in the tiles Tile[]
	}
	
	public int getTilesetSize()
	{
		return this.tilesetSize;
	}
}
