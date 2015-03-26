package com.tarena.tlbs.util;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageUtil {
	/**
	 * ��ʾsdcard��ͼƬ
	 * @param context
	 * @param filePathName
	 * @param imageView
	 */
	 public static void displaySdcardImage(Context context,String filePathName,ImageView imageView)
	  {
		 ImageLoader imageLoader=ImageLoader.getInstance();
		 imageLoader.init(getConfig(context));
		 //ǰ�����file://��ʾ����ļ� ����sdcard��
		 filePathName="file://"+filePathName;
		imageLoader.displayImage(filePathName, imageView);
		 
	  }
	
	//���ò���
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
   * ��ʾ���ϵ�һ��ͼƬ
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
		public void onLoadingComplete
		(String imageUri, View view, 
				Bitmap loadedImage) {

			try {
				//��ͼƬ���浽sdcard��
				//��ͼƬ���ݷŵ�outputStream
				ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
				loadedImage.compress(CompressFormat.PNG, 30, outputStream);
				
				//imageUri:http://124:8080/tlbsSever/1/1.png
				String fileName=imageUri.substring(imageUri.lastIndexOf("/")+1);
				Tools.writeToSdcard(Const.IMAGE_PATH, fileName, outputStream.toByteArray());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
	});
	  
  }
  
 
}
