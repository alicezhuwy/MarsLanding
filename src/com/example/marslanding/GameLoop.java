package com.example.marslanding;

import com.example.util.Background;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.graphics.DiscretePathEffect;

public class GameLoop extends SurfaceView implements Runnable,
		SurfaceHolder.Callback, OnTouchListener {
	
	/**
	 * @author Original from John Casey 11/04/2014, modified by Alice Zhu 21/10/2015
	 * Assignment of subject ISCG7424 Mobile Application Development
	 * 
	 * Code illustrates collision detection and models gravity using synthetic time.
	 */
	
	public static final double INITIAL_TIME = 3.5;
	static final int REFRESH_RATE = 20;
	static final int GRAVITY = 1;
	//set a flag to call out the flame of spaceship
	int NOTHING =0;
	int LEFT = 1;
	int UP = 2;
	int RIGHT = 3;
	int DIRECTION;
	//set flag to end game 
	boolean gameover = false;
	double t = INITIAL_TIME;
	float x, y;
	int width;

	
	Thread main;
	Paint paint = new Paint();	
	Canvas offscreen;
	Bitmap buffer, flame, background, terrain, mainflame;
	Path path;
	
	
//	Obtain screen width 
	WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);	
	int screenWidth = wm.getDefaultDisplay().getWidth();
	int screenHeight = wm.getDefaultDisplay().getHeight();


