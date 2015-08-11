package com.comyted;


public class OpenCloseResult<T> {
	public T[] Opens;
	public T[] Closed;
	
	public boolean isEmpty(){
		return (Opens == null || Opens.length ==0) &&
				(Closed == null || Closed.length ==0);
	}
}
