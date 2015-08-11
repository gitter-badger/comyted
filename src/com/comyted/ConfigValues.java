package com.comyted;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigValues {
	
	public static final String CONST_URL_SERVICE = "CONST_URL_SERVICE";
	public static final String CONST_EMAIL_USER = "CONST_EMAIL_USER";
	public static final String CONST_EMAIL_PASSWORD = "CONST_EMAIL_PASSWORD";
	public static final String CONST_EMAIL_SMTP = "CONST_EMAIL_SMTP";
	public static final String CONST_EMAIL_PORT = "CONST_EMAIL_PORT";
	
	SharedPreferences prefs = null;
	
	public ConfigValues(SharedPreferences prefs){
		this.prefs = prefs;
		//save(CONST_URL_SERVICE, "http://cessa.info:8050");
	}
	
	public boolean save(String key, String value){
		boolean ok = false;
		if (key != null && !key.equals("")){
			try{
				Editor editor = this.prefs.edit();
				editor.putString(key, value);
				editor.commit();
				ok = true;
			}catch(Exception exc){}
		}
		return ok;
	}
	
	public String get(String key){
		return get(key, "");
	}
	
	public String get(String key,String defaultValue){
		return this.prefs.getString(key, defaultValue);
	}
	
	public boolean contains(String key){
		return this.prefs.contains(key);
	}
}
