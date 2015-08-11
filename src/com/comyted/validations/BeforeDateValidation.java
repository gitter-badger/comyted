package com.comyted.validations;

import java.util.Date;

import com.enterlib.fields.DatePickerButtonField;
import com.enterlib.validations.Validator;

public class BeforeDateValidation extends Validator {

	DatePickerButtonField fieldPrevius;
	DatePickerButtonField fieldAfter;
	

	public BeforeDateValidation(DatePickerButtonField fieldPrevius,
			DatePickerButtonField fieldAfter) {
		this.fieldPrevius = fieldPrevius;
		this.fieldAfter = fieldAfter;
	}
	
	@Override
	public boolean validate() {
		setErrorMessage(null);
		Date previus  = fieldPrevius.getDate();
		boolean result = true;
		if(previus == null){
			fieldPrevius.setErrorMessage(fieldPrevius.getRequiredMessage());
			result = false;
		}
		
		Date after = fieldAfter.getDate();
		if(after == null){
			fieldAfter.setErrorMessage(fieldAfter.getRequiredMessage());
			result = false;
		}
		
		if(!result)
			return result;
		
		if(previus.compareTo(after) > 0){
			String display = fieldPrevius.getDisplay();	
			String error;
			
			if(display!=null)
				error= String.format("Posterior a %s", display);
			else
				error = "No válida";
										
			fieldAfter.setErrorMessage(error);
			return false;
		}
		
		return true;
	}

}
