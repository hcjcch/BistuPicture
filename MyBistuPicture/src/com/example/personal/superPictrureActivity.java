package com.example.personal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.publicClass.manageCookie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class superPictrureActivity extends Activity {

	private ImageView imageView;
	private int page;
	private int current_page = 1;
	private ListView listView;
	private String cookie;
	private superAdapter superadapter;
	private ArrayList<Map<String, Object>> anotherDataArrayList;
	private Handler handler;
	private ProgressDialog progressDialog;

	private boolean refresh = false;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.superlistview);
		
		progressDialog = new ProgressDialog(superPictrureActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("努力加载中！");
		progressDialog.setIndeterminate(false);
		
		listView = (ListView) findViewById(R.id.superList);
		imageView = (ImageView)findViewById(R.id.title_bar_menu_btn);
		getCookie();

		/*get_superInformation(current_page + "");
		superadapter = new superAdapter(this, anotherDataArrayList, 1);
		refresh = true;
		listView.setAdapter(superadapter);*/
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(superPictrureActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					superadapter.addData(anotherDataArrayList);
					superadapter.notifyDataSetChanged();
					refresh = true;
					progressDialog.hide();
					if (current_page == page) {
						progressDialog.dismiss();
					}
				} else if (msg.what == 2) {
					superadapter = new superAdapter(superPictrureActivity.this, anotherDataArrayList, 1);
					refresh = true;
					listView.setAdapter(superadapter);
					progressDialog.hide();
					//progressDialog.dismiss();
				}else {
					progressDialog.show();
				}
			}
		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				get_superInformation(current_page + "");
				handler.sendEmptyMessage(2);
			}
		}).start();
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (refresh) {
						refresh = false;
						if (current_page < page) {
							new Thread(new Mythread()).start();
						}
						if (current_page == page) {
							Toast.makeText(superPictrureActivity.this, "已全部加载完！",
									Toast.LENGTH_SHORT).show();
							current_page++;
							progressDialog.dismiss();
						}
					}
				}
			}
		});
	}

	private void get_superInformation(String page1) {
		personSuper personSuper = new personSuper(cookie);
		String informationString = personSuper.getinformation(page1);
		List<personSuperData> person = personSuper
				.dealPersonSuper(informationString);
		page = Integer.parseInt(personSuper.total) / 5 + 1;
		System.out.println(page);
		anotherDataArrayList = new ArrayList<Map<String, Object>>();
		for (personSuperData personSuperData : person) {
			Map<String, Object> nowData = new HashMap<String, Object>();
			Bitmap bitmap = getBitmap(personSuperData.get_img_main());
			nowData.put("img", bitmap);
			nowData.put("id", personSuperData.get_id());
			nowData.put("name", personSuperData.get_name());
			nowData.put("like_num", personSuperData.get_like_num());
			anotherDataArrayList.add(nowData);
		}
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
			get_superInformation(++current_page + "");
			handler.sendEmptyMessage(1);
		}
	}
}
