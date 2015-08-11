package com.comyted.repository;

import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public interface IWorkTypeRepository {
	
	IdNameValue[] getWorkTypes() throws InvalidOperationException;
}
