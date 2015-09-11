package com.comyted.activities;

import java.util.ArrayList;
import java.util.List;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.R.id;
import com.comyted.R.layout;
import com.comyted.R.menu;
import com.enterlib.DialogUtil;
import com.enterlib.app.PresentUtils;
import com.enterlib.threading.AsyncManager;
import com.enterlib.threading.IAsyncInvocator;
import com.enterlib.threading.IWorkPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class ActivityMap extends FragmentActivity {

	Geocoder geocoder;
	String addressString;
	LatLng coordinates;
	List<Address> adresses = new ArrayList<Address>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		
		addressString = getIntent().getStringExtra(Constants.ADDRESS);
		geocoder = new Geocoder(this);
		
		AsyncManager.InvokeAsync(new IAsyncInvocator() {
						
			@Override
			public void OnAsyncOperationException(Exception e) {
				DialogUtil.showErrorDialog(ActivityMap.this, e.getMessage());				
			}
			
			@Override
			public void OnAsyncOperationComplete() {
				if(adresses == null || adresses.size() == 0){
					DialogUtil.showErrorDialog(ActivityMap.this, "No se pudieron determinar las coordenadas");
					return;
				}
				
			
				GoogleMap map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
				if(map != null){
				
					if(adresses.size() > 0){					
						Address addr = adresses.get(0);
						
						coordinates = new LatLng(addr.getLatitude(), addr.getLongitude());
						MarkerOptions marker = new MarkerOptions()
						.position(coordinates)
						.title(addressString);		
						
						//adds a marker to the location
						map.addMarker(marker);
						
						//move the camera to the location					
						CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(coordinates)
							.zoom(12)
							.build();
						map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	
					}
					else{
						map.clear();
					}				
				}
				
			}
			
			@Override
			public void DoAsyncOperation() throws Exception {
					try{
					if(Geocoder.isPresent()){
						//get the latitud and longitud from the adress		
						adresses = geocoder.getFromLocationName(addressString, 1);
					}else{
						adresses = new ArrayList<Address>();					
					}
				}
				catch(Exception e){				
					adresses = new ArrayList<Address>();
					Log.d("ActivityMap", e.getMessage(), e);
				}
					
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

}
