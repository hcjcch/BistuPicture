package com.example.mybistupicyure;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import com.example.publicClass.downloadPicture;

import android.graphics.Bitmap;

public class BitmapCache {

	private static BitmapCache cache;
	private ReferenceQueue<Bitmap> q;
	private Hashtable<String, BitmapRef> bitmapRefs;

	private BitmapCache() {
		// TODO Auto-generated constructor stub
		bitmapRefs = new Hashtable<String, BitmapCache.BitmapRef>();
		q = new ReferenceQueue<Bitmap>();
	}

	public static BitmapCache getInstance() {
		if (cache == null) {
			cache = new BitmapCache();
		}
		return cache;
	}

	public Bitmap getBitmap(String cookie, String url) {

		Bitmap bitmap = null;
		if (bitmapRefs.containsKey(url)) {
			BitmapRef ref = bitmapRefs.get(url);
			bitmap = ref.get();
		}
		if (bitmap == null) {
			System.out.println(url+"--------------");
			bitmap = downloadPicture.downloadPicture(cookie, url);
			this.addCacheBitmap(bitmap, url);
		}
		return bitmap;

	}

	private void addCacheBitmap(Bitmap bitmap, String url) {
		// TODO Auto-generated method stub
		cleanCache();
		BitmapRef ref = new BitmapRef(bitmap, q, url);
		bitmapRefs.put(url, ref);
	}

	private void cleanCache() {
		// TODO Auto-generated method stub
		BitmapRef ref = null;
		while ((ref = (BitmapRef) q.poll()) != null) {
			bitmapRefs.remove(ref._key);
		}
	}

	private class BitmapRef extends SoftReference<Bitmap> {

		private String _key;

		public BitmapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
			super(bmp, q);
			// TODO Auto-generated constructor stub
			this._key = key;
		}
	}

	// 清除Cache内的全部内容
	public void clearCache() {
		cleanCache();
		bitmapRefs.clear();
		System.gc();
		System.runFinalization();
	}

	 
}
