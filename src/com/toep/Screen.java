package com.toep;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Screen extends JPanel {

	public static Sheet tileSheet, playerSheet, fontsheet, propSheet, skySheet, guiSheet;
	/**
	 * 
	 */
	int tick = 0;

	ArrayList<Particle> particles = new ArrayList<Particle>();
	private static final long serialVersionUID = 1L;
	BufferedImage img;
	DrawableObject gasStation, marketPlace, garage;
	Cloud[] cloud = new Cloud[35];
	// static int SIZE = 960;
	int[] data;
	static Tile t[] = new Tile[40];
	float xPos = 0, yPos = 0;
	final static int WORLD_WIDTH_TILE = 100;
	final static int WORLD_HEIGHT_TILE = 400;
	int[][] tiles = new int[WORLD_WIDTH_TILE][WORLD_HEIGHT_TILE];
	private Player p;
	private static int[][] letters = new int[91][64];
	private final int GAS_PRICE = 1;
	private final int GUI_FUEL_X = 250;
	private final int GUI_FUEL_Y = 10;
	private final int GUI_HULL_X = GUI_FUEL_X+120;
	private final int GUI_HULL_Y = GUI_FUEL_Y;
	private final int FUEL_STATION_X = 32*10, SELL_ORE_X = 32*13, UPGRADE_X = 32*16;
	Drawable gasGUI, hullGUI, moneyGUI, heatGUI, heatGUI_arrow, upgradeGUI_Bar, upgradeGUI_Bar_green, upgradeGUI_Bar_yellow, upgradeGUI_Bar_red, upgradeGUI_arrow,upgradeGUI_check;
	ArrayList<AnimatedDrawable> animations = new ArrayList<AnimatedDrawable>();
	int[] skyShades = new int[256];
	static String fongLegend = new String(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&'()*+,-./:;<=>?[\\]^_|@ ");

	private boolean atBuilding = false;// show clicking gui
	private int buildingID = -1;// no
	private int[] price = new int[]{30, 60, 100, 250, 750, 2000, 5000, 20000, 100000, 50000, 50000};
	private int deadTick;
	private int dayTime = 1;
	private boolean updateOpen = false;
	int updateStack = 0;//0 = select upgrade type, 1 = buy lv1-5
	int updateselect0 = 0;
	int updateselect1 = 0;
	int heat = 0;
	
	final int ID_AIR = -1, ID_DIRT = 0, ID_GRASS = 1, ID_BEDROCK = 2, ID_COPPER = 3, ID_IRON = 4, ID_RUBY = 5, ID_SAPPHIRE = 6, ID_EMERALD = 7, ID_GEMSTONE = 8, ID_DIAMOND = 9, ID_EBONITE = 10, ID_MYSTITE = 11, ID_CHEST = 12, ID_BONE = 13, ID_ROCK = 14, ID_LAVA = 15;
	//int SKY_COLOR = 0xff87CEEB;
	int dd = 1;
	public Screen() {
		tileSheet = new Sheet("tiles.png", 256, 256);
		playerSheet = new Sheet("playersheet.png", 220, 200);
		guiSheet = new Sheet("gui.png", 256, 128);

		fontsheet = new Sheet("font.png", 496, 16);
		propSheet = new Sheet("props.png", 256, 256);
		skySheet = new Sheet("sky.png", 256, 1);
		skyShades = BitmapLoader.readFromSheet(skySheet, 0, 0, 256, 1);
		img = new BufferedImage((int) (Main.WIDTH), (int) (Main.HEIGHT),
				BufferedImage.TYPE_INT_ARGB);
		data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < letters.length; i++) {
			letters[i] = BitmapLoader.readFromSheet(fontsheet, (i % 62)*8,
					(i / 62)*8, 8);
		}
		gasStation = new DrawableObject(BitmapLoader.readFromSheet(propSheet, 0, 0,
				64), 64, 64, FUEL_STATION_X,
				320 - 64);
		marketPlace = new DrawableObject(BitmapLoader.readFromSheet(propSheet, 64, 0,
				64), 64, 64, SELL_ORE_X,
				320 - 64);
		garage = new DrawableObject(BitmapLoader.readFromSheet(propSheet, 64, 64,
				64), 64, 64, UPGRADE_X,
				320 - 64);
		for(int i = 0; i < cloud.length; i++){
			if(Math.random()<.5)
				cloud[i] = new Cloud(BitmapLoader.readFromSheet(Screen.propSheet, 128, 0, 128, 64), 128, 64);
			else
				cloud[i] = new Cloud(BitmapLoader.readFromSheet(Screen.propSheet, 0,64, 64, 64), 64, 64);

		}
		gasGUI = new Drawable(BitmapLoader.readFromSheet(guiSheet, 0, 0, 19, 50), 19, 50);
		hullGUI = new Drawable(BitmapLoader.readFromSheet(guiSheet, 19, 0, 19, 50), 19, 50);
		moneyGUI = new Drawable(BitmapLoader.readFromSheet(guiSheet, 38, 0, 120, 39), 120, 39);
		heatGUI = new Drawable(BitmapLoader.readFromSheet(guiSheet, 59, 39, 109, 11), 109, 11);
		heatGUI_arrow = new Drawable(BitmapLoader.readFromSheet(guiSheet, 67, 50, 7, 8), 7, 8);

		upgradeGUI_Bar = new Drawable(BitmapLoader.readFromSheet(guiSheet, 0, 50, 67, 11), 67, 11);
		upgradeGUI_Bar_green = new Drawable(BitmapLoader.readFromSheet(guiSheet, 0, 61, 67, 11), 67, 11);
		upgradeGUI_Bar_yellow = new Drawable(BitmapLoader.readFromSheet(guiSheet, 0, 72, 67, 11), 67, 11);
		upgradeGUI_Bar_red = new Drawable(BitmapLoader.readFromSheet(guiSheet, 0, 83, 67, 11), 67, 11);

		upgradeGUI_arrow = new Drawable(BitmapLoader.readFromSheet(guiSheet, 38, 39, 10, 11), 10, 11);
		upgradeGUI_check = new Drawable(BitmapLoader.readFromSheet(guiSheet, 48, 39, 11, 11), 11, 11);

		for (int i = 0; i < t.length; i++) {
			t[i] = new Tile(i);
		}
		p = new Player(300, 235, this);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = ID_AIR;// set everthing to air
			}
		}
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 10; j < tiles[0].length; j++) {
				if (j == 10){
					tiles[i][j] = ID_GRASS;
					if(i >8 && i < 19)
						tiles[i][j] = ID_BEDROCK;
				}
				else {
					tiles[i][j] = ID_DIRT;// dirt
					if (Math.random() < .15) {
						if (j < 50)
							tiles[i][j] = ID_COPPER;
					}
					if (Math.random() < .15) {
						if (j < 80 && j > 10)
							tiles[i][j] = ID_IRON;
					}
					if (Math.random() < .002) {
						if (j < 300 && j > 80)
							tiles[i][j] = ID_IRON;
					}
					if (Math.random() < .04) {
						if (j < 200 && j > 70)
							tiles[i][j] = ID_RUBY;
					}
					if (Math.random() < .04) {
						if (j < 250 && j > 130)
							tiles[i][j] = ID_SAPPHIRE;
					}
					if (Math.random() < .04) {
						if (j < 290 && j > 200)
							tiles[i][j] = ID_EMERALD;
					}
					if (Math.random() < .04) {
						if (j < 310 && j > 240)
							tiles[i][j] = ID_GEMSTONE;
					}
					if (Math.random() < .05) {
						if (j < 400 && j > 200)
							tiles[i][j] = ID_ROCK;
					}
					if (Math.random() < .05) {
						if (j < 400 && j > 280)
							tiles[i][j] = ID_ROCK;
					}
					if (Math.random() < .07) {
						if (j < 400 && j > 320)
							tiles[i][j] = ID_ROCK;
					}
					if (Math.random() < .05) {
						if (j < 400 && j > 260)
							tiles[i][j] = ID_LAVA;
					}
					if (Math.random() < .08) {
						if (j < 400 && j > 360)
							tiles[i][j] = ID_LAVA;
					}
					if (Math.random() < .05) {
						if (j < 350 && j > 300)
							tiles[i][j] = ID_DIAMOND;
					}
					if (Math.random() < .005) {
						if (j < 400 && j > 350)
							tiles[i][j] = ID_EBONITE;
					}
					if (Math.random() < .0003) {
						tiles[i][j] = ID_CHEST;
					}
					if (Math.random() < .0003) {
						tiles[i][j] = ID_BONE;
					}

					if (Math.random() < .1)
						tiles[i][j] = ID_AIR;
				}
				if (j == tiles[0].length - 1 || i == tiles.length - 1 || i == 0)
					tiles[i][j] = ID_BEDROCK;
			}
		}

	}

	

	public void paint() {
		// System.out.println(t.width);
		xPos = p.x - 300;
		yPos = p.y - 230;
		if (xPos < 0)
			xPos = 0;
		//if (yPos < 0)
		//	yPos = 0;
		if (xPos > tiles.length * Tile.TileWidth - Main.WIDTH)
			xPos = tiles.length * Tile.TileWidth - Main.WIDTH;
		if (yPos > tiles[0].length * Tile.TileHeight - Main.HEIGHT)
			yPos = tiles[0].length * Tile.TileHeight - Main.HEIGHT;
		int xx = (int) xPos >> 5;
		int yy = (int) yPos >> 5;
		for (int i = xx; i < xx + 20; i++) {
			for (int j = yy; j < yy + 16; j++) {
				if (i >= 0 && j >= 0 && i < tiles.length && j < tiles[0].length)
					if (tiles[i][j] != -1)
						t[tiles[i][j]].draw(this, i * (Tile.TileWidth)
								- ((int) xPos), j * (Tile.TileHeight)
								- ((int) yPos));
					else {
						if (j >= 10) {
							// underground

							boolean r = false, l = false, tp = false, b = false;

							if (i + 1 < tiles.length)
								if (empty(tiles[i + 1][j]))
									r = true;
							if (i - 1 >= 0)
								if (empty(tiles[i - 1][j]))
									l = true;
							if (j - 1 >= 0)
								if (empty(tiles[i][j - 1]))
									tp = true;

							if (j + 1 < tiles[0].length)
								if (empty(tiles[i][j + 1]))
									b = true;
							if(trans(tiles[i][j])){
								t[16].draw(this, i * (Tile.TileWidth)
										- ((int) xPos), j * (Tile.TileHeight)
										- ((int) yPos));
							}
							if (!l && !b)
								t[20].draw(this, i * (Tile.TileWidth)
										- ((int) xPos), j * (Tile.TileHeight)
										- ((int) yPos));
							if (!r && !b)
								t[19].draw(this, i * (Tile.TileWidth)
										- ((int) xPos), j * (Tile.TileHeight)
										- ((int) yPos));
							if (!r && !tp)
								t[18].draw(this, i * (Tile.TileWidth)
										- ((int) xPos), j * (Tile.TileHeight)
										- ((int) yPos));
							if (!l && !tp)
								t[17].draw(this, i * (Tile.TileWidth)
										- ((int) xPos), j * (Tile.TileHeight)
										- ((int) yPos));
							
						}
					}
			}
		}

		gasStation.draw(this);
		marketPlace.draw(this);
		garage.draw(this);
		for(int i = 0; i < cloud.length; i++)
		cloud[i].draw(this);
		p.draw(this);
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).draw(this, -xPos, -yPos);
		}
		// t[3].draw(this, 100, 100);
		
		//drawString("particles: " + particles.size(), 0, 0, 0xffff00ff);
		
		
		
		
		
		// render GUI
		
		
		if (p.fuelLevel / p.maxFuelLevel < 0.2f) {
			// warning
			if ((tick / 30) % 2 == 0)
				drawString("Low fuel level!", GUI_FUEL_X - 50, GUI_FUEL_Y + 50,0xffff0000);
		}
		if (p.hullPoints / p.maxHullPoints < 0.2f) {
			// warning
			if (((tick+20) / 30) % 2 == 0)
				drawString("Hull damaged!", GUI_HULL_X - 40, GUI_HULL_Y + 50,0xffff0000);
		}
		
		if(p.inventoryCount == p.maxInventoryCap){
			if ((tick / 30) % 2 == 0)
				drawString("Cargo FULL!", 280, 25, 0xffff0000);

		}
		if(p.heat){
			if (((tick+15) / 30) % 2 == 0)

			drawString("Heat warning!",250, 220, 0xffff0000);
		}
		if (atBuilding) {
			int cost = (int) ((p.maxFuelLevel - p.fuelLevel) * GAS_PRICE);
			if (buildingID == 1) {
				drawString("press x to buy gas ($" + cost + ")", 220, 270,
						0xffffff00);
			}
			if (buildingID == 2) {
				drawString("press x to sell ores!", 220, 270,
						0xffffff00);
			}
			if (buildingID == 3) {
				drawString("press x to see upgrades!", 220, 270,
						0xffffff00);
			}
		}
		// drawString("Fuel:" + p.fuelLevel + "/" + p.maxFuelLevel, 450, 450,
		// 0xff00ff00);
		if(p.fuelLevel <= 0 || p.hullPoints <= 0){
			//dead :(
			
			int tin = (int) (((deadTick/180.0)*20.0)+1);
			for(int i = 0; i < data.length; i++){
				int r, g, b;
				r = (data[i]>>16)&0xff;
				g = (data[i]>>8)&0xff;
				b = data[i]&0xff;
				//System.out.println(r);
				r = r/tin;
				g = g/tin;
				b = b/tin;
				data[i] = (0xff<<24)+(r<<16)+(g<<8)+b;
			}
		}
		
		for(int i = 0; i < animations.size(); i++){
			animations.get(i).draw(this);
		}
		
		moneyGUI.draw(this, GUI_FUEL_X+4, GUI_FUEL_Y-9);
		heatGUI.draw(this, GUI_FUEL_X+10, GUI_FUEL_Y+29);
		heatGUI_arrow.draw(this, GUI_FUEL_X+7+(int)(((heat)/100.0)*107.0), GUI_FUEL_Y+39);
		// draw fuel level
		int dpt = -(int)((p.y)/8-37);
		drawString(dpt+"m", 300, 25, 0xffffffff);
		drawString("$"+p.cash, 300, 5, 0xffffffff);
		String cg = "Cargo " + p.inventoryCount + "/" + p.maxInventoryCap;
		drawString(cg, 314-cg.length()*4, 15, 0xffffffff);
		drawRect(10, (int) (p.maxFuelLevel * (40/p.maxFuelLevel)), GUI_FUEL_X, GUI_FUEL_Y,
				0xff4B5320);
		drawRect(10, (int) (p.fuelLevel * (40/p.maxFuelLevel)), GUI_FUEL_X, GUI_FUEL_Y+(int) (p.maxFuelLevel * (40/p.maxFuelLevel))-(int) (p.fuelLevel * (40/p.maxFuelLevel)),
				0xffFFBF00);
		//draw hp
		drawRect(10, (int) (p.maxHullPoints * (40/p.maxHullPoints)), GUI_HULL_X, GUI_HULL_Y,
				0xff4b5320);
		drawRect(10, (int) (p.hullPoints * (40/p.maxHullPoints)), GUI_HULL_X, GUI_HULL_Y+(int) (p.maxHullPoints * (40.0/p.maxHullPoints))-(int) (p.hullPoints * (40.0/p.maxHullPoints)),
				0xffaa0000);
		gasGUI.draw(this, GUI_FUEL_X-5, GUI_FUEL_Y-9);
		hullGUI.draw(this, GUI_HULL_X-5, GUI_HULL_Y-9);
		
		if(updateOpen){
			
			for(int i = 0; i < 6; i++){
				if(i != updateselect0)
				upgradeGUI_Bar.draw(this, 200, 98+10*i);
				else{
					if(updateStack == 0)
						upgradeGUI_Bar_green.draw(this, 200, 98+10*i);
					else
						upgradeGUI_Bar_yellow.draw(this, 200, 98+10*i);

				}

			}
			drawString("Tank", 233-4*4, 100, 0xffffffff);
			drawString("Hull ", 233-4*4, 100+10, 0xffffffff);
			drawString("Drill", 233-4*5, 100+20, 0xffffffff);
			drawString("Cargo", 233-4*5, 100+30, 0xffffffff);
			drawString("Cooler", 233-4*6, 100+40, 0xffffffff);
			drawString("Exit", 233-4*4, 100+50, 0xffffffff);
			if(updateselect0 != 5){
				upgradeGUI_arrow.draw(this, 267, 98+10*updateselect0);

			for(int i = 0; i < levels(updateselect0)+1; i++){
				
				if(p.getSkill(updateselect0) >= i){
					if(updateselect1 == i)
						upgradeGUI_Bar_yellow.draw(this, 277, updateselect0*10+98+10*i);
					else
					upgradeGUI_Bar_green.draw(this, 277, updateselect0*10+98+10*i);

					upgradeGUI_check.draw(this, 344, updateselect0*10+98+10*i);
				}
				else if(i != updateselect1){
					if(!p.canAfford(updateselect0, i))
						upgradeGUI_Bar_red.draw(this, 277, updateselect0*10+98+10*i);
					else{
						upgradeGUI_Bar.draw(this, 277, updateselect0*10+98+10*i);
					}
				}
				else{
					upgradeGUI_Bar_yellow.draw(this, 277, updateselect0*10+98+10*i);
				}
				
				
				drawString("Tier " + (i), 306-4*5, updateselect0*10+100+i*10, 0xffffffff);
			}
			
			
			
			}
			
		}
	}

	private int levels(int s) {
		return 5;
		
	}

	private boolean trans(int i) {
		return i == ID_AIR;
	}

	private void drawRect(int width, int height, int x, int y, int col) {

		drawToArr(x, y, width, height, col);

	}

	public void drawString(String str, int x, int y, int col) {
		// System.out.println(x + ", " + y);
		// System.out.println(str);
		for (int i = 0; i < str.length(); i++) {
			drawToArr(letters[fongLegend.indexOf(str.charAt(i))], x + (i << 3),
					y, 8, 8, col);
		}
	}

	private void drawToArr(int[] data, int i, int j, int width, int height,
			int col) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (data[x + (y * width)] != 0xffff00ff
						&& i + j * Main.WIDTH + x + y * Main.WIDTH > 0
						&& i + j * Main.WIDTH + x + y * Main.WIDTH < this.data.length && i+x < Main.WIDTH)
					this.data[i + j * Main.WIDTH + x + y * Main.WIDTH] = col;
			}
		}
	}

	private void drawToArr(int i, int j, int width, int height, int col) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (i + j * Main.WIDTH + x + y * Main.WIDTH > 0
						&& i + j * Main.WIDTH + x + y * Main.WIDTH < this.data.length)
					this.data[i + j * Main.WIDTH + x + y * Main.WIDTH] = col;
			}
		}
	}
	


	public void render() {

		Graphics g = getGraphics();	
		g.drawImage(img, 0, 0, Main.WIDTH * 2, Main.HEIGHT * 2, null);

	}

	void clear() {
		for (int i = 0; i < data.length; i++) {
			data[i] = skyShades[0];
		}
	}

	public void update(boolean[] keys) {
		tick++;
		
		for(int i = animations.size()-1; i >= 0; i--){
			if(animations.get(i).alive)
				animations.get(i).update();
			else
				animations.remove(i);
		}
		
		float dpt = ((p.y)/8-37)/4;
		//System.out.println(dpt);
		heat = (int) ((dpt*4)*(1-p.radiatorPercentage));
		if(heat < 0)
			heat = 0;
		if(heat > 100)
			heat = 100;
		if(heat > 95){
			p.heat = true;
			p.damagePx.transparency = .5f;
			//p.takeDamage(.01f);
		}else
			p.heat =false;
		// xPos+=.5;
		for(int i = 0; i < cloud.length; i++)
			cloud[i].update();
		if(tick%10==0){
			if(dayTime >= 255 || dayTime <1)
				dd*=-1;
			dayTime+=dd;
		}
		if(!updateOpen)
			p.update();
		
		if(updateOpen){
			if(updateStack == 0)
				updateselect1 = -1;
		}
		for (int i = particles.size() - 1; i >= 0; i--) {
			if (!particles.get(i).alive)
				particles.remove(i);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
		}
		atBuilding = false;
		buildingID = -1;// gas
		Particle pa = new Particle(SELL_ORE_X+25+(float)(5*Math.random())+5, 277+(float)(5*Math.random()), 0xffaaaaaa);
		pa.dx = (float) -Math.random()/2f;
		pa.dy = (float) -Math.random()/1f;
		pa.transparent = true;
		particles.add(pa);
		if (p.y == 300) {
			// on grass...
			
			//marketplace clouds
			
			
			if (p.x + 16 > FUEL_STATION_X && p.x + 16 < FUEL_STATION_X + 64) {
				// System.out.println("pay");
				atBuilding = true;
				buildingID = 1;// gas
			}
			if (p.x + 16 > SELL_ORE_X && p.x + 16 < SELL_ORE_X + 64) {
				atBuilding = true;
				buildingID = 2;// sell
			}
			if (p.x + 16 > UPGRADE_X && p.x + 16 < UPGRADE_X + 64) {
				atBuilding = true;
				buildingID = 3;// garage
			}else
				updateOpen = false;

		}
		if(p.isDead || p.fuelLevel <= 0 || p.hullPoints <= 0){
			//dead :(
			p.isDead = true;
			deadTick++;
			if(deadTick > 60*3){
				//3 secs..
				respawn();
			}
			
		}
	}

	private void respawn() {
		p.respawn();
		deadTick = 0;

	}

	public void mousePressed(MouseEvent e) {
		int x = e.getX() - 8;
		int y = e.getY() - 31;

	}

	public void keyDown(boolean[] keys) {
		
			p.keyDown(keys);
			if (keys[KeyEvent.VK_G]) {
				System.out.println("GG");
				keys[KeyEvent.VK_G] = false;
				p.cash+=2000;
			}
		if (atBuilding) {
			if(updateOpen){
				if(keys[KeyEvent.VK_UP]){
					if(updateStack == 0){
						updateselect0--;
						if(updateselect0 < 0)
							updateselect0 = 0;
					}else if(updateStack == 1){
						updateselect1--;
						if(updateselect1 < 0)
							updateselect1 = 0;
					}
				}else if(keys[KeyEvent.VK_DOWN]){
					if(updateStack == 0){
						updateselect0++;
						
						if(updateselect0 > 5)
							updateselect0 = 5;
						System.out.println(updateselect0);
					}else if(updateStack == 1){
						updateselect1++;
						if(updateselect1 > levels(updateselect0))
							updateselect1 = levels(updateselect0);
					}
				}else if (keys[KeyEvent.VK_LEFT]){
					if(updateStack == 1){
						updateStack = 0;
						updateselect1 = -1;
					}
				}else if(keys[KeyEvent.VK_RIGHT]){
					if(updateStack == 0 && updateselect0 <= 4){
						updateStack = 1;
						updateselect1 = 0;
					}
				}else if(keys[KeyEvent.VK_X]){
					if(updateStack == 0){
						if(updateselect0 == 5){//exit
						updateOpen = false;
						updateselect0 = 0;
						
						}
						else{
							updateStack = 1;
							updateselect1 = 0;
						}
					}else{
						if(p.canAfford(updateselect0, updateselect1))
							p.buy(updateselect0, updateselect1);
						else
							particles.add(new StringParticle("Insufficient funds!", 460, 330, 0, -.2f, 0xffff0000));

					}
					
				}else if(keys[KeyEvent.VK_ESCAPE]){
					//System.out.println("s");
					updateOpen = false;
					updateselect1 = 0;
					updateselect0 = 0;
					updateStack = 0;
				}
			}
			else if (keys[KeyEvent.VK_X]) {
				switch(buildingID){
				case 1:
					//gas station
					int cost = (int)((p.maxFuelLevel-p.fuelLevel)*GAS_PRICE);
					if(p.cash < cost){
						particles.add(new StringParticle("Insufficient funds!", 300, 330, 0, -.2f, 0xffff0000));
					}else
					{
						p.cash-=cost;
						if(cost == 0){
							particles.add(new StringParticle("Your tank is full", FUEL_STATION_X-30, 330, 0, -.2f, 0xffff8000));

						}else
						particles.add(new StringParticle("-$"+cost, FUEL_STATION_X, 330, 0, -.2f, 0xffff0000));

						p.fuelLevel = p.maxFuelLevel;
					}
					break;
				case 2:
					//sell
					int totalVal = 0;
					for(int i = 0; i < p.inventoryCount; i++){
						totalVal+=price[p.inventory[i]-ID_COPPER];
					}
					p.inventory = new int[p.maxInventoryCap];//clean inv
					p.inventoryCount = 0;
					p.cash+=totalVal;
					if(totalVal == 0){
						particles.add(new StringParticle("You have nothing to sell", SELL_ORE_X-80, 330, 0, -.2f, 0xffff8000));

					}else
					particles.add(new StringParticle("+$"+totalVal, SELL_ORE_X, 330, 0, -.2f, 0xff008000));
					break;
					
				case 3:
					//upgrades..
					updateOpen = true;
					
					break;
				}
			}
			
		}

	}

	public static boolean keys(int key) {
		return Main.listener.keys[key];
	}

	public void removeTile(int xi, int xj) {
		int ID = tiles[xi][xj];
		if (tiles[xi][xj] != ID_BEDROCK)
			tiles[xi][xj] = -1;
		// if(ID == 3)
		// addMessage("+1 " + ID, xi<<5, xj<<5);
		if (ID >= ID_COPPER && ID <= ID_MYSTITE){
			if(p.inventoryCount < p.maxInventoryCap){
				p.inventory[p.inventoryCount++] = ID;
				addMessage("+1 " + getName(ID), xi << 5, xj << 5);
			}
		}
		if(ID == ID_LAVA){			
			p.takeDamage(20);
		}

	}

	private String getName(int iD) {
		switch (iD) {
		case ID_COPPER:
			return "Copper";
		case ID_IRON:
			return "Iron";
		case ID_RUBY:
			return "Ruby";
		case ID_SAPPHIRE:
			return "Sapphire";
		case ID_EMERALD:
			return "Emerald";
		case ID_GEMSTONE:
			return "Gem";
		case ID_DIAMOND:
			return "Diamond";
		case ID_EBONITE:
			return "Opal";
		case ID_MYSTITE:
			return "Mystite";
		default:
			return "Unknownium";
		}
	}

	private void addMessage(String string, int i, int j) {
		particles.add(new StringParticle(string, i, j, 0f, -.5f, 0xff33aa33));
	}

	public static boolean empty(int i) {
		return i == -1;
	}

	public void addMiningParticles(float x, float y) {
		int tileID = tiles[(int) x >> 5][(int) y >> 5];
		if(x >= 0 && y >= 0 && tileID != ID_AIR)
			particles.add(new Particle(x, y, t[tileID]));
	}

	public int getTileAt(int mx, int my) {
		//System.out.println(tiles.length);
		if(mx > tiles.length-1 || my > tiles[0].length-1)
			return -1;
		if(mx < 0 || my < 0)
			return ID_AIR;
		return tiles[mx][my];
	}

	public static float getWorldWidth() {
		return WORLD_WIDTH_TILE*Tile.TileWidth;
	}



	public int getDataLength() {
		return data.length;
	}



	public int[] getData() {
		return data;
	}



	public void copyToLine(int[] bs, int x, int y, int width2) {
		if(y > Main.HEIGHT || x < -Main.WIDTH || y < 0)return;
		int startIndex = 0;
		if(width2+(x+y*Main.WIDTH) > data.length){
			width2 = data.length-(x+y*Main.WIDTH);
			if(width2 < 0)return;
		}
		if(width2+x > Main.WIDTH){
			width2 = Main.WIDTH-x;
			if(width2 < 0)return;
		}
		if(x < 0){
			width2+=x;
			startIndex = -x;
			x = 0;
			if(width2 < 0)return;
		}
		System.arraycopy(bs, startIndex, data, x+y*Main.WIDTH, width2);
	}



	public void addExplosion(float f, float g) {
		animations.add(new ExplosionDrawable((int)f, (int)g));
	}



	public static void setkeys(int vkB, boolean b) {
		Main.listener.keys[vkB] = b;
		}

}
