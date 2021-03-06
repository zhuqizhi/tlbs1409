package com.tarena.tlbs.biz.implAsmack;

import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.network.NetworkCallBackListener;
import com.tarena.tlbs.parser.MapParser;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.LogUtil;
import com.tarena.tlbs.util.MapUtil;

public class MapBiz {
/**
 * ��ѯ��ַ
 * @param latitude
 * @param longitude
 */
	public void queryAddress(double latitude,double longitude)
{
		MapUtil.QueryAddress(String.valueOf(latitude), String.valueOf(longitude), new NetworkCallBackListener() {
			
			@Override
			public void callback(byte[] serverReturnData) {
				String jsong=new String(serverReturnData);
				LogUtil.i("QueryAddress", jsong);
				
				MapParser mapParser=new MapParser();
				String address=mapParser.parserAddress(jsong);
				
				Intent intent=new Intent(Const.ACTION_GET_LOCATION_ADDRESS);
				intent.putExtra(Const.KEY_DATA, address);
				TApplication.instance.sendBroadcast(intent);
			}
		});
	}
}
