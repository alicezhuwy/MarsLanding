package com.example.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import com.example.marslanding.R;

/**
 * Class to create the background of the game
 * 
 * @author Richard
 * 
 */
public class Background 
{
	private Resources mResources;
	private Canvas canvas;

	private Bitmap backGround;

	/**
	 * Constructor
	 * 
	 * @param resources
	 *            Resource to load content from files
	 */
	public Background(Canvas canvas, Resources resources)
	{
		this.canvas = canvas;
		this.mResources = resources;

		load();
	}

	public void load()
	{
		// load background tile from file
		backGround = BitmapFactory.decodeResource(mResources,
				R.drawable.background);
		
		System.err.println("Background: " + backGround.getHeight());		

	}

	public void draw()
	{
		// get size of canvas
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		// for loops to fill the screen with tiles
		for(int i = 0; i < height; i += backGround.getHeight())
		{
			for(int j = 0; j < width; j += backGround.getWidth())
			{
				// draw tile
				canvas.drawBitmap(backGround, j, i, null);
			}
		}		
	}


}

