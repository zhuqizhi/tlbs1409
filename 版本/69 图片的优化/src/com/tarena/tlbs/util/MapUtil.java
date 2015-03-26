package com.tarena.tlbs.util;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.network.NetworkCallBackListener;

public class MapUtil {
	/**
	 * 根据坐标查询所在位置的地址
	 */
	public static void QueryAddress(String latitude, String longitude,
			final NetworkCallBackListener networkCallBackListener) {
		RequestParams params = new RequestParams();
		params.put("ak", Const.BAIDU_SERVER_KEY);
		params.put("location", latitude + "," + longitude);
		params.put("output", "json");
		params.put("pois", "0");

		TApplication.asyncHttpClient.get(Const.MAP_QUERY_ADDRESS, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

						networkCallBackListener.callback(responseBody);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						networkCallBackListener.callback(responseBody);


					}
				});

	}

	/**
	 * 
	 * 搜索指定坐标点周围的自定义POI(话题) 自定义POI保存在 lbsyun.baidu.com 上，请创建相关数据表，并申请ak等访问秘钥
	 * 
	 * @param geotableId
	 *            POI表ID
	 * @param tags
	 *            标签
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param radius
	 *            半径
	 * 
	 */
	public static void SearchPOIByNearby(String geotableId, String tags,
			double longitude, double latitude, long radius,
			final NetworkCallBackListener callBackListener) {

		RequestParams params = new RequestParams();
		params.put("ak", Const.BAIDU_SERVER_KEY);
		params.put("geotable_id", geotableId);
		params.put("output", "json");
		params.put("location", "" + longitude + "," + latitude);
		params.put("radius", "" + radius);
		params.put("tags", tags);
		params.put("page_index", "0");
		params.put("page_size", "50");

		TApplication.asyncHttpClient.get(Const.MAP_QUERY_NEARBY_POI_LIST,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						callBackListener.callback(responseBody);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						callBackListener.callback(responseBody);

					}
				});
	}

	/**
	 * 创建一个自定义POI自定义POI保存在 lbsyun.baidu.com 上，请创建相关数据表，并申请ak等访问秘钥
	 * 
	 * @param title
	 *            标题
	 * @param address
	 *            地址(可以通过QueryAddressByLatitude方法将坐标转换为地址)
	 * @param tags
	 *            标签
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param coordType
	 *            坐标类型(这里推荐使用百度自己的坐标)
	 * @param geotableId
	 *            POI表ID
	 * @param weiboId
	 *            微博ID
	 * @param weiboText
	 *            微博内容
	 */
	public static void CreatePOI(String title, String address, String tags,
			String latitude, String longitude, String coordType,
			String geotableId, String username, String body, String image,
			String time, final NetworkCallBackListener callBackListener) {
		RequestParams params = new RequestParams();
		//百度要求提供的
		params.put("ak", Const.BAIDU_SERVER_KEY);
		params.put("title", title);
		params.put("address", address);
		params.put("tags", tags);
		params.put("latitude", latitude);
		params.put("longitude", longitude);
		params.put("coord_type", coordType);
		params.put("geotable_id", geotableId);
		//key名必须与我们创建的列名一样
		params.put("username", username);
		params.put("body", body);
		params.put("imageAddress", image);
		params.put("time", time);
		TApplication.asyncHttpClient.post(Const.MAP_POI_CREATE, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

						callBackListener.callback(responseBody);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {

						callBackListener.callback(responseBody);

					}
				});

	}
}
