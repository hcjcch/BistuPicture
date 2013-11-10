package com.example.personal;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dengLu.dengLu;
import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.publicClass.manageCookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class personal extends Activity {

	private String cookie;
	private ImageView title_bar_menu_btn;
	private String content = null;
	private ImageView personalPictureImageView;
	private TextView nicknameTextView;
	private TextView sexTextView;
	private TextView emailTextView;
	private TextView join_timeTextView;
	private TextView scoreTextView;
	private TextView textView1;
	private TextView aaTextView;
	private TextView bbTextView;
	private TextView ccTextView;
	private ProgressDialog progressDialog;

	private Button superButton;
	private Button uploadButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal);

		cookie = getCookie();
		if (cookie == null) {
			Intent intent = new Intent();
			intent.setClass(personal.this, dengLu.class);
			Toast.makeText(personal.this, "未登录,请登录!", Toast.LENGTH_SHORT)
					.show();
			startActivity(intent);
			finish();
			return;
		}

		progressDialog = new ProgressDialog(personal.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在加载！");
		progressDialog.setIndeterminate(false);

		title_bar_menu_btn = (ImageView) findViewById(R.id.title_bar_menu_btn);
		nicknameTextView = (TextView) findViewById(R.id.nickname);
		sexTextView = (TextView) findViewById(R.id.sex);
		emailTextView = (TextView) findViewById(R.id.email);
		join_timeTextView = (TextView) findViewById(R.id.join_time);
		scoreTextView = (TextView) findViewById(R.id.score);
		textView1 = (TextView) findViewById(R.id.gerenxinxi);

		aaTextView = (TextView) findViewById(R.id.aa);
		bbTextView = (TextView) findViewById(R.id.bb);
		ccTextView = (TextView) findViewById(R.id.cc);

		superButton = (Button) findViewById(R.id.superPicture);
		uploadButton = (Button) findViewById(R.id.uploadPicture);
		title_bar_menu_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(personal.this, MainActivity.class);
				startActivity(intent);
			}
		});
		superButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(personal.this, superPictrureActivity.class);
				startActivity(intent);
			}
		});
		uploadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(personal.this, uploadPictureActivity.class);
				startActivity(intent);
			}
		});
		(new Asytry()).execute();
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

	// 获得个人信息
	private String getContent() {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://mybistu.sinaapp.com/api/user.json");
		get.setHeader("cookie", "PHPSESSID=" + cookie);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = "utf-8";
				// 使用EntityUtils的toString方法，传递默认编码，在EntityUtils中的默认编码是ISO-8859-1
				content = EntityUtils.toString(entity, charset);
				System.out.println(content + "返回的个人信息");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	// 解析个人信息
	private Map<String, String> getJson(String s) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(s);
			String email = jsonObject.getString("email");
			String nickName = jsonObject.getString("nickname");
			String sex = jsonObject.getString("sex");
			String score = jsonObject.getString("score");
			String img = jsonObject.getString("img");
			String join_time = jsonObject.getString("join_time");
			map.put("email", email);
			map.put("nickname", nickName);
			map.put("sex", sex);
			map.put("score", score);
			map.put("img", img);
			map.put("join_time", join_time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString() + "这是解析个人信息json数据");
		}
		return map;
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

	class Asytry extends AsyncTask<String, String, String> {
		private String info;
		private Bitmap personalPicture;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			info = getContent();
			if (info == null) {
				return null;
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map = getJson(info);
				String url = map.get("img");
				System.out.println(url);
				if (url != "null") {
					personalPicture = getBitmap(url);
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (info == null) {
				Toast.makeText(personal.this, "未登录，请登录", Toast.LENGTH_SHORT)
						.show();
				progressDialog.hide();
			} else if(personalPicture != null){
				personalPictureImageView = (ImageView) findViewById(R.id.personalPicture);
				personalPictureImageView.setImageBitmap(personalPicture);
			}
			Map<String, String> map = new HashMap<String, String>();
			map = getJson(info);
			String email = map.get("email");
			String join_time = map.get("join_time");
			String nickname = map.get("nickname");
			String sex = map.get("sex");
			String score = map.get("score");
			nicknameTextView.setText(nickname);
			nicknameTextView.getPaint().setFakeBoldText(true);
			if (sex.equals("1")) {
				sexTextView.setText("♂");
			} else if (sex.equals("2")) {
				sexTextView.setText("♀");
			} else {
				sexTextView.setText("");
			}
			emailTextView.setText(email);
			join_timeTextView.setText(join_time);
			scoreTextView.setText(score);
			textView1.setText("个人信息");
			aaTextView.setVisibility(View.VISIBLE);
			bbTextView.setVisibility(View.VISIBLE);
			ccTextView.setVisibility(View.VISIBLE);
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}

}
