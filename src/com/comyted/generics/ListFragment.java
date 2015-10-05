package com.comyted.generics;

import java.util.Comparator;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.comyted.Constants;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.mvvm.CollectionViewModel;
import com.enterlib.mvvm.DataViewModel;

public abstract class ListFragment<T> extends com.comyted.ListFragment {

	CollectionViewModel<T> viewModel;
	ListAdapter<T>adapter;	
	Comparator<T> comparator;
	
	public CollectionViewModel<T> getViewModel() {
		return viewModel;
	}

	@Override
	public void onDataLoaded() {
		T[] items = viewModel.getItems();
		 
		 if(items == null || items.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = createAdapter(items);
		 setAdapter(adapter);

	}
	
	public Comparator<T> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	@Override
	protected void sortItems(int sortOrder) {
		if(adapter == null)
			return;		
		if(comparator instanceof DefaultComparator<?>){	
			DefaultComparator<?> defaultComparer = (DefaultComparator<?>)comparator;
			defaultComparer.Order = sortOrder;
			
			adapter.sort(comparator);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		@SuppressWarnings("unchecked")
		T c = (T) parent.getItemAtPosition(position);
		
		Intent intent = getItemViewIntent(c);
		if(intent!=null)
			startActivityForResult(intent, Constants.EDIT);
	}

	@Override
	protected DataViewModel createViewModel() {
		viewModel = new CollectionViewModel<T>(this, createRepository());
		return viewModel;
	}

	protected abstract ICollectionRepository<T> createRepository();

	protected abstract ListAdapter<T> createAdapter(T[] items);
	
	protected  abstract Intent getItemViewIntent(T c);

}
