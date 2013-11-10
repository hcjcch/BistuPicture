package com.example.upload;

import java.io.IOException;

import java.util.List;

import org.apache.http.cookie.Cookie;

import com.example.dengLu.dengLu;
import com.example.mybistupicyure.BitmapCache;
import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.publicClass.manageCookie;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UploadfileActivity extends Activity {
	private ProgressDialog progressDialog;
	private LinearLayout uploadLinearlaoyoutLayout;
	private String uploadString;
	private String cookie;
	private RelativeLayout relativeLayout;
	private Button Startbutton;

	private Button btn_take_photo;
	private Button btn_pick_photo;
	private Button btn_cancel;

	private Uri photoUri;
	private String picPath = null;
	private static final String TAG = "SelectPicActivity";
	public static final String KEY_PHOTO_PATH = "photo_path";
	private ImageView button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uploadfile);
		
		button = (ImageView)findViewById(R.id.title_bar_menu_btn);
		cookie = getCookie();	
			
		progressDialog = new ProgressDialog(UploadfileActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在上传！");
		progressDialog.setIndeterminate(false);

		relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
		relativeLayout.setVisibility(View.GONE);
		Startbutton = (Button) findViewById(R.id.selectPicture);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_pick_photo = (Button) findViewById(R.id.btn_pick_photo);
		btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
		uploadLinearlaoyoutLayout = (LinearLayout) findViewById(R.id.uploadLinearLayout);

		Startbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (cookie != null) {
					relativeLayout.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(UploadfileActivity.this, "未登录,请登录!",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(UploadfileActivity.this, dengLu.class);
					startActivity(intent);
					finish();
				}
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_pick_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BitmapCache.getInstance().clearCache();
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
			}
		});

		btn_take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BitmapCache.getInstance().clearCache();
				takePhoto();
			}
		});
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UploadfileActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	// 拍照
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, 2);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	private String getCookie() {
		manageCookie manage = (manageCookie) getApplicationContext();
		List<Cookie> cookies = manage.getCookies();
		if (cookies == null) {
			System.out.println("-------Cookie NONE---------");
			return null;
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				String sesssionkey = cookies.get(i).getName();
				if (sesssionkey.equals("PHPSESSID")) {
					cookie = cookies.get(i).getValue();
					System.out.println(cookies.get(i).getValue() + "!!!");
				}
				System.out.println(cookies.get(i).getName() + "="
						+ cookies.get(i).getValue());
			}
			System.out.println(cookie + "上传照片+uploagfile");
			return cookie;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			relativeLayout.setVisibility(View.GONE);
			Startbutton.setVisibility(View.GONE);
			uploadLinearlaoyoutLayout.setBackgroundResource(R.drawable.upload);
			doPhoto(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doPhoto(int requestCode, Intent data) {
		try {
			if (requestCode == 1) { //从相册取图片，有些手机有异常情况，请注意
				if (data == null) {
					Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
					return;
				}
				photoUri = data.getData();
				if (photoUri == null) {
					Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
					return;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(UploadfileActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
			return;
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i(TAG, "imagePath = " + picPath);
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
			/*Toast.makeText(UploadfileActivity.this, picPath, Toast.LENGTH_SHORT)
					.show();*/
			(new AsyncTask1()).execute();
		} else {
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}

	class AsyncTask1 extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			cookie = getCookie();
			if (cookie == null) {
				Toast.makeText(UploadfileActivity.this, "未登录,请登录!",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			cookie = getCookie();
			Upload upload = new Upload(cookie, picPath);
			try {
				uploadString = upload.uploadFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("uploaderror" + e.getMessage());
				e.printStackTrace();
			}
			return uploadString;
		}

		@Override
		protected void onPostExecute(final String result) {
			// TODO Auto-generated method stub
			Toast.makeText(UploadfileActivity.this, "照片已成功上传！请补充照片信息！",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.putExtra("information", uploadString);
			intent.putExtra("picpath", picPath);
			intent.setClass(UploadfileActivity.this, uploadInformation.class);
			progressDialog.hide();
			progressDialog = null;
			System.out.println(11);
			startActivity(intent);
			finish();
			super.onPostExecute(result);
		}
	}
}
