package com.toep;

public class Particle extends Drawable {

	int life = 0;
	float x, y;
	float dx, dy;
	boolean alive = true;
	int col;
	
	public Particle() {
		life = 20;
	}
	
	public Particle(float x, float y) {
		this();
		this.x = x;
		this.y = y;
		width = 2;
		height = 2;
		col = 0xffabcabc;
		pixels = new int[width*height];
		dx = (float) (Math.random()*1.0)-.5f;
		dy = (float) (Math.random()*1.0)-.5f;
		transparent = true;
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = col;
		}
		prepDataArray();
	}
	public Particle(float x, float y, Tile t) {
		this();
		this.x = x;
		this.y = y;
		width = 2;
		height = 2;
		transparent = true;

		pixels = new int[width*height];
		dx = (float) (Math.random()*1.0)-.5f;
		dy = (float) (Math.random()*1.0)-.5f;
		int rand = (int) (Math.random()*t.pixels.length);
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = t.pixels[rand];
		}
		prepDataArray();
	}

	public Particle(float x, float y, int col) {
		this();
		this.x = x;
		this.y = y;
		width = 2;
		height = 2;
		this.col = col;
		transparent = true;

		pixels = new int[width*height];
		dx = (float) (Math.random()*1.0)-.5f;
		dy = (float) (Math.random()*1.0)-.5f;

		for(int i = 0; i < pixels.length; i++){
			pixels[i] = col;
		}
		prepDataArray();
	}

	void update() {
		if(!alive)
			return;
		transparency = (life)/40.0f;
		life--;
		
		if(life <= 0)
			alive = false;
		x+=dx;
		y+=dy;
		
	}
	
	public void draw(Screen s, float i, float j){
		super.draw(s, (int)(i+x), (int)(j+y));
	}
	
}
