package com.joseph.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.joseph.main.Game;
import com.joseph.world.Camera;
//import com.joseph.world.Camera;

public class Shoot extends Entity{
	
	public static int direction;
	
	private double dx;
	private double dy;
	private double spd = 4;
	private int duration = 40, maxDuration = 0;
	
	public Shoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x+=dx * spd;
		y+=dy * spd;
		duration--;
		if(maxDuration == duration) {
			Game.shoots.remove(this);
			return;
		}
	}

	public void render(Graphics g) {
		if(direction == 1) {
			g.drawImage(Game.arrow1.getSprite(0, 0, 50, 50), this.getX() - Camera.x, this.getY() - Camera.y, 16, 16, null);
		}else if(direction == -1) {
			g.drawImage(Game.arrow2.getSprite(0, 0, 50, 50), this.getX() - Camera.x, this.getY() - Camera.y, 16, 16, null);
		}else if(direction == 2) {
			g.drawImage(Game.arrow3.getSprite(0, 0, 50, 50), this.getX() - Camera.x, this.getY() - Camera.y, 16, 16, null);
		}else if(direction == -2) {
			g.drawImage(Game.arrow4.getSprite(0, 0, 50, 50), this.getX() - Camera.x, this.getY() - Camera.y, 16, 16, null);
		}
		super.render(g);
	}
	
}
