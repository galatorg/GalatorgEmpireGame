package org.galat.empiregame.entities;

import org.galat.empiregame.gfx.Screen;
import org.galat.empiregame.level.Level;

public abstract class Entity {

	public int x, y;
	protected Level level;

	public Entity(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);
}