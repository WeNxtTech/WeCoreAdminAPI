package com.maan.eway.embedded;

import java.util.Comparator;
import java.util.Map;

public class MyComparater implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		Map<String,String> map1 =(Map<String,String>)o1;
		Map<String,String> map2 =(Map<String,String>)o2;
		String str1 =map1.get("Description")==null?"":map1.get("Description").toString();
		String str2 =map2.get("Description")==null?"":map2.get("Description").toString();
		return str1.compareToIgnoreCase(str2);
	}

}
