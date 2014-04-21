package com.util;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

	public static String getCSV(List<String> strList)
	{
		StringBuilder retValue = new StringBuilder("");
		boolean isFirstRec = true;
		for (String mId : strList)
		{
			if (isFirstRec)
				isFirstRec = false;
			else
				retValue.append(",");
			retValue.append("'" + mId + "'");
		}
		
		return retValue.toString();
	}
	
	
}
