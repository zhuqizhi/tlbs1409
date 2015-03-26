package com.tarena.tlbs.adapter;

import java.io.IOException;
import java.io.InputStream;

import org.fengwx.gif.GifImageView;

import com.tarena.tlbs.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter {
	Context context;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return faceImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return faceImageIds[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GifImageView imageView = new GifImageView(context);
		try {
			InputStream inputStream=context.getAssets().open(faceImageIds[position]);
			int size=inputStream.available();
			byte[] faceData=new byte[size];
			inputStream.read(faceData);
			Bitmap bitmap=BitmapFactory.decodeByteArray(faceData, 0, faceData.length);
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return imageView;
	}

	String[] faceImageIds = new String[] { "f001.gif", "f002.gif", "f003.gif",
			"f004.gif", "f005.gif", "f006.gif", "f007.gif", "f008.gif",
			"f009.gif", "f010.gif", "f011.gif", "f012.gif", "f013.gif",
			"f014.gif", "f015.gif", "f016.gif", "f017.gif", "f018.gif",
			"f019.gif", "f020.gif", "f021.gif", "f022.gif", "f023.gif",
			"f024.gif", "f025.gif", "f026.gif", "f027.gif", "f028.gif",
			"f029.gif", "f030.gif", "f031.gif", "f032.gif", "f033.gif",
			"f034.gif", "f035.gif", "f036.gif", "f037.gif", "f038.gif",
			"f039.gif", "f040.gif", "f041.gif", "f042.gif", "f043.gif",
			"f044.gif", "f045.gif", "f046.gif", "f047.gif", "f048.gif",
			"f049.gif", "f050.gif", "f051.gif", "f052.gif", "f053.gif",
			"f054.gif", "f055.gif", "f056.gif", "f057.gif", "f058.gif",
			"f059.gif", "f060.gif", "f061.gif", "f062.gif", "f063.gif",
			"f064.gif", "f065.gif", "f066.gif", "f067.gif", "f068.gif",
			"f069.gif", "f070.gif", "f071.gif", "f072.gif", "f073.gif",
			"f074.gif", "f075.gif", "f076.gif", "f077.gif", "f078.gif",
			"f079.gif", "f080.gif", "f081.gif", "f082.gif", "f083.gif",
			"f084.gif", "f085.gif", "f086.gif", "f087.gif", "f088.gif",
			"f089.gif", "f090.gif", "f091.gif", "f092.gif", "f093.gif",
			"f094.gif", "f095.gif", "f096.gif", "f097.gif", "f098.gif",
			"f099.gif", "f100.gif", "f101.gif", "f102.gif", "f103.gif",
			"f104.gif", "f105.gif", "f106.gif", "f107.gif", "f108.gif",
			"f109.gif", "f110.gif", "f111.gif", "f112.gif", "f113.gif",
			"f114.gif", "f115.gif", "f116.gif", "f117.gif", "f118.gif",
			"f119.gif", "f120.gif", "f121.gif", "f122.gif", "f123.gif",
			"f124.gif", "f125.gif", "f126.gif", "f127.gif", "f128.gif",
			"f129.gif", "f130.gif", "f131.gif", "f132.gif", "f133.gif",
			"f134.gif", "f135.gif", "f136.gif", "f137.gif", "f138.gif",
			"f139.gif", "f140.gif", "f141.gif", "f142.gif" };

	public FaceAdapter(Context context) {
		super();
		this.context = context;
	}

}
