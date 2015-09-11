package com.comyted;

import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;
import com.google.android.gms.drive.internal.ab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class SupportViewFragment extends Fragment {
	public static final String SELECTION = "SELECTION";
	
	private int selectedPosition;
	private IDataView dataView;
	private DataViewModel viewModel;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState!=null){
			selectedPosition = savedInstanceState.getInt(SELECTION);
		}
		
		dataView = createView(getActivity());
		viewModel = createViewModel(dataView, getActivity());
		
	}
	
	protected abstract DataViewModel createViewModel(IDataView view, FragmentActivity activity);

	protected abstract IDataView createView(FragmentActivity activity);

	@Override
	public void onResume() {	
		super.onResume();
		
		if(dataView!=null){
			//activar la vista
			dataView.setIsValid(true);
		}
		
		if(viewModel!=null){
			//cargar los datos
			viewModel.load();
		}
	}
	
	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public IDataView getDataView() {
		return dataView;
	}

	public DataViewModel getViewModel() {
		return viewModel;
	}

	@Override
	public void onStop() {
		//desabilitar la vista
		if(dataView!=null)
			dataView.setIsValid(false);
		super.onStop();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putInt(SELECTION, selectedPosition);
	}
}
