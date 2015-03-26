package com.tarena.tlbs.adapter;

import java.io.File;
import java.util.ArrayList;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.TopicEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.ImageUtil;
import com.tarena.tlbs.util.LogUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAdapter extends BaseAdapter {
	Context context;
	ArrayList<TopicEntity> list;

	public TopicAdapter(Context context, ArrayList<TopicEntity> list) {
		super();
		this.context = context;
		if (list == null) {
			this.list = new ArrayList<TopicEntity>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		int size = 0;
		try {
			size = list.size();
		} catch (Exception e) {
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.topic_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvUsername = (TextView) convertView
						.findViewById(R.id.tv_topic_item_username);
				viewHolder.tvBody = (TextView) convertView
						.findViewById(R.id.tv_topic_item_body);
				viewHolder.tvAddress = (TextView) convertView
						.findViewById(R.id.tv_topic_item_address);

				viewHolder.ivImage = (ImageView) convertView
						.findViewById(R.id.iv_topic_item_image);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// 取数据
			TopicEntity topicEntity = list.get(position);

			viewHolder.tvUsername.setText(topicEntity.getUsername());
			viewHolder.tvBody.setText(topicEntity.getBody());
			viewHolder.tvAddress.setText(topicEntity.getLocationAddress());

			// 显示图片
			// /tlbsServer/tlbsUserimages/11.png
			// 工作中，得到地址最好有http://域名
			String imageAddress = topicEntity.getImageAddress();
			imageAddress = "http://" + TApplication.host + ":8080"
					+ imageAddress;
			// 取到文件名
			String fileName = imageAddress.substring(imageAddress
					.lastIndexOf("/") + 1);
			fileName = Const.IMAGE_PATH+"/" + fileName;
			File file = new File(fileName);
			// 判断sdcard上有没有
			if (file.exists()) {
				// 有，显示sdcard上的
				ImageUtil.displaySdcardImage(context, fileName,
						viewHolder.ivImage);
				LogUtil.i("imageLoader使用", "sdcard "+imageAddress);
			} else {
				// 没有
				// 不用imageLoader
				// new
				// Thread(run(httpClient))--byte[]-->bitmap-->imageView.setImageBitmap
				ImageUtil.displayNetWorkImage(context, imageAddress,
						viewHolder.ivImage);
				LogUtil.i("imageLoader使用", "取服务器的 "+imageAddress);

			}

		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvUsername, tvBody, tvTime, tvAddress;
		ImageView ivImage;
	}

}
