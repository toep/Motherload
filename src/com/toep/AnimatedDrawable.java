package com.toep;

public class AnimatedDrawable extends DrawableObject {

	int animationTick = 0;
	int tick = 0;
	boolean alive = true;
	

	public AnimatedDrawable(int[] pix, int width, int height, int x, int y) {
		
		super(pix, width, height, x, y);
	}
	
	public void update(){
		tick++;
		
		//if(tick%4==0){
			animationTick++;
		//}
	}

}
