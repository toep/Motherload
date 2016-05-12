package com.toep;



public class BitmapLoader {
	//static int mapSize = Tile.SIZE;
	//int size;
	public static int[] readFromSheet(Sheet s, int x, int y, int size){
		if(s == null){
			System.err.println("Sheet not loaded correctly!");
		}
		int[] ans = new int[size*size];
		for(int j = 0; j < size; j++){
			for(int i = 0; i < size; i++){
				ans[i+j*size] = s.pixels[i+j*s.getWidth()+x+y*s.getWidth()];
			}
		}
		return ans;
	}

	public static int[] readFromSheet(Sheet s, int x, int y, int w, int h){
		if(s == null){
			System.err.println("Sheet not loaded correctly!");
		}
		int[] ans = new int[w*h];
		for(int j = 0; j < h; j++){
			for(int i = 0; i < w; i++){
				ans[i+j*w] = s.pixels[i+j*s.getWidth()+x+y*s.getWidth()];
			}
		}
		return ans;
	}
}
