package com.tarena.tlbs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageUtil {
	//设置参数
  private static ImageLoaderConfiguration config;
  
  private static ImageLoaderConfiguration getConfig(Context context)
  {
	  if (config==null)
	  {
		  config=new ImageLoaderConfiguration.Builder(context).discCacheSize(1024*1024*50).discCacheFileCount(50).build();
	  }
	  return config;
  }
  /**
   * 显示网上的一个图片
   * @param context
   * @param imageUrl
   * @param imageView
   */
  public static void displayNetWorkImage(Context context,String imageUrl,ImageView imageView)
  {
	  ImageLoader imageLoader=ImageLoader.getInstance();
	  imageLoader.init(getConfig(context));
	  
	  imageLoader.displayImage(imageUrl, imageView, new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
	});
	  
  }
}
