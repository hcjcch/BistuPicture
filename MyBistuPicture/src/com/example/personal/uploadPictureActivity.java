package com.example.personal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.example.dengLu.dengLu;
import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.personal.superPictrureActivity.Mythread;
import com.example.publicClass.manageCookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class uploadPictureActivity extends Activity {

	private String cookie;
	private ArrayList<Map<String, Object>> upload;
	private int page;
	private int current_page = 1;
	private uploadAdapter uploadAdapter;
	private ListView listView;
	private Handler handler;
	private ProgressDialog progressDialog;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uploadlisview);
		listView = (ListView) findViewById(R.id.uploadList);
		imageView = (ImageView) findViewById(R.id.title_bar_menu_btn);
		progressDialog = new ProgressDialog(uploadPictureActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("努力加载中！");
		progressDialog.setIndeterminate(false);

		cookie = getCookie();
		if (cookie == null) {
			Intent intent = new Intent();
			intent.setClass(uploadPictureActivity.this, dengLu.class);
			Toast.makeText(uploadPictureActivity.this, "未登录,请登录!",
					Toast.LENGTH_SHORT).show();
			startActivity(intent);
			finish();
			return;
		}
		
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(uploadPictureActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		/*get_uploadInformation(current_page + "");
		uploadAdapter = new uploadAdapter(this, upload);
		listView.setAdapter(uploadAdapter);
*/
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 1) {
					uploadAdapter.addData(upload);
					uploadAdapter.notifyDataSetChanged();
					progressDialog.hide();
					if (current_page == page) {
						progressDialog.dismiss();
					}
				}else if (msg.what == 2) {
					uploadAdapter = new uploadAdapter(uploadPictureActivity.this, upload);
					listView.setAdapter(uploadAdapter);
					progressDialog.hide();
					if (current_page == page) {
						progressDialog.dismiss();
					}
				} else {
					progressDialog.show();
				}
			}

		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				handler.sendEmptyMessage(0);
				get_uploadInformation(current_page + "");
				handler.sendEmptyMessage(2);
			}
		}).start();
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					if (current_page < page) {
						new Thread(new Mythread()).start();
					}
					if (current_page == page) {
						Toast.makeText(uploadPictureActivity.this, "已全部加载完！",
								Toast.LENGTH_SHORT).show();
						current_page++;
						progressDialog.dismiss();
					}
					break;

				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void get_uploadInformation(String page1) {
		upload = new ArrayList<Map<String, Object>>();
		uploadData uploadinformation = new uploadData(cookie);
		String informationString = uploadinformation.getinformation(page1);
		System.out.println(informationString);
		try {
			upload = uploadinformation.dealuploadData(informationString);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		page = Integer.parseInt(uploadData.total1) / 5 + 1;
	}

	// 获取cookie
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

	Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(url);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		httpRequest.setParams(httpParams);
		try {
			HttpResponse response = httpClient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream instream = bufHttpEntity.getContent();
			bitmap = BitmapFactory.decodeStream(instream);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	class Mythread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(0);
			get_uploadInformation(++current_page + "");
			handler.sendEmptyMessage(1);
		}
	}
}
