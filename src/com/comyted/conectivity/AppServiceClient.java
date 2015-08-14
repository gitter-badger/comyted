package com.comyted.conectivity;

import junit.framework.Assert;

import com.comyted.MainApp;
import com.enterlib.conetivity.WCFServiceClient;

public class AppServiceClient extends WCFServiceClient {
	
	public AppServiceClient(String serviceName) {
		super(serviceName);
								
		
	}

	@Override
	public String getBaseUrl() {
		String url = MainApp.getBaseUrl();
		Assert.assertNotNull("SERVICE URL NULL", url);
		return url;
	}	

}