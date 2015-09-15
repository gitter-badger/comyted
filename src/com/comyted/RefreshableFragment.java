package com.comyted;

import android.view.MenuItem;

import com.enterlib.app.DataViewModel;
import com.enterlib.app.FragmentView;

public abstract class RefreshableFragment extends FragmentView {
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
