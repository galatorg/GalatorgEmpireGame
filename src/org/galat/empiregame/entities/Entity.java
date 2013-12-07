package org.galat.empiregame.entities;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.gfx.SpriteSheet;
import org.galat.empiregame.level.Level;

/*****************************************************************************\
 *                                                                           *
 * Entity, abstract class                                                    *
 *                                                                           *
 * Base class for other classes to inherit to represent entities in a level  *
 * such as players and mobs.                                                 *
 *                                                                           *
\*****************************************************************************/

public abstract class Entity
{

	public int x, y; // store coordinates of the entity
	protected Level level; // reference to the Level the entity is in
	public SpriteSheet entitySheet;

	public Entity(Level level, SpriteSheet sheet)
	{
		if (sheet==null) sheet = SpriteSheet.defaultPlayer;
		entitySheet = sheet;
		init(level);
	}
	
	public final void init(Level level)
	{
		this.level = level;
	}
	
	public abstract void setSpriteSheet(SpriteSheet sheet);
	
	public abstract void tick(); // game tick to update the entity's properties
	
	public abstract void render(Screen screen); // function to render the entity to screen/game area
}
