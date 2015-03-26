package com.tarena.tlbs.parser;

import org.json.JSONObject;

public class MapParser {
	
	public String parserAddress(String jsonString)
	{
		String address=null;
		try {
			JSONObject jsonObject=new JSONObject(jsonString);
			JSONObject result=jsonObject.getJSONObject("result");
			address=result.getString("formatted_address");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return address;
	}

}
