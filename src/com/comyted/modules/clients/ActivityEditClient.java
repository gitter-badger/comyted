/**
 * 
 */
package com.comyted.modules.clients;

import com.comyted.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author ansel
 *
 */
public class ActivityEditClient extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.no_avalailable);
		
		setResult(RESULT_CANCELED);
	}
}
