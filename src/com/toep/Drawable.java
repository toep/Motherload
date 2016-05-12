package com.toep;

import java.util.Arrays;

public class Drawable {
	public int[] pixels;
	protected int width;
	protected int height;
	public int[][] preppedData;//only for bitmaps that aren't the size of the screen, for faster rendering..

	boolean squared = true;//MxM pixels
	boolean transparent = false;
	float transparency = .2f;
	public Drawable(){
		//prepDataArray();
	}
	public Drawable(int[] readFromSheet, int width, int height) {
		pixels = readFromSheet;
		this.width = width;
		this.height = height;
		prepDataArray();
	}
	public void prepDataArray(){
		if(transparent)
			squared = false;
		for(int i = 0; i < pixels.length; i++){
			if(pixels[i] == 0xffff00ff) {
				squared = false;
				break;
			}
		}
		if (squared){
				preppedData = new int[height][width];
				for (int line = 0; line < height; line++)
					preppedData[line] = Arrays.copyOfRange(pixels, width * line, width * line + width);
				
			}
		
		
	}
	public void draw(Screen s, int i, int j){
		//System.out.println("drawing " + i + ", " + j + "< " + width +", " + height + " >");
		if (squared){
			if(this.pixels.length == s.getDataLength()){
				
				//we can just fill the array, only if not transparent though
				System.arraycopy(pixels, 0, s.getData(), 0, pixels.length);
				return;
			}
			//System.out.println(preppedData);
			for (int line = 0; line < height; line++){
				s.copyToLine(preppedData[line], i, j + line, width);
			}
			return;
		}
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				int dataI = i+j*Main.WIDTH+x+y*Main.WIDTH;
				if(dataI < s.data.length && dataI >= 0){
				if(pixels[x+(y*width)] != 0xffff00ff && i+x < Main.WIDTH  && j+y < Main.HEIGHT  && i+x >= 0  && j+y >= 0){
					//int ssd = i+j*Main.WIDTH+x+y*Main.WIDTH;
					int css = pixels[x+(y*width)];
					if(!transparent){
						s.data[dataI] = css;
					}
					else{
						int r, g, b, rs, gs, bs;
						r = (s.data[dataI]>>16)&0xff;
						g = (s.data[dataI]>>8)&0xff;
						b = s.data[dataI]&0xff;
						rs = (css>>16)&0xff;
						gs = (css>>8)&0xff;
						bs = css&0xff;
						//System.out.println(r + " -> " + ((r)+(255.0-r)*.2f));
						r = (int) ((r)+(rs-r)*transparency);
						g = (int) (g+(gs-g)*transparency);
						b = (int) (b+(bs-b)*transparency);
						s.data[dataI] = (0xff<<24)+(r<<16)+(g<<8)+b;
						//s.data[i+j*Main.WIDTH+x+y*Main.WIDTH];
					}

				}
				}
			}
		}
	}
	public void draw(Screen s, int i, int j, boolean bdds) {//flipped
		int xx= 0;
		for(int x = width-1; x >= 0; x--){
			
			for(int y = 0; y < height; y++){
				
				if(pixels[xx+(y*width)] != 0xffff00ff && i+x < Main.WIDTH  && j+y < Main.HEIGHT  && i+x >= 0  && j+y >= 0){
					int ssd = i+j*Main.WIDTH+x+y*Main.WIDTH;
					int css = pixels[xx+(y*width)];
					if(!transparent)
						s.data[ssd] = css;
					else{
						int r, g, b, rs, gs, bs;
						r = (s.data[ssd]>>16)&0xff;
						g = (s.data[ssd]>>8)&0xff;
						b = s.data[ssd]&0xff;
						rs = (css>>16)&0xff;
						gs = (css>>8)&0xff;
						bs = css&0xff;
						//System.out.println(r + " -> " + ((r)+(255.0-r)*.2f));
						r = (int) ((r)+(rs-r)*transparency);
						g = (int) (g+(gs-g)*transparency);
						b = (int) (b+(bs-b)*transparency);
						s.data[ssd] = (0xff<<24)+(r<<16)+(g<<8)+b;
						//s.data[i+j*Main.WIDTH+x+y*Main.WIDTH];
					}
				}
			}
			xx++;
		}
	}
	public void draw(Screen s, int i, int j, boolean bb, int k) {
		int xx= 0;
		for(int x = width-1; x >= 0; x--){
			
			for(int y = 0; y < height; y++){
				
				if(pixels[xx+(y*width)] != 0xffff00ff && i+x < Main.WIDTH  && j+y < Main.HEIGHT  && i+x >= 0  && j+y >= 0){
					int ssd = i+j*Main.WIDTH+x+y*Main.WIDTH;
					int css = pixels[xx+(y*width)];
					if(!transparent)
						s.data[ssd] = k|css;
					else{
						int r, g, b, rs, gs, bs;
						r = (s.data[ssd]>>16)&0xff;
						g = (s.data[ssd]>>8)&0xff;
						b = s.data[ssd]&0xff;
						rs = (css>>16)&0xff;
						gs = (css>>8)&0xff;
						bs = css&0xff;
						//System.out.println(r + " -> " + ((r)+(255.0-r)*.2f));
						r = (int) ((r)+(rs-r)*transparency);
						g = (int) (g+(gs-g)*transparency);
						b = (int) (b+(bs-b)*transparency);
						s.data[ssd] = (0xff<<24)+(r<<16)+(g<<8)+b;
						//s.data[i+j*Main.WIDTH+x+y*Main.WIDTH];
					}
				}
			}
			xx++;
		}
	}
	public void draw(Screen s, int i, int j, int k) {
		//System.out.println(k);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				int dataI = i+j*Main.WIDTH+x+y*Main.WIDTH;
				if(dataI < s.data.length && dataI >= 0){
				if(pixels[x+(y*width)] != 0xffff00ff && i+x < Main.WIDTH  && j+y < Main.HEIGHT  && i+x >= 0  && j+y >= 0){
					//int ssd = i+j*Main.WIDTH+x+y*Main.WIDTH;
					int css = pixels[x+(y*width)];
					if(!transparent){
						s.data[dataI] = k|css;
					}
					else{
						int r, g, b, rs, gs, bs;
						r = (s.data[dataI]>>16)&0xff;
						g = (s.data[dataI]>>8)&0xff;
						b = s.data[dataI]&0xff;
						rs = (css>>16)&0xff;
						gs = (css>>8)&0xff;
						bs = css&0xff;
						//System.out.println(r + " -> " + ((r)+(255.0-r)*.2f));
						r = (int) ((r)+(rs-r)*transparency);
						g = (int) (g+(gs-g)*transparency);
						b = (int) (b+(bs-b)*transparency);
						s.data[dataI] = (0xff<<24)+(r<<16)+(g<<8)+b;
						//s.data[i+j*Main.WIDTH+x+y*Main.WIDTH];
					}

				}
				}
			}
		}
	}
}
