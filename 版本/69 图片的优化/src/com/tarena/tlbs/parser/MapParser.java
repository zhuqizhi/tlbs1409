package com.tarena.tlbs.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tarena.tlbs.model.TopicEntity;
import com.tarena.tlbs.util.ExceptionUtil;

public class MapParser {

	public ArrayList<TopicEntity> parserAllData(String json) {
		ArrayList<TopicEntity> list = new ArrayList<TopicEntity>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray array = jsonObject.getJSONArray("pois");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObjectTopic = array.getJSONObject(i);
				String username = null, body = null, imageAddress = null, locatoinAddress = null;
				double time = 0, longitude = 0, latitude = 0;

				// 如果json中没有username这个key,会出异常。
				try {
					username = jsonObjectTopic.getString("username");
				} catch (Exception e) {
				}
				try {
					body = jsonObjectTopic.getString("body");
				} catch (Exception e) {
				}

				try {
					imageAddress = jsonObjectTopic.getString("imageAddress");
				} catch (Exception e) {
				}
				try {
					locatoinAddress = jsonObjectTopic.getString("address");
				} catch (Exception e) {
				}
				try {
					String strTime = jsonObjectTopic.getString("time");
					time = Double.parseDouble(strTime);
				} catch (Exception e) {
				}
				try {
					JSONArray jsonArrayLocation = jsonObjectTopic
							.getJSONArray("location");
					longitude = jsonArrayLocation.getDouble(0);
					latitude = jsonArrayLocation.getDouble(1);
				} catch (Exception e) {
				}

				TopicEntity topicEntity = new TopicEntity(username, body,
						imageAddress, locatoinAddress, time, longitude,
						latitude);
				list.add(topicEntity);
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return list;
	}

	public String parserAddress(String jsonString) {
		String address = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject result = jsonObject.getJSONObject("result");
			address = result.getString("formatted_address");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return address;
	}

}
