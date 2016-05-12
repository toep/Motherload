package com.toep;

import javax.swing.JFrame;

public class Main extends JFrame implements Runnable{

	
	private Screen screen;
	
	static Listener listener;
	Thread thread;
	boolean running = false;
	public static int WIDTH = 600, HEIGHT = 460;
	public Main() {
		
			
			screen = new Screen();
			add(screen);
			setSize(WIDTH*2+16, HEIGHT*2+39);
			setVisible(true);
			listener = new Listener(screen);
			addKeyListener(listener);
			addMouseListener(listener);
			addMouseMotionListener(listener);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		
			/*frame = new JFrame();
		//new Sheet();
		screen = new Screen();
		frame.add(screen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH*2+16, HEIGHT*2+39);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		listener = new Listener(screen);

		frame.addKeyListener(listener);
		frame.addMouseListener(listener);
		frame.addMouseMotionListener(listener);
		
		*/
		running = true;
		
		start();
	}
	public synchronized void start() {
		
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		screen.clear();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
				updates++;
			}
			//clear?
			render(true);
			frames++;

			if (System.currentTimeMillis() - timer >= 1000) {
				//if(!isApplet)
					setTitle("Normal | " + ("FPS: " + frames + ", UPS: " + updates));
				frames = 0;
				updates = 0;
				timer = System.currentTimeMillis();
			}
		}
	}
	private void render(boolean clear) {
		if(clear)
			screen.clear();
		screen.paint();
		
		//if(isApplet)
			//screen.repaint();
		//else
			screen.render();
	}
	private void update() {
		
		screen.update(listener.keys);
	}
	public static void main(String[] args) {
		new Main();
	}

}
