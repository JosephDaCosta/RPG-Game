package com.joseph.entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.joseph.main.Game;
import com.joseph.world.Camera;
import com.joseph.world.World;

public class Enemy extends Entity {
	
	public static Player player;
	
	private double speed = 0.4;
	
	private int maskx = 3, masky = 5, maskw = 10, maskh = 10;
	private boolean move = true;
	
	private int frames = 0, maxFrames = 8, index = 0, maxIndex = 3;
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] downEnemy;
	
	private int life = 20;
	
	private boolean isDamaged = false;
	private int damagedFrames = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[3];
		leftEnemy = new BufferedImage[3];
		upEnemy = new BufferedImage[3];
		downEnemy = new BufferedImage[3];
		
		for(int i = 0; i < 3; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(26*17, (6*17) + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(23*17, (6*17) + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			upEnemy[i] = Game.spritesheet.getSprite(25*17 , (6*17) + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			downEnemy[i] = Game.spritesheet.getSprite(24*17 , (6*17) + (i*17), 16, 16);			
		}
		
	}
	
	public void update() {
			if(this.isCollidingWithPlayer() == false) {
				move = true;
				if((int)x < Game.player.getX() && World.isFree((int)(x+speed),this.getY())
						&& !isColliding((int)(x+speed),this.getY())) {
					x+=speed;
				}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed),this.getY())
						&& !isColliding((int)(x-speed),this.getY())) {
					x-=speed;
				}else if((int)y < Game.player.getY() && World.isFree(this.getX(),(int)(y+speed))
						&& !isColliding(this.getX(),(int)(y+speed))) {
					y+=speed;
				}else if((int)y > Game.player.getY() && World.isFree(this.getX(),(int)(y-speed))
						&& !isColliding(this.getX(),(int)(y-speed))) {
					y-=speed;
				}
			}else {
				//Colliding with player.
				//What we do?
				if(Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
				}	
			}
			if(this.isCollidingWithPlayer() == true) {
				move = false;
			}
		
			if(move) {
			frames ++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index == maxIndex)
					index = 0;
			}	
			}else {
				index = 0;
			}
			
			collidingShoot();
			if(life <= 0) {
				destroyEnemy();
				return;
			}
			if(life < 0) {
				life = 0;
				destroyEnemy();
				return;
			}
			
			if(isDamaged) {
				damagedFrames++;
				if(this.damagedFrames == 6) {
					this.damagedFrames = 0;
					isDamaged = false;
				}
			}
			
	}
	
	public void destroyEnemy() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingShoot() {
		for(int i = 0; i < Game.shoots.size(); i++) {
			Entity e = Game.shoots.get(i);
			if(e instanceof Shoot) {
				if(Entity.isColliding(this, e)) {
					isDamaged = true;
					life -= Game.rand.nextInt(5) + 5;
					//life -= 20; //Privilégio de desenvolvedor
					Game.shoots.remove(i);
					//System.out.println("Colisão"); //Testando colisão das balas
					System.out.println(life); //Testando o damage sofrido
					return;
				}
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY()+ masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
		if(this.getX() < Game.player.getX()) {
			g.drawImage(rightEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);							
		}else if(this.getX() > Game.player.getX()) {
			g.drawImage(leftEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);							
		}else if(this.getY() > Game.player.getY()) {
			g.drawImage(upEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);			
		}else if(this.getY() < Game.player.getY()) {
			g.drawImage(downEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);			
		}
		}else {
			
		}
	/*
		//Show collision mask.
		g.setColor(Color.blue);
		g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	*/
	}
}
