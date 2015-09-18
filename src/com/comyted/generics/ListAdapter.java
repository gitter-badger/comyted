package com.comyted.generics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enterlib.app.CollectionAdapter;

public abstract class ListAdapter<T> extends CollectionAdapter<T> {
	private LayoutInflater _inflater;
	private int _layout;
	
	public ListAdapter(Context context,int layout,T[] objects) {
		super(context,layout, objects);
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else		
			view = _inflater.inflate(_layout, null);
		
		T item = getItem(position);
		updateView(view, item, position);
		return view;
	}
	
	protected abstract void updateView(View view, T item, int position);
}
