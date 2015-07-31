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
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.comyted.R;
import com.comyted.modules.sheets.SignatureManager;

public class PaintActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paint);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PaintFragment()).commit();
		}
		
		setResult(RESULT_CANCELED);
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
		PaintFragment fragment;
		
		switch (id) {
		case R.id.ok:
				Intent data = new Intent();
				fragment = (PaintFragment) getFragmentManager().findFragmentById(R.id.container);				
				
				Bitmap bitmap = fragment.getBitmap();								
				data.putExtra(PaintFragment.BITMAP, bitmap);
				setResult(RESULT_OK, data);
				finish();
				return true;
			
			case R.id.clear:
				fragment = (PaintFragment) getFragmentManager().findFragmentById(R.id.container);
				fragment.clearBitmap();
				return true;
			}	
		
		return false;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PaintFragment extends Fragment 
		implements View.OnTouchListener , View.OnClickListener, SurfaceHolder.Callback ,
				   View.OnKeyListener
	{

		public static final String BITMAP = "SURF_BITMAP";  
		
		View rootView;
		SurfaceView surface;
		DrawingThread drawing;		
		boolean paint  = true;
		private EditText stroke;
		Bitmap bmp;
		boolean clearBitmap;
		
		
		
		public PaintFragment() {
		
		}
		
		public void clearBitmap() {
			if(drawing!=null)
				drawing.Clear();			
		}

		public SurfaceView getSurface(){
			return surface;
		}
		
		public Bitmap getBitmap(){
			return drawing.getBitmap();					
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			if(savedInstanceState !=null){
				clearBitmap = false;
				bmp = savedInstanceState.getParcelable("bitmap");				
			}
			
			if(bmp == null){
				bmp = Bitmap.createBitmap(SignatureManager.SIGNATURE_WIDTH, SignatureManager.SIGNATURE_HEIGHT, Config.ARGB_8888);
				clearBitmap = true;
			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_paint,
					container, false);
			
			surface = (SurfaceView) rootView.findViewById(R.id.surface);
			surface.setOnTouchListener(this);
			surface.getHolder().addCallback(this);
			
			
			Switch switcher = (Switch)rootView.findViewById(R.id.switch1);
			//switcher.setOnClickListener(this);
			switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(drawing!=null)
						drawing.setPaint(!arg1);
				}
			});
			
			stroke = (EditText)rootView.findViewById(R.id.stroke);
			stroke.setOnKeyListener(this);
			return rootView;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if(view.getId() != R.id.surface)
				return false;
			
			int x = (int)event.getX();
			int y =(int)event.getY();
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				drawing.move(x, y);				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				//drawing.setDrawingPoint(-1, -1);
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE){
				drawing.setDrawingPoint(x, y);
				//drawing.draw(x, y);
			}			
		
			return true;
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int format, int width,
				int height) {
			if(drawing!=null){
				drawing.updateSize(width, height);
				bmp = drawing.getBitmap();
			}
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			drawing = new DrawingThread(arg0);
			drawing.setBitmap(bmp, clearBitmap);
			drawing.setPaint(paint);
			drawing.start();						
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			if(drawing!=null){
				drawing.quit();	
				drawing = null;
			}
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
	
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			outState.putParcelable("bitmap", bmp);
		}
		
	public static class DrawingThread extends HandlerThread implements Handler.Callback
	{
		
		public static final int MSG_PAINT = 400;
		public static final int MSG_ERASE = 401;
		public static final int MSG_MOVE = 402;
		public static final int MSG_CLEAR = 403;
		public static final int MSG_UPDATE_CANVAS = 404;
		
		SurfaceHolder holder;
		Canvas bmpCanvas;
		Bitmap bmp;
		Paint painter;		
		Handler receiver;
		int surfWidth;
		int surfHeight;		
		float radius = 3;
		Rect border;		
		boolean paint;		
		Rect srcRect;
		RectF destRect;
		float lastX;
		float lastY;
		boolean clearOnCreate;
		
		public DrawingThread(SurfaceHolder holder){
			super("DrawingThread");
			
			this.holder = holder;
			
			painter = new Paint(Paint.ANTI_ALIAS_FLAG);						
			
			Rect rec = holder.getSurfaceFrame();
			surfWidth = rec.width(); 
			surfHeight = rec.height(); 
			border = new Rect(1,1,surfWidth-1,surfHeight-1);
			
			destRect = new RectF(1,1,surfWidth-1, surfHeight-1);
		}
		
		Bitmap getBitmap(){			
			return bmp;
		}
		
		void setBitmap(Bitmap bitmap, boolean clear){
			clearOnCreate = clear;
			bmp = bitmap;
			bmp.prepareToDraw();
			bmpCanvas = new Canvas(bmp);			
			srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());			
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
		
		@Override
		protected void onLooperPrepared() {
			receiver = new Handler(getLooper(), this);
			if(clearOnCreate)
				receiver.sendEmptyMessage(MSG_CLEAR);
			else{
				receiver.sendEmptyMessage(MSG_UPDATE_CANVAS);
			}
		}
		
		public void updateSize(int width, int height) { 
			surfWidth = width; 
			surfHeight = height; 
			border = new Rect(1,1,surfWidth-1,surfHeight-1);
											
//			bmp = Bitmap.createBitmap(320, 240, Config.ARGB_8888);
//			bmp.prepareToDraw();
//			bmpCanvas = new Canvas(bmp);			
//			srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
			
			destRect = new RectF(1,1,surfWidth-1, surfHeight-1);
		}

		@Override
		public boolean handleMessage(Message msg) {
			Canvas c;
			switch (msg.what) {
			case MSG_PAINT:
				draw(msg.arg1, msg.arg2);					
				break;
			case MSG_CLEAR:
				//grab render device
				c = holder.lockCanvas();																																			
				
				bmpCanvas.drawColor(Color.WHITE);
				
				c.drawColor(Color.BLACK);				
				//draw borders
				painter.setColor(Color.WHITE);				
				c.drawRect(border, painter);	
				
				//present to view
				holder.unlockCanvasAndPost(c);	
				
				break;
			case MSG_MOVE:
				lastX = ((float)msg.arg1 / (float)surfWidth) * bmp.getWidth();
				lastY = ((float)msg.arg2 / (float)surfHeight) * bmp.getHeight();
				float tradius = ((float)radius / (float)surfWidth) * bmp.getWidth();				
				drawCircle(lastX, lastY, tradius);				
												
				c = holder.lockCanvas();
				c.drawColor(Color.BLACK);	
				painter.setColor(Color.WHITE);
				c.drawRect(border, painter);
				
				painter.setColor(paint?Color.BLACK:Color.WHITE);
				c.drawBitmap(bmp, srcRect, destRect, painter);
				holder.unlockCanvasAndPost(c);
				break;
			case MSG_UPDATE_CANVAS:
				updateCanvas();
				break;
			}		
			
			return true;
		}
		
		public void move(int x, int y){
			Message msg = Message.obtain(receiver, MSG_MOVE, x, y);
			receiver.sendMessage(msg);			
		}

		public void draw(int x, int y) {														
			if(x >= 0 && y >= 0)
			{
						
				
				float tx = ((float)x / (float)surfWidth) * bmp.getWidth();
				float ty = ((float)y / (float)surfHeight)* bmp.getHeight() ;
				float tradius = ((float)radius / (float)surfWidth) * bmp.getWidth();
				
				//drawCircle(tx, ty, tradius);
				
				drawLine(tx, ty, tradius + tradius);
				
				drawCircle(lastX, lastY, tradius);
				
				//c.drawCircle(x, y, radius, painter);
				//painter.setStrokeWidth(tradius* tradius);
				//bmpCanvas.drawCircle(tx * bmp.getWidth(), ty * bmp.getHeight(), tradius * bmp.getWidth() , painter);
				//bmpCanvas.drawLine(lastX, lastY, tx, ty, painter);
				
//				float dx = x - lastX;
//				float dy = y - lastY;
//				float halfRadius = radius * 0.5f;
//				float lenght = (float)Math.sqrt(dx*dx + dy*dy);
//				float dxN = dx /lenght;
//				float dyN = dy / lenght;
//				float steps = (float) ((float)Math.sqrt(dx*dx + dy*dy)/ (radius * 0.5));												
//				for (int i = 0; i < steps; i++) {
//					x = (int) ((float)lastX + dxN * halfRadius);
//					y = (int) ((float)lastY + dyN * halfRadius);
//					
//					tx = (float)x / (float)surfWidth;
//					ty = (float)y / (float)surfHeight;
//					tradius = (float)radius / (float)surfWidth; 							
//					
//					//c.drawCircle(x, y, radius, painter);
//					bmpCanvas.drawCircle(tx * bmp.getWidth(), ty * bmp.getHeight(), tradius * bmp.getWidth() , painter);
//										
//				}
				
				//grab render device
					
				updateCanvas();
				
				lastX = tx;
				lastY = ty;
								
			}					
		}

		private void updateCanvas() {
			Canvas c = holder.lockCanvas();	
			c.drawColor(Color.BLACK);
			painter.setColor(Color.WHITE);
			c.drawRect(border, painter);
			c.drawBitmap(bmp, srcRect, destRect, painter);							
			//present to view
			holder.unlockCanvasAndPost(c);
		}
		
		public void drawCircle(float x, float y ,float radius){			
			painter.setColor(paint?Color.BLACK:Color.WHITE);
			painter.setStrokeWidth(radius* radius);
			bmpCanvas.drawCircle(x, y, radius , painter);
		}
		
		public void drawLine(float x, float y, float stroke){
			painter.setColor(paint?Color.BLACK:Color.WHITE);						 													
			painter.setStrokeWidth(stroke);
			bmpCanvas.drawLine(lastX, lastY, x, y, painter);
		}
			
	}

	}
}
