package com.joseph.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.joseph.entities.Enemy;
import com.joseph.entities.Entity;
import com.joseph.entities.Player;
import com.joseph.entities.Shoot;
import com.joseph.graficos.Spritesheet;
import com.joseph.graficos.UI;
import com.joseph.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private  static int fps_show = 0;
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> shoots;
	public static Spritesheet spritesheet;
	public static Spritesheet spritesheet2;
	public static Spritesheet spritesheet3;
	public static Spritesheet arrow1, arrow2, arrow3, arrow4;
	public static World world;
	
	public static Player player;
	
	public static Random rand; 
	
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando objetos.
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shoots = new ArrayList<Shoot>();
		
		spritesheet = new Spritesheet("/tilemap.png");
		spritesheet2 = new Spritesheet("/Overworld.png");
		spritesheet3 = new Spritesheet("/Items.png");
		arrow1 = new Spritesheet("/sprite_1.png");
		arrow2 = new Spritesheet("/sprite_1_left.png");
		arrow3 = new Spritesheet("/sprite_1_up.png");
		arrow4 = new Spritesheet("/sprite_1_down.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(408, 0, 16, 16));
		entities.add(player);
		world = new World("/level_1.png");
		
		menu = new Menu();
	}
	public void initFrame() {
		frame = new JFrame("First Game");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void update() {
		if(gameState == "NORMAL") {
		this.restartGame = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
		}
		
		for(int i = 0; i < shoots.size(); i++) {
			shoots.get(i).update();
		}
		
		if(enemies.size() == 0) {
			//Next level!
			//System.out.println("Level Up!");	//Debug next level
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level_" + CUR_LEVEL + ".png";
			//System.out.println(newWorld);
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			//System.out.println("Game Over");	//Debugando o sistema de game over
			this.framesGameOver ++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showGameOver)
					this.showGameOver = false;
					else
						this.showGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level_" + CUR_LEVEL + ".png";
				//System.out.println(newWorld);
				World.restartGame(newWorld);
			}
			
		}else if(gameState == "MENU") {
			//Game menu.
			menu.update();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		//Renderização do jogo.
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < shoots.size(); i++) {
			shoots.get(i).render(g);
		}
		ui.render(g);
		/***/		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		//Show weapon
		if(Game.player.hasWeapon == true) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,20));
		g.drawString("wooden bow", 300, 20);
		}
		//Show Player Life
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 85, 37);
		//Show FPS in game
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + fps_show, WIDTH + 390, 20);
		
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			
			g.setFont(new Font("arial", Font.BOLD, 35));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH*SCALE) / 2 - 93, 230);
			
			g.setFont(new Font("arial", Font.BOLD, 20));
			if(this.showGameOver)
				g.drawString("Pressione ENTER para reiniciar", (WIDTH*SCALE) / 2 - 145, 470);
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		
		bs.show();
	}
	
	@Override
	public void run() {
		
		requestFocus();
		
		//Frames
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		//Game Looping
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				
				update();
				render();
				
				frames ++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				fps_show = frames;
				//System.out.println("Frames " + frames);
				frames = 0;
				timer += 1000;
			}
			
		}
		stop();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//Andar para direita.
				  player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			//Andar para esquerda
				  player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			//Andar para cima.
				  player.up = true;
				  if(gameState == "MENU") {
						menu.up = true;
					}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			//Andar para Baixo.
				  player.down = true;
				if(gameState == "MENU") {
					menu.down = true;
				}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && e.isControlDown()) {
			player.cRight = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT && e.isControlDown()) {
			player.cLeft = true;
		}else if (e.getKeyCode() == KeyEvent.VK_UP && e.isControlDown()) {
			player.cUp = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN && e.isControlDown()) {
			player.cDown = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;		
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			//Andar para cima.
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			//Andar para Baixo.
			player.down = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.cRight = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.cLeft = false;
		}else if (e.getKeyCode() == KeyEvent.VK_UP) {
			player.cUp = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.cDown = false;
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		player.MouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
		//System.out.println(player.mx);
		//System.out.println(player.my);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

