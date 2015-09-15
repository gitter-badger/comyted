package com.comyted;

import com.enterlib.converters.DateConverter;

public final class Constants {
	public static final String ORDER_ID = "orderID";
	public static final String ORDER = "order";
	public static final String SHEET_ID = "sheetID";
	public static final String SHEET = "sheet";
	public static final String TASK = "task";	
	public static final String TASK_ID = "sheet";
	public static final String TITLE = "title";
	public static final String USER_ID = "userId";
	public static final String CLIENT = "client";	
	public static final String CLIENT_ID = "clientId";
	public static final String ADDRESS = "ADDRESS";
	public static final String ID = "ID";
	public static final String ENTIRY = "ENTITY";
	
	public static final DateConverter ServerDateConverter = new DateConverter("yyyy-MM-dd HH:mm:ss");
	
	
	public static final int EDIT_TASK = 1;
	public static final int EDIT_SHEET = 3;
	public static final int TASK_OK = 4;
	public static final int SHEET_OK = 5;
	public static final int ADD_TASK = 6;
	public static final int ADD_SHEET = 7;
	public static final int CLIENT_MODIFIED = 8;
	public static final int EDIT_CLIENT = 9;
	public static final int CONTACT_MODIFIED = 10;	
	public static final int MODIFIED = 11;
	public static final int OK = 12;
	public static final int CANCEL = 13;
	public static final int EDIT = 14;
	
}
