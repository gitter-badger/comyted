/**
 * 
 */
package com.comyted;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.util.Log;

import com.comyted.conectivity.GetUsersClient;
import com.comyted.models.AppUser;
import com.comyted.modules.admin.IUserManager;
import com.comyted.modules.admin.UsersManager;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.generics.IObserver;
import com.enterlib.generics.ObservableValue;

/**
 * @author ansel
 *
 */
public class MainApp extends Application 
{
	public final static boolean TEST = false;
	public static boolean PROXY = false;
	public static long SLEEP_TIME = 5000;
	
	boolean initialized;
	static MainApp instance;	
	
	public void setConfigValues(ConfigValues configValues) {
		this.configValues.setValue(configValues);
	}
	
	ObservableValue<AppUser> currentUser  = new ObservableValue<AppUser>();
	ObservableValue<ConfigValues> configValues = new ObservableValue<ConfigValues>();
		
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;		
		
	}
	
	//***** properties ********************
	public static MainApp getInstance() {
		return instance;
	}	
	
	public void setConfigValuesObserver(IObserver<ConfigValues>observer){
		configValues.addObserver(observer);
	}
	
	public void removeConfigValuesObserver(IObserver<ConfigValues>observer){
		this.configValues.removeObserver(observer);
	}
	
	public ConfigValues getConfigValues() {
		return configValues.getValue();
	}	
	
	public static AppUser getCurrentUser(){
		return instance.currentUser.getValue();
	}
	
	public static void setCurrentUser(AppUser user){
		instance.currentUser.setValue(user);
	}
	
	public static void addCurrentUserObserver(IObserver<AppUser>observer){
		instance.currentUser.addObserver(observer);
	}
	
	public static void removeCurrentUserObserver(IObserver<AppUser>observer){
		instance.currentUser.removeObserver(observer);
	}
	
	
	public static ProgressDialog getProgressDialog(Activity act, int stringID){
		return getProgressDialog(act, act.getApplicationContext().getString(stringID));
	}	
	
	public static ProgressDialog getProgressDialog(Activity act, String message){
		ProgressDialog progresbar = new ProgressDialog(act);
		progresbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progresbar.setMessage(message);
		progresbar.setCancelable(true);
		return progresbar;		
	}	
	
		

	@Override
	public void onTerminate()
	{	
		instance = null;
		try 
		{
			//automatic logout when the application finish
			currentUser.clearObservers();
			Log.d("MainApp", "Application terminate");
			IUserManager userManager = new UsersManager(null, new GetUsersClient());
			userManager.logOut();
		}
		catch (InvalidOperationException e) 
		{
			// Probably connection with the service fail
			e.printStackTrace();
		}
		super.onTerminate();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		// Update Configuration values
		super.onConfigurationChanged(newConfig);
		
	}
	
	public static String getBaseUrl()
	{
		return instance.getConfigValues().get(ConfigValues.CONST_URL_SERVICE);
	}
	
}
