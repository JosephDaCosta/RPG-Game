package com.joseph.graficos;

import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics;

import com.joseph.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(15, 6, 50, 8);
		g.setColor(Color.green);
		g.fillRect(15, 6,(int)((Game.player.life/Game.player.maxLife)*50), 8);
		/*
		//Show Player Life
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 27, 13);
		*/
	}
	
}
