package com.comyted;

import java.util.Locale;

import com.enterlib.converters.IValueConverter;
import com.enterlib.exceptions.ConversionFailException;

public class ToUpperConverter implements IValueConverter {

	@Override
	public Object convert(Object value) throws ConversionFailException {		
		if(value!=null)
			return value.toString().toUpperCase(Locale.getDefault());
		return null;
	}

	@Override
	public Object convertBack(Object value) throws ConversionFailException {
		if(value!=null)
			return value.toString().toUpperCase(Locale.getDefault());
		return null;
	}

}
