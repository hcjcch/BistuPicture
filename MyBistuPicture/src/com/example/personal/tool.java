package com.example.personal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class tool {
	private static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/downloadPicture/";
	
	public static void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.PNG, 80, bos);//Ñ¹Ëõ
		bos.flush();
		bos.close();
	}
}
