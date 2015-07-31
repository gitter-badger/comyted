package com.comyted.models;

public class IdNameValue extends BaseModel 
{	
	public String name;
	
	public IdNameValue() {
		
	}
	
	public IdNameValue(int id, String name) {
		this.id =id;
		this.name = name;
	}

	@Override
	public String toString() {
		return name!=null? name: super.toString();
	}
	
	
	
	public boolean isValid(){
		return id >=0 && name != null;
	}	
}
