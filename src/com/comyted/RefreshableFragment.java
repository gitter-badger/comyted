package com.comyted;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.enterlib.app.DataViewModel;
import com.enterlib.app.FragmentView;
import com.enterlib.app.PresentUtils;

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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(resultCode == Constants.MODIFIED){			
			//indica a la actividad que el cliente se modifico
			getActivity().setResult(resultCode);
			getViewModel().load();
		}
	}
	
	public String getViewText(int id){
		View rootView = getView();	
		TextView tv = (TextView) rootView.findViewById(id);
		return tv.getText().toString();
	}
	public void setText(int id, String text){
		View rootView = getView();
		PresentUtils.setTextViewText(rootView, id, text);
	}
	
	public int getModelId(){
		return getActivity()
				.getIntent()
				.getIntExtra(Constants.ID, 0);
	}
	
}
