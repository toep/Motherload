package com.toep;

public class DrawableObject extends Drawable{
	
	float x, y;
	boolean gui = false;
	
	public DrawableObject(int[] readFromSheet, int w, int h,
			int x, int y) {
		pixels = readFromSheet;
		width = w;
		height= h;
		this.x = x;
		this.y = y;
		prepDataArray();
	}
	
	public void draw(Screen s) {
		//System.out.println(gui);
		if(!gui)
			super.draw(s, (int)(Math.ceil(x-s.xPos)), (int)(Math.ceil(y-s.yPos)));
		else
			super.draw(s, (int)x, (int)y);
	}

	
	
}
