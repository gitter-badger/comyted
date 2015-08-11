package com.comyted.validations;

import java.util.Date;

import com.comyted.Messages;
import com.enterlib.DateUtils;
import com.enterlib.validations.validators.ValueValidator;

public class AfterCurrentDateValidation extends ValueValidator {

	public AfterCurrentDateValidation() {
			super(Messages.getString("AfterCurrentDateValidation.after_current_date")); //$NON-NLS-1$
	}
	
	@Override
	public boolean validateValue(Object value) {
		Date date = (Date)value;
		Date current = DateUtils.getCurrentDate();
		return date.compareTo(current) <= 0;
	}

	
}
