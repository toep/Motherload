package com.toep;

public class ExplosionDrawable extends AnimatedDrawable {

	static Sheet explosion = new Sheet("explosion17.png", 160, 160);
	static Drawable[] explosionArray;//16 frames for the explosion
	static int width = 32, height = 32;
	
	public ExplosionDrawable(int x, int y) {
		super(explosionArray[0].pixels, width, height, x, y);
		//transparent = true;
		squared = false;
	}
	
	public void update(){
		super.update();
		if(animationTick > explosionArray.length-1)
			alive = false;
		System.out.println(animationTick);

	}
	
	@Override
	public void draw(Screen s) {
		if(alive){
			pixels = explosionArray[animationTick].pixels;
			super.draw(s);
		}
	}
	
	static{
		explosionArray = new Drawable[25];
		for(int i = 0; i < explosionArray.length; i++){
			explosionArray[i] = new Drawable(BitmapLoader.readFromSheet(explosion, (i*width)%explosion.getWidth(), (i*width)/explosion.getWidth()*height, 32), 32, 32);
		}
	}

}
