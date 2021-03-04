package com.joseph.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.joseph.main.Game;
import com.joseph.world.Camera;

public class Weapon extends Entity {

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void render(Graphics g) {
		g.drawImage(Game.spritesheet3.getSprite(10*48, 6*48, 48, 48),this.getX()-Camera.x, this.getY()-Camera.y, 16, 16, null );
		super.render(g);
	}
	
}
