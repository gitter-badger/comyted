/**
 * 
 */
package com.comyted.models;

import java.util.Date;

import com.comyted.Utils;

/**
 * @author Ansel
 *
 */
public class DateValues {
	public int Day;
	public int Month;
	public int Year;
	
	public DateValues(Date date)
	{
		if(date == null)
			throw new NullPointerException("date");
		String[] value = Utils.formatDateOnly(date).split("/");
		if(value.length!=3)
			return;
		Day = Integer.parseInt(value[0]);
		Month =Integer.parseInt(value[1]);
		Year = Integer.parseInt(value[2]);
	}
	
}
