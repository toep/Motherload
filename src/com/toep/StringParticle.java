package com.toep;

public class StringParticle extends Particle{

	
	String string = "";
	int col;
	public StringParticle(String str, float x, float y, float dx, float dy, int col) {
		this.x = x;
		this.y = y;
		this.string = str;
		this.dx = dx;
		this.dy = dy;
		this.col = col;
		this.life = 30;
	}
	
	public void draw(Screen s, float xo, float yo) {
		s.drawString(string, (int)(x+xo), (int)(y+yo), col);
	}
}