//	Arraylist for draw the Mars path
	int xcor[] = { 0, 150, 180, 200, 298, 309, 315, 325, 410, 420,
			448, 462, 476, 550, 600, screenWidth, screenWidth, 0, 0 };
	int ycor[] = { screenHeight-180, screenHeight-220, screenHeight-250, screenHeight-290, screenHeight-290, screenHeight-240, screenHeight-250, 
			screenHeight-260, screenHeight-260, screenHeight-230, screenHeight-240, screenHeight-270, screenHeight-210, screenHeight-280, screenHeight-340,
			screenHeight-270, screenHeight, screenHeight,screenHeight-240};

	/**
	 * @author Start GameLoop.
	 */
	public GameLoop(Context context, AttributeSet attrs) {
		super(context, attrs);
		//draw Mars 
		init();
	}
	
	/**
	 * @author Start the thread.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		main = new Thread(this);

		if (main != null)
			main.start();
	}

	/**
	 * @author build the path of the rock
	 */
	public void init()
	{	
		path = new Path();

		for (int i = 0; i < xcor.length; i++) {
			path.lineTo(xcor[i], ycor[i]);		
		}	
		setOnTouchListener(this);
		getHolder().addCallback(this);	
	}

	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		width = w;	
		x = width /2;
	}

	/**
	 * @author Main game loop start here
	 */
	public void run() {
		while(true)
		{
			setUpSurface();
		}		
	}
	
	/**
	 * @author Draw the canvas and surface.
	 */
	public void setUpSurface() {
		
		while (!gameover)
		{
			Canvas canvas = null;
			SurfaceHolder holder = getHolder();
			
			synchronized (holder) {
				
				imageLOAD();
				
				//lock surface			
				canvas = holder.lockCanvas();	
				
				//draw the rock, using the path created in init()
				background = ((BitmapDrawable)getResources().getDrawable(R.drawable.background)).getBitmap();  
			    canvas.drawBitmap(background,0, 0, null); 
			    
			    paint.setAntiAlias(true);
			    
			    //draw Rock stroke with different color
			    paint.setColor(Color.BLACK);
			    paint.setStrokeWidth(10);
			    paint.setStyle(Paint.Style.STROKE);
				canvas.drawPath(path, paint);
			    
			    //fill rock with color
			    paint.setColor(Color.LTGRAY);
			    paint.setStyle(Paint.Style.FILL);
			    canvas.drawPath(path, paint);
			    
			    spaceState(canvas);
				
			}

			try {
				Thread.sleep(REFRESH_RATE);
			} 
			
			catch (Exception e) {
			}

			finally
			{
				if (canvas != null)
				{
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}	
	}
	
	private void spaceState(Canvas canvas) {
		// TODO Auto-generated method stub
		// s = ut + 0.5 gt^2
	    t = t + 0.01; // increment the parameter for synthetic time by a small amount

		// not that the initial velocity (u) is zero so I have not put ut into the code below
		 y = (int) y + (int) ((0.5 * (GRAVITY * t * t)));

	
		boolean bottomLeft = contains(xcor, ycor, x-35, y+60);
		boolean bottomRight = contains(xcor, ycor, x+35, y+60);
		
		if (bottomLeft || bottomRight)
		{
			if ((x>=245 && x<=303)||(x>=365 && x<=455))
			{	  
			    canvas.drawBitmap(buffer,x-45, y-53, null); 
			}
			
			else{						
			    canvas.drawBitmap(terrain,x-50, y, null); 
			}
			t = INITIAL_TIME; // reset the time variable
			
			gameover = true;
		}			
		else
		{									  
		    canvas.drawBitmap(buffer,x-45, y-53, null); 
		    if (DIRECTION == LEFT )
		    {						    	
		    	canvas.drawBitmap(flame,x+27, y+57, null);
		    	DIRECTION = NOTHING;
		    }			    
		    else if(DIRECTION == UP)
		    {				    	
		    	canvas.drawBitmap(mainflame,x-17, y+50, null);
		    	DIRECTION = NOTHING;
		    }
		    else if (DIRECTION == RIGHT)
		    {			    	    		
	    		canvas.drawBitmap(flame,x-27, y+57, null);
	    		DIRECTION = NOTHING;
		    }
		}
	}

	/**
	 * @author Load all the bitmaps.
	 */
	private void imageLOAD() {
		// TODO Auto-generated method stub
		buffer = ((BitmapDrawable)getResources().getDrawable(R.drawable.spaceship)).getBitmap();
		terrain = ((BitmapDrawable)getResources().getDrawable(R.drawable.terrain)).getBitmap();  
		flame = ((BitmapDrawable)getResources().getDrawable(R.drawable.sideflame)).getBitmap();
		mainflame = ((BitmapDrawable)getResources().getDrawable(R.drawable.mainflame)).getBitmap();
	}

	/**
	 * @author Check if spaceship land on the flat surface.
	 */
	public boolean contains(int[] xcor, int[] ycor, double x0, double y0) {
		
		int crossings = 0;

		for (int i = 0; i < xcor.length - 1; i++) {
			int x1 = xcor[i];
			int x2 = xcor[i + 1];

			int y1 = ycor[i];
			int y2 = ycor[i + 1];

			int dy = y2 - y1;
			int dx = x2 - x1;

			double slope = 0;
			if (dx != 0) {
				slope = (double) dy / dx;
			}

			boolean cond1 = (x1 <= x0) && (x0 < x2); // is it in the range?
			boolean cond2 = (x2 <= x0) && (x0 < x1); // is it in the reverse
														// range?
			boolean above = (y0 < slope * (x0 - x1) + y1); // point slope y - y1

			if ((cond1 || cond2) && above) {
				crossings++;
			}
		}
		return (crossings % 2 != 0); // even or odd
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * @author Destroy the surface.
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry)
		{
			try
			{
				main.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
				// try again shutting down the thread
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		x = event.getX();
		y = event.getY();
		t = 3;
		DIRECTION = NOTHING;
		gameover = false;

		return true;
	}
	
	/**
	 * @author Set restart method which is called from MainActicity.
	 */
	public void reset()
	{	
		gameover = false;
		
		x = width /2;
		y = 0;
		t = 3;
		DIRECTION = NOTHING;
	}

	
	/**
	 * @author Set left method to let spaceship turn left-up.
	 */
	public void left() {
		// TODO Auto-generated method stub
		wrapTerain();
		x = x-15;
		y = y-35;
//		leftPressed = true;
		DIRECTION = LEFT;
	}

	/**
	 * @author Set left method to let spaceship turn straight-up.
	 */
	public void up() {
		// TODO Auto-generated method stub
		y = y-50;
//		upPressed = true;
		DIRECTION = UP; 
	}

	/**
	 * @author Set left method to let spaceship turn right-up.
	 */
	public void right() {
		
		// TODO Auto-generated method stub
		wrapTerain();
		x = x+15;
		y = y-35;
//		rightPressed = true;
		DIRECTION = RIGHT;
	}
	
	private void wrapTerain() {
		// TODO Auto-generated method stub
		if(x < 90)
		{
			x = screenWidth;
		}
		else if(x > screenWidth)
		{
			x = 90;
		}
	}
}
