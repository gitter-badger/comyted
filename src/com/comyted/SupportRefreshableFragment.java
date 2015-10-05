package com.comyted;

import android.view.MenuItem;

import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.SupportFragmentView;

public abstract class SupportRefreshableFragment extends SupportFragmentView {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		DataViewModel vm = getViewModel();	
		switch (id) {		
			case R.id.refresh:
				if(vm!=null)
					vm.load();
				return true;		
		}
		return false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(getViewModel()!=null){
			getViewModel().load();
		}
	}
}
