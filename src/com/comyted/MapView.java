package com.comyted;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.comyted.activities.ActivityMap;
import com.enterlib.conetivity.RestClient;
import com.enterlib.threading.AsyncNotifyTask;
import com.enterlib.threading.IAsyncCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MapView extends FrameLayout
			implements IAsyncCallback{

	protected Bitmap adressMap;
	private String addres;


	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * @param context
	 */
	public MapView(Context context) {
		super(context);
		init(context);
	}

	void init(Context context){
		//Get the layout inflater
		LayoutInflater lif = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(lif==null)
			return;
		//inflate the custom layout of listing 2-2
		//Use the second argument to attach the layout
		//as a child of this layout
		lif.inflate(R.layout.layout_mapview, this);		
		
		 ImageView mapView = (ImageView) findViewById(R.id.googleMapImage);
		 mapView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {							
				Intent intent = new Intent(getContext(), ActivityMap.class);
				intent.putExtra(Constants.ADDRESS, addres);
				getContext().startActivity(intent);				
			}
		});
	}
	
	public void loadMapAsync(String addres){											
		BeginDownloadMap();
		
		this.addres = addres;
		ImageView mapView = (ImageView)findViewById(R.id.googleMapImage);
		int width = mapView.getWidth();
		int height = mapView.getHeight();
		String size = width+"x"+height;
		final String url;
		try {
			url = "http://maps.googleapis.com/maps/api/staticmap?size="+size+"&markers=size:mid|color:red|" 
					+ URLEncoder.encode(addres, "UTF-8") + "&zoom=15&sensor=false";
			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();			
			return;
		}
		
		new AsyncNotifyTask(this) {						

			@Override
			protected void doInBackground() throws Exception {												
				adressMap = RestClient.downloadImage2(url);
				
			}
		}.run();
	}


	@Override
	public void operationCompleted(Exception e) {					
		if(e!=null){			
			EndDownLoadMap(null);
			return;
		}
	
		EndDownLoadMap(adressMap);		
	}
	
	
	private void BeginDownloadMap() {
		ProgressBar pbar = (ProgressBar) findViewById(R.id.googleMapProgressBar);
		pbar.setVisibility(View.VISIBLE);			
		
		ImageView mapView = (ImageView) findViewById(R.id.googleMapImage);
		mapView.setVisibility(View.INVISIBLE);
		mapView.setEnabled(false);
	}


	private void EndDownLoadMap(Bitmap map) {
		ProgressBar pbar = (ProgressBar) findViewById(R.id.googleMapProgressBar);
		pbar.setVisibility(View.INVISIBLE);			
		
		ImageView mapView = (ImageView) findViewById(R.id.googleMapImage);
		mapView.setImageBitmap(map);						
		mapView.setVisibility(View.VISIBLE);			
		mapView.setEnabled(map != null);
		
	}

}
