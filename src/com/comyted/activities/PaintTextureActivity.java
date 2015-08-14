package com.comyted.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.comyted.R;

public class PaintTextureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paint);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PaintFragment()).commit();
		}
		
		getActionBar().setTitle(R.string.dibujar_firma);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.ok:
				Intent data = new Intent();
				PaintFragment fragment = (PaintFragment) getFragmentManager().findFragmentById(R.id.container);				
				
				Bitmap bitmap = fragment.getBitmap();								
				data.putExtra(PaintFragment.BITMAP, bitmap);
				setResult(RESULT_OK, data);
				finish();
				return true;				
			}	
		
		return false;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PaintFragment extends Fragment 
		implements View.OnTouchListener , View.OnClickListener, SurfaceTextureListener ,
				   View.OnKeyListener
	{

		public static final String BITMAP = "SURF_BITMAP";  
		
		TextureView surface;
		DrawingThread drawing;		
		boolean paint  = true;
		private EditText stroke;
		
		public PaintFragment() {
						
		}
		
		public TextureView getSurface(){
			return surface;
		}
		
		public Bitmap getBitmap(){
			return drawing.getBitmap();					
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_paint_texture,
					container, false);
			
			surface = (TextureView) rootView.findViewById(R.id.surface);
			surface.setOnTouchListener(this);
			surface.setSurfaceTextureListener(this);			
			
			
			Switch switcher = (Switch)rootView.findViewById(R.id.switch1);
			switcher.setOnClickListener(this);
			
			((Button)rootView.findViewById(R.id.clear)).setOnClickListener(this);
			
			getActivity().setResult(RESULT_CANCELED);
			
			stroke = (EditText)rootView.findViewById(R.id.stroke);
			stroke.setOnKeyListener(this);
			return rootView;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if(view.getId() != R.id.surface)
				return false;
			
			if(drawing == null)
				return false;
			
			int x = (int)event.getX();
			int y =(int)event.getY();
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				drawing.setDrawingPoint(x, y);				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				drawing.setDrawingPoint(-1, -1);
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE){
				drawing.setDrawingPoint(x, y);
				//drawing.draw(x, y);
			}
		
			return false;
		}
		
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
				int arg2) {
			drawing = new DrawingThread(new Surface(arg0));
			drawing.setPaint(paint);
			drawing.start();
			
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
			drawing.quit();	
			drawing = null;
			return false;
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int width,
				int height) {
			drawing.updateSize(width, height);
			
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onDestroy() {
			if(drawing!=null){
				drawing.quit();
				drawing = null;
			}
			// TODO Auto-generated method stub
			super.onDestroy();			
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.ok:
				Intent data = new Intent();
				Bitmap bitmap = drawing.getBitmap();
				data.putExtra(BITMAP, bitmap);
				getActivity().setResult(RESULT_OK, data);
				getActivity().finish();
				break;								
			case R.id.switch1:
				paint = !paint;
				drawing.setPaint(paint);
				break;
			case R.id.clear:
				drawing.Clear();
			break;
			default:
				break;
			}
			
		}

		@Override
		public boolean onKey(View arg0, int arg1, KeyEvent arg2) 
		{			
			if(arg0.getId() == R.id.stroke && stroke.getText().length() > 0)
			{
				float value = Float.parseFloat(stroke.getText().toString());
				if(value <= 0)
				{
					value = 3;			
					stroke.setText("3");				
				}
				drawing.setRadius(value);				
			}
			return false;
		}		
	
		public static class DrawingThread extends HandlerThread implements Handler.Callback
	{
		
		public static final int MSG_PAINT = 400;
		public static final int MSG_ERASE = 401;
		public static final int MSG_MOVE = 402;
		public static final int MSG_CLEAR = 403;
		
		Surface holder;
		Canvas bmpCanvas;
		Bitmap bmp;
		Paint paintFill;
		Paint paintErase;
		Handler receiver;
		int surfWidth;
		int surfHeight;		
		float radius = 3;
		Rect border;		
		boolean paint;		
		Rect srcRect;
		RectF destRect;
		Rect surfRec;
		
		public DrawingThread(Surface holder){
			super("DrawingThread");
			
			this.holder = holder;
			
			paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintFill.setColor(Color.BLACK);
			
			paintFill.setDither(false);
				
			paintErase = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintErase.setColor(Color.WHITE);
			paintFill.setDither(false);					
			
			surfRec = new Rect();
		}
		
		Bitmap getBitmap(){			
			return bmp;
		}
		
		public void setRadius(float value){
			radius =  value;			
		}
		
		public float getRadius(){
			return radius;
		}
		
		public void setDrawingPoint(int x, int y)
		{
			Message msg = Message.obtain(receiver, MSG_PAINT, x, y);
			receiver.sendMessage(msg);			
		}
		
		public void setPaint(boolean paint){
			this.paint = paint;
		}
		
		public void Clear(){			
			receiver.sendEmptyMessage(MSG_CLEAR);
		}
		
		public void UpdateSurface(){
			receiver.sendEmptyMessage(MSG_PAINT);
		}
		
		@Override
		protected void onLooperPrepared() {
			receiver = new Handler(getLooper(), this);
			receiver.sendEmptyMessage(MSG_CLEAR);			
		}
		
		public void updateSize(int width, int height) { 
			surfWidth = width; 
			surfHeight = height; 
			border = new Rect(1,1,surfWidth-1,surfHeight-1);
											
			bmp = Bitmap.createBitmap(320, 240, Config.ARGB_8888);
			bmp.prepareToDraw();
			bmpCanvas = new Canvas(bmp);
			
			srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
			destRect = new RectF(1,1,surfWidth-1, surfHeight-1);
			surfRec.set(0, 0, width, height);
		}

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_PAINT:
				draw(msg.arg1, msg.arg2);				
				break;
			case MSG_CLEAR:
				//grab render device
				Canvas c=null;
				try {
					c = holder.lockCanvas(surfRec);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfResourcesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
																			
				c.drawColor(Color.BLACK);	
				c.drawRect(border, paintErase);
				if(bmpCanvas!=null)
					bmpCanvas.drawColor(Color.WHITE);
				
				//present to view
				holder.unlockCanvasAndPost(c);	
				
				break;			
			}					
			return true;
		}
		
		

		public void draw(int x, int y) {														
			if(x >= 0 && y >= 0)
			{
				//grab render device
				Canvas c=null;
				try {
					c = holder.lockCanvas(surfRec);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfResourcesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				float tx = (float)x / (float)surfWidth;
				float ty = (float)y / (float)surfHeight;
				float tradius = (float)radius / (float)surfWidth; 
				
				Paint painter = paint? paintFill: paintErase;
				
				//c.drawCircle(x, y, radius, painter);
				if(bmp!=null){
					bmpCanvas.drawCircle(tx * bmp.getWidth(), ty * bmp.getHeight(), tradius * bmp.getWidth() , painter);				
					c.drawColor(Color.BLACK);	
					c.drawRect(border, paintErase);
					c.drawBitmap(bmp, srcRect, destRect, painter);
				}
				//present to view
				holder.unlockCanvasAndPost(c);
			}
		}
			
	}
	

	}
}
