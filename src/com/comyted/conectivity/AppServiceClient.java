package com.comyted.conectivity;

import com.comyted.MainApp;
import com.enterlib.conetivity.ServiceClient;

public class AppServiceClient extends ServiceClient {
	
	public AppServiceClient(String serviceName) {
		super(serviceName);
								
		
	}

	@Override
	public String getBaseUrl() {
		return MainApp.getBaseUrl();
	}	

}