package com.toep;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Sheet {
	int[] pixels;
	private int width;
	int height;
	//Sheet sheet, items;
	public Sheet(){
	//	sheet = new Sheet("tilemap.png", 512, 512);
	//	items = new Sheet("items.png", 256, 256);
	}

	BufferedImage img;
	public Sheet(String name, int w, int h){
		try {
			img = ImageIO.read(Utils.getResourceAsStream("/resources/"+name));
			//width = img.getWidth();
			//height = img.getHeight();
			pixels = new int[w*h];
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		width = w;
		height = h;
		img.getRGB(0, 0, width, height, pixels, 0, width);
		
	}
	public int getWidth()
	{return width;}
	
}