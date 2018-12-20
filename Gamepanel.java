import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gamepanel extends JPanel implements Runnable, KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private Thread thread;
	
	private boolean running;
	private boolean right = true;
	private boolean left = false;
	private boolean up = false;
	private boolean down  = false;
	
	private Body b;
	private ArrayList<Body> snake;
	
	private food ThingSnakeEats;
	private ArrayList<food> TheThingSnakeEats;

	private Random r;
	private int x = 10;
	private int y = 10;
	private int size = 5;
	private int ticks = 0;
	
	public Gamepanel()
	{
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		addKeyListener(this);
		
		snake = new ArrayList<Body>();
		TheThingSnakeEats = new ArrayList<food>();
		r = new Random();
		
		start();
	}
	
	public void start()
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop()
	{
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void tick()
	{
		if(snake.size() == 0)
		{
			b = new Body(x, y, 10);
			snake.add(b);
		}
		
		ticks++;
		
		if(ticks > 1000000)
		{
			if (right) x++;
			if (left) x--;
			if (up) y--;
			if (down) y++;
			
			ticks = 0;
			
			b = new Body(x, y, 10);
			snake.add(b);
			
			if(snake.size() > size)
			{
				snake.remove(0);
			}
		}
		
		if(TheThingSnakeEats.size() == 0)
		{
			int x = r.nextInt(49);
			int y = r.nextInt(49);
			ThingSnakeEats = new food(x, y, 10);
			TheThingSnakeEats.add(ThingSnakeEats);
		}
		
		for(int i = 0; i < TheThingSnakeEats.size(); i++)
		{
			if (x == TheThingSnakeEats.get(i).getX() && y == TheThingSnakeEats.get(i).getY())
			{
				size++;
				TheThingSnakeEats.remove(i);
				i++;
			}
		}
		
		for(int i = 0; i < snake.size(); i++)
		{
			if(x == snake.get(i).getX() && y == snake.get(i).getY())
			{
				if(i != snake.size()-1)
				{
					JFrame loseFrame = new JFrame();
					JOptionPane.showMessageDialog(loseFrame, "YOU LOSE!", "", JOptionPane.PLAIN_MESSAGE);
					System.exit(0);
				}
			}
		}
		
		if (x < 0 || x > 49 || y < 0 || y > 49)
		{
			JFrame loseFrame = new JFrame();
			JOptionPane.showMessageDialog(loseFrame, "YOU LOSE!", "", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
	}
	
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < WIDTH/10; i++)
		{
			g.drawLine(i*10, 0, i*10, HEIGHT);
		}
		
		for(int i = 0; i < HEIGHT/10; i++)
		{
			g.drawLine(0, i*10, HEIGHT, i*10);
		}
		for(int i = 0; i < snake.size(); i++)
		{
			snake.get(i).draw(g);
		}
		for(int i = 0; i < TheThingSnakeEats.size(); i++)
		{
			TheThingSnakeEats.get(i).draw(g);
		}
	}

	@Override
	public void run() {
		while(running)
		{
			tick();
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT && !left)
		{
			right = true;
			up = false;
			down = false;
		}
		
		if(key == KeyEvent.VK_LEFT && !right)
		{
			left = true;
			up = false;
			down = false;
		}
		
		if(key == KeyEvent.VK_UP && !down)
		{
			up = true;
			left = false;
			right = false;
		}
		
		if(key == KeyEvent.VK_DOWN && !up)
		{
			down = true;
			left = false;
			right = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
