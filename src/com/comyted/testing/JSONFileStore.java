package com.comyted.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.enterlib.StringUtils;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.serialization.JSonSerializer;

import junit.framework.Assert;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class JSONFileStore {

	static final String ReposoryDir="Repositories";
	Context context;
	JSonSerializer serializer;
	public JSONFileStore(Context context){
		this.context = context;
		serializer = new JSonSerializer();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T load(Class<T> clase, String jsonFile){
		String content = getJson(jsonFile);
		
		try {
			return (T) serializer.deserialize(clase, content);
		} catch (InvalidOperationException e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}
	
	protected void save(Object value, String jsonFile){
		String content = null;
		try {
			content = serializer.serialize(value);
		} catch (InvalidOperationException e) {
			Assert.fail("valor no valido: " +e.getMessage());
		}
		
		OutputStream os = getOutputFromExternalDir(jsonFile);
	     OutputStreamWriter writer = new OutputStreamWriter(os);
	     try {
			writer.write(content);			
			writer.close();			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}	     
	    return;
	}
	
	protected String getJson(String filename){
		InputStream is = getFromExternalDir(filename);
		if(is == null)
			is = getFromAppDir(filename);
		if(is ==null)
			is = getStreamFromAssets(filename);		
		
		Assert.assertNotNull(is);
		
		String content = StringUtils.readAllText(is);
		try {
			is.close();
		} catch (IOException e) {
			Assert.fail("Stream No CLOSE");
		}
		
		Assert.assertNotNull(content);
		
		return content;
	}
	
	private InputStream getStreamFromAssets(String filename){
		AssetManager manager = context.getAssets();
		//Open our data file 
		try {
			InputStream fis = manager.open(filename);
			return fis;
		} catch (IOException e1) {
			Log.d("JSONFileStore", "Unable to load file from Assets");			
			return null;
		} 
	}
	
	private InputStream getFromAppDir(String filename){
		 File dir = context.getDir(ReposoryDir, Context.MODE_PRIVATE);
		 InputStream fis = null;
		   try {
				fis = new FileInputStream(dir.getAbsolutePath()+ "/" + filename);
				return fis;
			} catch (FileNotFoundException e) {
				Log.d("JSONFileStore", "Unable to load file from AppDir");	
				return null;			
			}
	}
	
	private InputStream getFromExternalDir(String filename){
		File dir = context.getExternalFilesDir(null);
		InputStream fis = null;		
		   try {
				fis = new FileInputStream(dir.getAbsolutePath()+ "/" + filename);
				return fis;
			} catch (FileNotFoundException e) {
				Log.d("JSONFileStore", "Unable to load file for AppDir in SD card");	
				return null;			
			}
		
	}
	
	private OutputStream getOutputFromExternalDir(String filename){		
		File dir = context.getExternalFilesDir(null);
		File file = new File(dir.getAbsolutePath()+ "/" + filename);
//		if(!file.exists()){
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		FileOutputStream fis = null;		
		   try {
				fis = new FileOutputStream(file,false);
			
				return fis;
			} catch (FileNotFoundException e) {
				Log.d("JSONFileStore", "Unable to load file for AppDir in SD card");	
				return null;			
			}
	}
		
	
}
