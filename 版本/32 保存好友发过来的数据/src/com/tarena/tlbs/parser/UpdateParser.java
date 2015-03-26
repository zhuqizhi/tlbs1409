package com.tarena.tlbs.parser;

import org.json.JSONObject;

import com.tarena.tlbs.model.UpdateEntity;

public class UpdateParser {
	// {key:value,key:value}
	// {'status':'0','version':'5.5','changeLog':'" + changeLog
	// "','apkUrl':'http://192.168.188.98:8080/tlbsServer/a.apk'}"
	public UpdateEntity parser(String string) {
		UpdateEntity entity = null;
		try {

			//����jsong��������
			JSONObject jsonObject=new JSONObject(string);
			//����keyȡvalue
			String status=jsonObject.getString("status");
			String version=jsonObject.getString("version");
			String changeLog=jsonObject.getString("changeLog");
			String apkUrl=jsonObject.getString("apkUrl");
			entity=new UpdateEntity(status, version, changeLog, apkUrl);
		} catch (Exception e) {

		}
		return entity;
	}

}
