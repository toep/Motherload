package com.toep;

import java.awt.event.KeyEvent;

public class Player extends Drawable {

	float x, y;
	float dx, dy;
	float term = 12.8f;
	float miningX, miningY;
	//float miningSpeed = .4f;
	int miningTick = 0;
	int dir = 1;
	int animationTick = 0;
	int cash = 0;

	boolean ann = false;// animation bool
	boolean mining = false;
	boolean wallLeft = false, wallRight = false, onGround = false;
	Drawable drill, drillDown, hull;
	float collisionHeight = 18;
	float collisionWidth = 22;
	private boolean drillingDown;
	Drawable[] hullDrawables = new Drawable[6];
	Drawable[][] drillDrawables = new Drawable[6][2];
	
	int hullLevel = 0;//0-5
	int tankLevel = 0;
	int cargoLevel = 0;
	int engineLevel = 0;
	
	final int[] hullList = new int[]{17, 30, 50, 80, 120, 180};
	final int[] fuelTankList = new int[]{15, 25, 40, 60, 100, 150};
	final int[] cargoCapacityList = new int[]{10, 15, 25, 40, 70, 120};
	final float[] enginePowerList = new float[]{.2f, .23f, .26f, .3f, .32f, .35f};
	final float[] radiatorList = new float[]{.25f, .583f, .75f, .875f, .91f, .94f};

	boolean[][] owned = new boolean[][]{{true, false, false, false, false, false}, {true, false, false, false, false, false}, {true, false, false, false, false, false}, {true, false, false, false, false, false}, {true, false, false, false, false, false}};
	int[] cashCost = new int[]{0, 750, 2000, 5000, 20000, 100000};
	
	float maxHullPoints = hullList[hullLevel];
	float hullPoints = maxHullPoints;
	
	float maxFuelLevel = fuelTankList[tankLevel];
	float fuelLevel = maxFuelLevel;//start out refueled
	
	private float enginePower = enginePowerList[engineLevel];

	
	int maxInventoryCap = cargoCapacityList[cargoLevel];
	int inventoryCount = 0;
	int[] inventory = new int[maxInventoryCap];
	
	Screen screen;
	boolean isDead;
	private int damageAniTick;
	private boolean takingDamage;
	Drawable damagePx;
	private int radLevel;
	public float radiatorPercentage;
	private int spawnX, spawnY;
	boolean heat = false;
	
