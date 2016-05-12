package com.toep;

public class Cloud extends DrawableObject {
	public float speed;
	public Cloud(int[] readFromSheet, int w, int h) {
		super(readFromSheet, w, h, 0,0);
		respawn();
		x = (float) (Math.random()*Screen.getWorldWidth());
	}
	public void update() {
		x+=speed;
		if(x+width < 0 && speed < 0)
			respawn();
		if(x > Screen.getWorldWidth() && speed > 0)
			respawn();
	}
	private void respawn() {
		// TODO Auto-generated method stub
		speed = (float) (Math.random()-.5f);;
		if(speed < 0){
			x = Screen.getWorldWidth();
		}else
			x = -width;
		y = (float) (-Math.random()*2200+200);
	}
	
	
	
}
