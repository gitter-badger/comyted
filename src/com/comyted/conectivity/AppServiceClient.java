package com.comyted.conectivity;

import com.comyted.MainApp;
import com.enterlib.conetivity.WCFServiceClient;

public class AppServiceClient extends WCFServiceClient {
	
	public AppServiceClient(String serviceName) {
		super(serviceName);
								
		
	}

	@Override
	public String getBaseUrl() {
		return MainApp.getBaseUrl();
	}	

}