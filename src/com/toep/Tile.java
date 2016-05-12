package com.toep;

public class Tile extends Drawable{

	//public static final float PlayerWidth = 20;
	static int TileWidth = 32;
	static int TileHeight = 32;
	public Tile(int i) {
		
		width = 32;
		height = 32;
		pixels = BitmapLoader.readFromSheet(Screen.tileSheet, (i%8)*32, (i/8)*32, 32);
		prepDataArray();
	}
	
	
}
