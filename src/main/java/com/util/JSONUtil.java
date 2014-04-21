package com.util;

public class JSONUtil 
{

	public static String getKeyValue(String key, String value)
	{
		String Q = "\"";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(Q);
		sb.append(key);
		sb.append(Q);
		sb.append(":");
		sb.append(Q);
		sb.append(value);
		sb.append(Q);
		
		return sb.toString();
	}
}
