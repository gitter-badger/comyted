package com.comyted.models;

public class LocalSheet extends Syncronizable {
		
	public SheetEdit Sheet;
	public LocalSheet(){
		
	}
	
	public LocalSheet(SheetEdit sheet, int id, int action) {
		Sheet = sheet;
		Id = id;
		Action = action;
	}
	
	@Override
	public String toString() {
		return Sheet.toString();
	}
}
