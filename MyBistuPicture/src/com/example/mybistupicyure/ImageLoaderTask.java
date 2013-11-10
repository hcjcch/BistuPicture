package com.example.mybistupicyure;

import java.lang.ref.WeakReference;
import android.view.ViewGroup.LayoutParams;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{
	
	private int item_width;
	private String url;
	private final WeakReference<ImageView> imageViewReference;
	private WeakReference<Bitmap> bitmapReference = null;
	private String cookie;
	
	public ImageLoaderTask(ImageView imageView,String cookie,int item_width,String url) {
		this.item_width = item_width;
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.cookie = cookie;
		this.url = url;
	}
	@Override
	protected Bitmap doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		if (bitmapReference != null) {
			
		}
		Bitmap bm = BitmapCache.getInstance().getBitmap(cookie, url);
		bitmapReference = new WeakReference<Bitmap>(bm);
		return bm;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// TODO Auto-generated method stub
		super.onPostExecute(bitmap);
		if (isCancelled()) {
			bitmap = null;
		}
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				if (bitmap != null) {
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					LayoutParams lp = imageView.getLayoutParams();
					lp.height = (height * item_width) / width;
					imageView.setLayoutParams(lp);
					imageView.setImageBitmap(bitmap);
					/*if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}*/
				}
			}
		}
	}
}
