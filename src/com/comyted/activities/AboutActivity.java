package com.comyted.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.comyted.R;

public class AboutActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        PackageInfo pinfo = null;
		try {
			pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
        TextView about = (TextView)findViewById(R.id.about_info_header);
        about.setText("Version " + pinfo.versionName.toString());
    }

}
