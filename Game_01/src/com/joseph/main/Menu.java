package com.joseph.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {

	public String[] options = {"New Game","Load Game","Exit"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void update() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "New Game" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}else if(options[currentOption] == "Exit") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(new Font("arial", Font.BOLD, 40));
		
		g.drawString("No Name", (Game.WIDTH*Game.SCALE) /2 - 93, (Game.HEIGHT*Game.SCALE) /2 -150);
		
		//Menu Options
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		if(pause == false)
			g.drawString("New Game", (Game.WIDTH*Game.SCALE) /2 - 70, 160);
		else
			g.drawString("Continue", (Game.WIDTH*Game.SCALE) /2 - 70, 160);
		g.drawString("Load Game", (Game.WIDTH*Game.SCALE) /2 - 70, 200);
		g.drawString("Exit", (Game.WIDTH*Game.SCALE) /2 - 70, 240);
		
		if(options[currentOption] == "New Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 90, 160);
		}else if(options[currentOption] == "Load Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 90, 200);
		}else if(options[currentOption] == "Exit") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 90, 240);
		}
			
	}
	
}
