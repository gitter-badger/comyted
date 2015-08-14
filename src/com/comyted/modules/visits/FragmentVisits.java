package com.comyted.modules.visits;

import com.comyted.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentVisits extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{					
		View rootView = inflater.inflate(R.layout.no_avalailable,
				container, false);
		
		return rootView;
	}
}
