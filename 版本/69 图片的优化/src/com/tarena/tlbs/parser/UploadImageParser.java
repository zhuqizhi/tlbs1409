package com.tarena.tlbs.parser;

import org.json.JSONObject;

public class UploadImageParser {
	/**
	 * 
	 * @param jsonString
	 * @return 图片在server上的地址
	 */
	public String parser(String jsonString) {
		String address = null;
		try {
			// {'status':'0','msg':'/tlbsServer/tlbsuserImages/1.jpg'}
			JSONObject jsonObject = new JSONObject(jsonString);
			String status = jsonObject.getString("status");
			if ("0".equals(status)) {
				address = jsonObject.getString("msg");
			}
		} catch (Exception e) {

		}
		return address;
	}
}
