package com.example.marslanding;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {
    private GameLoop gameLoop;
    private ImageButton leftBtn, upBtn, rightBtn, restartBtn;
    private TextView energyBar;
    private int fullEnergy = 1000, sideEnergy = 100, mainEnergy = 150 ;
    

	/**
	 * @author John Casey 11/04/2014, modified by Alice Zhu 21/10/2015
	 * Assignment of subject ISCG7424 Mobile Application Development
	 * 
	 * Code illustrates collision detection and models gravity using synthetic time.
	 */
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main); // set the content view or our widget lookups will fail
        
        gameLoop = (GameLoop)findViewById(R.id.gameLoop);
        
        //set up restart button
        restartBtn = (ImageButton)findViewById(R.id.btnRestart);
        restartBtn.setOnClickListener(this);
        
        //set up left button
        leftBtn = (ImageButton)findViewById(R.id.btnLeft);
		leftBtn.setOnClickListener(this);
		
		//set up right button
		rightBtn = (ImageButton)findViewById(R.id.btnRight);
		rightBtn.setOnClickListener(this);
		
		//set up up button
		upBtn = (ImageButton)findViewById(R.id.btnUp);
		upBtn.setOnClickListener(this);
		
		energyBar = (TextView)findViewById(R.id.energyBar);
    }
		


		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if(view.getId() == R.id.btnRestart){	
				gameLoop.gameover = false;
				fullEnergy = 1000;
				energyBar.setText("Energy: " + String.valueOf(fullEnergy));
				gameLoop.reset();
				gameLoop.invalidate();			
			}

			if(view.getId() == R.id.btnLeft){
				gameLoop.left();
				
			}
			if(view.getId() == R.id.btnUp){
				gameLoop.up();
				
			}
			if(view.getId() == R.id.btnRight){
				gameLoop.right();
				
			} 
			energyCaculation(view);
		}
		

		/**
		 *Fuel caculation
		 */
		public void energyCaculation(View view){
			if(view.getId() == R.id.btnLeft || view.getId() == R.id.btnRight){
				fullEnergy = fullEnergy - sideEnergy;
			}
			else if (view.getId() == R.id.btnUp){
				fullEnergy = fullEnergy - mainEnergy;
			}
			if(fullEnergy <= 0)
			{
				gameLoop.gameover = true;
				energyBar.setText("Oops!!! "
						+ "Out of Energu!!!");
			}
			else
			{
				energyBar.setText("Energy: " + String.valueOf(fullEnergy));
			}
		}
		

}