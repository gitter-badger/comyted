package com.comyted.generics;

import java.util.Comparator;

import junit.framework.Assert;

public class DefaultComparator<T> implements Comparator<T>{
	public int Order = 1;// 1=Ascending -1=Descending
	
	@Override
	public int compare(T lhs, T rhs) {
		Assert.assertNotNull(lhs);		
		Assert.assertNotNull(rhs);		
		
		String v1= lhs.toString();
		String v2 = rhs.toString();
				
		Assert.assertNotNull(v1);		
		Assert.assertNotNull(v2);
		
		return v1.compareToIgnoreCase(v2) * Order;
	}
}