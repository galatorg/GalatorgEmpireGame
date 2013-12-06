package org.galat.empiregame.level.tiles;

/*****************************************************************************\
 *                                                                           *
 * BasicSolidTile, extends BasicTile, extends Tile                           *
 *                                                                           *
 * Basic solid tile that a player cannot pass through                        *
 *                                                                           *
\****************************************************************************/

public class BasicSolidTile extends BasicTile 
{
	// constructor
	public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor) 
	{
		super(id, x, y, tileColor, levelColor); // same thing as a BasicTile
		this.solid = true; // but the solid boolean gets set to true
	}
}
