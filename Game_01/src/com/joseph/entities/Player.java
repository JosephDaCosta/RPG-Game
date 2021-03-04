package com.joseph.entities;

//import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.joseph.graficos.Spritesheet;
import com.joseph.main.Game;
import com.joseph.world.Camera;
import com.joseph.world.World;

public class Player extends Entity{
	
	public boolean right,left,up,down;
	public boolean cRight = false, cLeft = false, cUp = false, cDown = false;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public double speed = 3;
	
	private int frames = 0, maxFrames = 6, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	public boolean isDamaged = false;
	private int damagedFrames = 0;
	
	public boolean shoot = false; 
	
	public boolean hasWeapon = false;
	
	public double life = 100, maxLife = 100;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
				
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		upPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		
		for(int i = 0; i < 3; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(442, 0 + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(391, 0 + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(425 , 0 + (i*17), 16, 16);			
		}
		for(int i = 0; i < 3; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(408 , 0 + (i*17), 16, 16);			
		}
		
	}
	
	public void update() {
		moved = false;

		if(!cRight) {
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		}else {
			moved = false;
			dir = right_dir;
				index = 0;
		}
	
		if(!cLeft) {
		if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		}else {
			moved = false;
			dir = left_dir;
				index = 0;
		}
	
		if(!cUp) {
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}
		}else {
			moved = false;
			dir = up_dir;
				index = 0;
		}
	
		if(!cDown) {
		if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		}
		}else {
			moved = false;
			dir = down_dir;
				index = 0;
		}
		
		if(moved) {
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
		
		this.checkCollisionLifePotion();
		this.checkCollisionWeapon();
		
		if(isDamaged) {
			damagedFrames++;
			if(this.damagedFrames == 3) {
				this.damagedFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			//Shoot
			shoot = false;
			if(hasWeapon) {				
			shoot = false;
			int dx = 0, dy = 0, px = 0, py = 0;
			System.out.println("Shoots!");
			if(dir == right_dir) {
				dx = 1;
				px = 5;
				py = 4;
				Shoot.direction = 1;
			}else if(dir == left_dir) {
				dx = -1;
				px = -4;
				py = 4;
				Shoot.direction = -1;
			}else if(dir == up_dir) {
				dy = -1;
				py = -6;
				px = 1;
				Shoot.direction = 2;
			}else if(dir == down_dir) {
				dy = 1;
				py = 8;
				px = 0;
				Shoot.direction = -2;
			}
			
			Shoot shoot = new Shoot(this.getX()+px, this.getY()+py, 2, 2, null, dx, dy);
			Game.shoots.add(shoot);
			
		}
		}
		/*
		if(life <= 0) {
			//Game Over!
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/tilemap.png");
			Game.spritesheet2 = new Spritesheet("/Overworld.png");
			Game.spritesheet3 = new Spritesheet("/Items.png");
			Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(408, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/Map.png");
			return;
		}
		*/
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2),0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2),0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionLifePotion() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePotion) {
				if(Entity.isColliding(this, atual)) {
					if(life < 100) {
					life+=Game.rand.nextInt(30) + 15;
					Game.entities.remove(atual);
					}
					if(life > 100)
					life = 100;	
				}
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					hasWeapon = true;
					//System.out.println("get weapon");
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			if(hasWeapon) {
				//Draw weapon
				//g.drawImage(Game.spritesheet3.getSprite(10*48, 6*48, 48, 48),this.getX() + 8 - Camera.x, this.getY()-Camera.y, 16, 16, null );
			}
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			if(hasWeapon) {
				//Draw weapon
				//g.drawImage(Game.spritesheet3.getSprite(10*48, 6*48, 48, 48),this.getX() - 8 - Camera.x, this.getY()-Camera.y, 16, 16, null );
			}
		}else if(dir == up_dir) {
			g.drawImage(upPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			if(hasWeapon) {
				//Draw weapon
				//g.drawImage(Game.spritesheet3.getSprite(10*48, 6*48, 48, 48),this.getX() + 8 - Camera.x, this.getY()-Camera.y, 16, 16, null );
			}
		}else if(dir == down_dir) {
			g.drawImage(downPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			if(hasWeapon) {
				//Draw weapon
				//g.drawImage(Game.spritesheet3.getSprite(10*48, 6*48, 48, 48),this.getX() + 8 - Camera.x, this.getY()-Camera.y, 16, 16, null );
			}
		}else {
			//g.setColor(Color.red);
			//g.setFont(new Font("arial", Font.BOLD,5));
			//g.drawString(Game.player.isDamaged, this.getX(), this.getY());
		}
		}
	}

}
