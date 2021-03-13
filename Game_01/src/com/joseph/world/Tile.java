package com.joseph.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.joseph.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet2.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet2.getSprite(2*16, 14*16, 16, 16);
	public static BufferedImage TILE_WALL2 = Game.spritesheet2.getSprite(4*16, 5*16, 16, 16);
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
}
