package com.comyted.conectivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.enterlib.exceptions.InvalidOperationException;

public class PostFirmaClient extends AppServiceClient {

	public PostFirmaClient() {
		super("PostFirmas.svc");
		
	}	
	
	public static byte[] getData(Bitmap bitmap) throws InvalidOperationException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[]data = null;
		try{
			if(!bitmap.compress(CompressFormat.PNG, 1, bos))
				throw new InvalidOperationException("Invalid Bitmap");	
			data = bos.toByteArray();
		}
		finally{
			try {
				bos.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return data;
	}
	
	
	public boolean GrabarFirmaCliente(int sheetId, Bitmap bitmap) 
			throws InvalidOperationException{
				
		byte[]data =getData(bitmap);		
		return post(boolean.class, "GrabarFirmaCliente", data, 
				new RequestParameter("codhoja", sheetId));
	}

	
	public boolean GrabarFirmaTecnico(int sheetId, Bitmap bitmap) 
			throws InvalidOperationException{
				
		byte[]data = getData(bitmap);		
		return post(boolean.class, "GrabarFirmaTecnico", data, 
				new RequestParameter("codhoja", sheetId));
	 }
}
