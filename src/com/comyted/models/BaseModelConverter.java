package com.comyted.models;

import com.enterlib.converters.IValueConverter;
import com.enterlib.exceptions.ConversionFailException;

public class BaseModelConverter implements IValueConverter{
	/**not used in SpinnerFields just return the parameter, In this case the parameter is an 
	 * integer representing an Id
	 * */
	public Object convert(Object value) throws ConversionFailException {
		return value;
	}
	
	/**Convert extract the Id from the BaseModel */
	public Object convertBack(Object value)
			throws ConversionFailException {	
		if(value==null)
			return null;
		return ((BaseModel)value).id;
	}
	
}