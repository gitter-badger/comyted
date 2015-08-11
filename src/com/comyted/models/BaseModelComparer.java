package com.comyted.models;

import com.enterlib.IEqualityComparer;

public class BaseModelComparer implements IEqualityComparer{			
	public boolean equals(Object item, Object value) {
		BaseModel model = (BaseModel)item;				
		Integer id = (Integer)value;
		return model.id == id;				
	}
	
}