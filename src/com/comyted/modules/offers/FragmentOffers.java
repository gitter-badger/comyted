package com.comyted.modules.offers;

import com.comyted.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOffers extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{					
		View rootView = inflater.inflate(R.layout.no_avalailable,
				container, false);
		
		return rootView;
	}
}
