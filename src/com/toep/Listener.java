package com.toep;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Listener implements MouseListener, MouseMotionListener, KeyListener{

	boolean keys[] = new boolean[256];
	Screen screen;
	public int mouseX;
	public int mouseY;
	
	public Listener(Screen s) {
		screen = s;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() > 255)
			return;
		keys[e.getKeyCode()] = true;
		screen.keyDown(keys);
	}

	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() > 255)
			return;
		keys[e.getKeyCode()] = false;
	}

	
	public void keyTyped(KeyEvent arg0) {
	}

	
	public void mouseDragged(MouseEvent arg0) {
	}

	
	public void mouseMoved(MouseEvent e) {
		//screen.mouseMoved(e);
		mouseX = e.getX();
		mouseY = e.getY();
	}

	
	public void mouseClicked(MouseEvent arg0) {
	}

	
	public void mouseEntered(MouseEvent arg0) {
	}

	
	public void mouseExited(MouseEvent arg0) {
	}

	
	public void mousePressed(MouseEvent e) {
		screen.mousePressed(e);
	}

	
	public void mouseReleased(MouseEvent arg0) {
	}

}
