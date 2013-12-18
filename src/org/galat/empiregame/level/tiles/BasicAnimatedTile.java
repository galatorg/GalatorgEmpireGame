package org.galat.empiregame.level.tiles;

import org.galat.empiregame.gfx.SpriteSheet;

/*****************************************************************************\
 *                                                                           *
 * AnimatedTile, extends BasicTile, extends Tile                             *
 *                                                                           *
 * Class that stores the data needed to animate a tile                       *
 *                                                                           *
\*****************************************************************************/

public class BasicAnimatedTile extends BasicTile 
{
	private int[][] animationTileCoords; // set of coordinates that define which tiles to use, [0][0]=x1, [0][1]=y1, [1][0]=x2, [1][1]=y2, etc.
	private int currentAnimationIndex; // current animation tile being used
	private long lastIterationTime; // time that the index was last updated
	private int animationSwitchDelay; // delay time between animation frames
	
	// constructor
	public BasicAnimatedTile(int id, int[][] animationCoords, int tileColor, int levelColor, int animationSwitchDelay, SpriteSheet sheet) 
	{
		super(id, animationCoords[0][0], animationCoords[0][1], tileColor, levelColor, sheet); // set the data for first frame
		this.animationTileCoords = animationCoords; // coordinates of the animation tiles
		this.currentAnimationIndex = 0; // set the index at the first frame
		this.lastIterationTime = System.currentTimeMillis(); // initialize the last time the index was updated as now
		this.animationSwitchDelay = animationSwitchDelay;
	}
	
	// as time passes and the tick function gets called...
	public void tick() 
	{
		if ((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)) // if enough time has elapsed to increment the animation frame 
		{
			lastIterationTime = System.currentTimeMillis(); // set the last time the tile was updated as now
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length; // increment the animation frame index, but make sure it doesn't go past the end
			this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1]*tileSheet.tileSize)); // update the tile id based on the new index 
		}
	}
}