	Player(int x, int y, Screen screen) {
		
		super(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 0, 22, 20), 22, 20);
		hullDrawables[0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 0, 22, 20), 22, 20);
		hullDrawables[1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 20, 22, 20), 22, 20);
		hullDrawables[2] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 40, 22, 20), 22, 20);
		hullDrawables[3] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 60, 22, 20), 22, 20);
		hullDrawables[4] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 80, 22, 20), 22, 20);
		hullDrawables[5] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 100, 22, 20), 22, 20);

		hull = hullDrawables[hullLevel];

		drillDrawables[0][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 0, 22,20), 22, 20);
		drillDrawables[0][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 0,22, 20), 22, 20);
		drillDrawables[1][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 20, 22,20), 22, 20);
		drillDrawables[1][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 20,22, 20), 22, 20);
		drillDrawables[2][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 40, 22,20), 22, 20);
		drillDrawables[2][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 40,22, 20), 22, 20);
		drillDrawables[3][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 60, 22,20), 22, 20);
		drillDrawables[3][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 60,22, 20), 22, 20);
		drillDrawables[4][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 80, 22,20), 22, 20);
		drillDrawables[4][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 80,22, 20), 22, 20);
		drillDrawables[5][0] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 22, 100, 22,20), 22, 20);
		drillDrawables[5][1] = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 44, 100,22, 20), 22, 20);

		drill = drillDrawables[0][0];
		drillDown = drillDrawables[0][1];
		
		this.x = x;
		this.y = y;
		this.spawnX = x;
		this.spawnY = y;
		damagePx = new Drawable(BitmapLoader.readFromSheet(Screen.playerSheet, 0, 20, 22,20), 22, 20);
		damagePx.transparent = true;
		
		this.screen = screen;
	}

	public void draw(Screen screen) {
		// System.out.println(y);
		// draw(screen, (int)x, (int)y);
		int offs = ann?2:1;
		int ext = miningTick<10?miningTick+8:18;
		if (dir == 1) {
			
				
				if (!drillingDown)
					drill.draw(screen, (int) (-screen.xPos + x) + 21,
							(int) (-screen.yPos + y + offs));
				else
					drillDown.draw(screen, (int) (-screen.xPos + x+1),
							(int) (-screen.yPos + y + offs+ext));
				hull.draw(screen, (int) (-screen.xPos + x),
						(int) (-screen.yPos + y + offs));
				
				if(heat){
					hull.draw(screen, (int) (-screen.xPos + x),
							(int) (-screen.yPos + y + offs),  (100)|0xff110000);
					if (!drillingDown)
						drill.draw(screen, (int) (-screen.xPos + x) + 21,
								(int) (-screen.yPos + y + offs),  (100)|0xff110000);
					else
						drillDown.draw(screen, (int) (-screen.xPos + x+1),
								(int) (-screen.yPos + y + offs+ext),  (100)|0xff110000);
				}
				if(takingDamage)
					hull.draw(screen, (int) (-screen.xPos + x),
						(int) (-screen.yPos + y + offs),  100|0xff0000);
			
		
		} else {
			
				
				if (!drillingDown)
					drill.draw(screen, (int) (-screen.xPos + x) - 21,
							(int) (-screen.yPos + y + offs), true);
				else
					drillDown.draw(screen, (int) (-screen.xPos + x+1),
							(int) (-screen.yPos + y + offs+ext), true);
				hull.draw(screen, (int) (-screen.xPos + x),
						(int) (-screen.yPos + y + offs), true);
				
				if(heat){
					hull.draw(screen, (int) (-screen.xPos + x),
							(int) (-screen.yPos + y + offs), true, (100)|0xff110000);
					if (!drillingDown)
						drill.draw(screen, (int) (-screen.xPos + x) - 21,
								(int) (-screen.yPos + y + offs), true, (100)|0xff110000);
					else
						drillDown.draw(screen, (int) (-screen.xPos + x+1),
								(int) (-screen.yPos + y + offs+ext), true, (100)|0xff110000);
				}
				if(takingDamage)
					hull.draw(screen, (int) (-screen.xPos + x),
							(int) (-screen.yPos + y + offs), true, 100|0xff0000);
		}
		
		//render GUI
		

	}

	public void update() {
		animationTick++;
		
		if(takingDamage){
			damageAniTick--;
			
			if(damageAniTick == 0)
				takingDamage = false;
		}
		int smokeColor = (hullPoints/maxHullPoints) < .2f?0xff332222:0xffaaaaaa;
		Particle p = new Particle(x+(float)(5*Math.random())+5, y+(float)(5*Math.random()), smokeColor);
		p.dx = (float) -Math.random()/2f;
		p.dy = (float) -Math.random()/1f;
		p.transparent = true;
		screen.particles.add(p);
		
		if (animationTick % 15 < 2){
			ann = false;
		}
		else
			ann = true;
		if (!mining) {

			y += dy;
			x += dx;

			dx *= .95;

			dy += .14f;
		}
		fuelLevel-=.001f;
		// float ady = dy>0?dy:-dy;
		// float adx = dx>0?dx:-dx;
		if (dy > term)
			dy = term;
		if (dy < -term+5)
			dy = -term+5;
		// System.out.println((y-height) + ",  " +
		// (Screen.tiles[0].length*Tile.TileHeight));
		if (y > screen.tiles[0].length * Tile.TileHeight - height) {
			dy = 0;
			System.out.println("bottom");
			y = screen.tiles[0].length * Tile.TileHeight - height;
		}
		
		if (!mining) {
			
				if (!Screen
						.empty(screen.getTileAt(((int) (x + 1) >> 5),((int) (y + height) >> 5)))
						|| (((int) (x + Tile.TileWidth - width) >> 5) + 1 < screen.tiles.length && !Screen
								.empty(screen.getTileAt((((int) (x + width - 1) >> 5)),((int) (y + height) >> 5))))) {
					if(dy > 5){
						System.out.println(dy);
						if(dy < 5.3)
							takeDamage(3);
						else if(dy <=6.6)
							takeDamage(4);
						else if(dy <=7.0)
							takeDamage(5);
						else if(dy <=8.0)
							takeDamage(6);
						else if(dy <=9.6)
							takeDamage(7);
						else
							takeDamage(8);
						
					}
					dy = 0;
					y = ((((int) (y + height) >> 5)) << 5) - height;
					onGround = true;
					// System.out.println("ground");

				} else
					onGround = false;

				if (!Screen
						.empty(screen.getTileAt(((int) (x + 1) >> 5),((int) (y) >> 5)))
						|| !Screen
								.empty(screen.getTileAt(((int) (x + width - 1) >> 5),((int) (y) >> 5)))) {
					dy = 0;
					y = (((int) (y + 15) >> 5) << 5);
					//System.out.println("ceil");
				}
				if (!Screen
						.empty(screen.getTileAt(((int) (x + dx) >> 5),((int) (y + 2) >> 5)))
						|| !Screen
								.empty(screen.getTileAt(((int) (x + dx) >> 5),((int) (y
										+ collisionHeight - 2) >> 5)))) {
					x = (((int) (x + 2) >> 5) << 5);
					dx = 0;
					wallLeft = true;
					//System.out.println("left");

				} else
					wallLeft = false;
				if (!Screen
						.empty(screen.getTileAt(((int) (x + width + dx) >> 5),((int) (y + 2) >> 5)))
						|| !Screen
								.empty(screen.getTileAt(((int) (x + width + dx) >> 5),((int) (y - 2 + height) >> 5)))) {
					x = (((int) (x + 2) >> 5) << 5) + 32 - width;
					dx = 0;
					wallRight = true;
					//System.out.println("right");

				} else
					wallRight = false;

				if(isDead)return;

			if (Screen.keys(KeyEvent.VK_LEFT)) {
				dx -= enginePower;
				int mx =((int) (x - 16) >> 5);
				int my = ((int) y >> 5);
				int id = screen.getTileAt(mx, my);
				if(canMine(id))
				if (wallLeft && onGround) {
					startMining((((int) (x - 16) >> 5) << 5) + 16,
							(((int) y >> 5) << 5) + 22, id);
					// .tiles[((int) (x - 16) >> 5)][((int) y >> 5)] = -1;
				}
				dir = -1;
			}
			if (Screen.keys(KeyEvent.VK_RIGHT)) {
				dx += enginePower;
				int mx =((int) (x + width + 16) >> 5);
				int my = ((int) y >> 5);
				int id = screen.getTileAt(mx, my);
				if(canMine(id))
				if (wallRight && onGround) {
					startMining((((int) (x + width + 16) >> 5) << 5) + 16,
							(((int) y >> 5) << 5) + 22, id);

				}
				dir = 1;
			}
			if (Screen.keys(KeyEvent.VK_UP)) {
				dy -= .32f;
				fuelLevel-=.003f;
			}
			if (Screen.keys(KeyEvent.VK_DOWN) && onGround) {
				int dxx = (((int) (x + 16) >> 5) << 5) + 16;
				int dyy = ((((int) y >> 5) + 1) << 5) + 16;
				int id = screen.getTileAt(dxx>>5,dyy>>5);
				if(canMine(id)){
					startMining(dxx,dyy, id);
					drillingDown = true;
				}
				// Screen.removeTile(((int) (x + 16) >> 5), ((int) y >> 5) + 1);
			}
			if(Screen.keys(KeyEvent.VK_B)){
				Screen.setkeys(KeyEvent.VK_B, false);
				//bomb? :)
				for(int i = -1; i <= 1; i++){
					for(int j = -1; j <= 1; j++){
						screen.removeTile(((int)(p.x)>>5)+i, ((int)(p.y)>>5)+j);
						screen.addExplosion(p.x+i*32, p.y+j*32);

					}
				}
			}
		} else {
			int parx = 0;
			int pary = 10;
			if(dir == 1)
				parx = 32;
			else
				parx = -11;
			if(drillingDown){
				parx = 11;
				pary = 32;
			}
			screen.addMiningParticles(x+parx, y+pary);
			miningTick++;
			fuelLevel-=.013f;
			if ((int) x + 10 < (int) miningX)
				x += enginePower*2;
			else if ((int) x + 10 > (int) miningX)
				x -= enginePower*2;
			if ((int) y + 10 < (int) miningY)
				y += enginePower*2;
			else if ((int) y + 10 > (int) miningY)
				y -= enginePower*2;
			if (x + 10 == miningX && y + 10 == miningY
					|| miningTick > 1 / (enginePower*(1+enginePower*2)*2) * 20) {
				// done mining
				mining = false;
				drillingDown = false;
				screen.removeTile((int) miningX >> 5, (int) miningY >> 5);
			}
		}
		if (x < 0) {
			x = 0;
			dx = 0;
		}
		if (x > (screen.tiles.length - 1) * Tile.TileWidth) {
			x = (screen.tiles.length - 1) * Tile.TileWidth;
			dx = 0;
		}
	}

	private boolean canMine(int id) {
		// TODO Auto-generated method stub
		return id != screen.ID_AIR && id != screen.ID_BEDROCK && id != screen.ID_ROCK;//air bedrock and stone
	}

	private void startMining(int i, int j, int id) {
		miningX = i;
		miningY = j;
		miningTick = 0;
		mining = true;
		if(id == screen.ID_LAVA)
			takeDamage(20);
		if(heat){
			takeDamage(4);
		}
	}

	public void keyDown(boolean[] keys) {

	}

	public void takeDamage(float i) {
		damageAniTick = 20;
		takingDamage = true;
		hullPoints-=i;
		
	}

	public int getSkill(int d) {
		if(d == 0){
			return tankLevel;
		} else if(d == 1){
			return hullLevel;
		}else if(d == 2){
			return engineLevel;
		}else if(d == 3){
			return cargoLevel;
		}else if(d == 4){
			return radLevel;
		}
		return 0;
	}

	public void buy(int type, int lv) {
		if(type == 0){
			tankLevel = lv;
			maxFuelLevel = fuelTankList[tankLevel];
			fuelLevel = maxFuelLevel;
		} else if(type == 1){
			hullLevel = lv;
			maxHullPoints = hullList[hullLevel];
			hullPoints = maxHullPoints;
		}else if(type == 2){
			engineLevel = lv;
			enginePower = enginePowerList[engineLevel];
		}else if(type == 3){
			cargoLevel = lv;
			maxInventoryCap = cargoCapacityList[cargoLevel];
			int[] tempInv = new int[inventory.length];
			for(int i = 0; i < tempInv.length; i++){
				tempInv[i] = inventory[i];
			}
			inventory = new int[maxInventoryCap];
			for(int i = 0; i < tempInv.length; i++){
				inventory[i] = tempInv[i];
			}
		}else if(type == 4){
			radLevel = lv;
			radiatorPercentage = radiatorList[radLevel];
		}
		if(!owned[type][lv]){
			cash-=cashCost[lv];
			screen.particles.add(new StringParticle("Success! (-$"+cashCost[lv]+")", 460, 330, 0, -.2f, 0xff008000));
			for(int i = lv; i >= 0; i--)
				owned[type][i] = true;
		}
		else{
			screen.particles.add(new StringParticle("Succesfully equipped!", 460, 330, 0, -.2f, 0xff008000));

		}
		updateLevels();
	}

	private void updateLevels() {
		hull = hullDrawables[hullLevel];
		drill = drillDrawables[engineLevel][0];
		drillDown = drillDrawables[engineLevel][1];
		/*for(int i = 0; i < owned.length; i++){
			for(int j = 0; j < owned[0].length; j++){
				System.out.print(owned[i][j] + " ");
			}
			System.out.println();
		}*/
	}

	public boolean canAfford(int updateselect0, int updateselect1) {
		return cash >= cashCost[updateselect1] || owned[updateselect0][updateselect1];
		
	}

	public void respawn() {
		x = spawnX;
		y = spawnY;
		inventory = new int[maxInventoryCap];
		inventoryCount = 0;
		isDead = false;
		animationTick = 0;
		fuelLevel = maxFuelLevel;
		mining = false;
		miningTick = 0;
		hullPoints = maxHullPoints;
		dx = 0;
		dy = 0;
	}
}
