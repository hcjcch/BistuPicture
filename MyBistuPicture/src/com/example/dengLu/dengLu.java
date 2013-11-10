package com.example.dengLu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.publicClass.manageCookie;

public class dengLu extends Activity {
	private Button startDengLu;
	private EditText username;
	private EditText password;
	private String name;
	private String passwd;
	private String infoString;
	private ImageView dengLuBack;
	private SharedPreferences sp = null;
	private CheckBox login;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.denlu);
		startDengLu = (Button) findViewById(R.id.startDengLu);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		dengLuBack = (ImageView) findViewById(R.id.title_bar_menu_btn);
		login = (CheckBox) findViewById(R.id.checkBok);
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		progressDialog = new ProgressDialog(dengLu.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("ÕýÔÚµÇÂ¼£¡");
		progressDialog.setIndeterminate(false);
		if (sp.getBoolean("auto", false)) {
			username.setText(sp.getString("uname", null));
			password.setText(sp.getString("upswd", null));
			login.setChecked(true);
		}
		startDengLu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = username.getText().toString();
				passwd = password.getText().toString();
				(new Asy()).execute();

			}
		});

		dengLuBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(dengLu.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	private void http(String username, String password) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mybistu.sinaapp.com/request/login.php");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("Data[email]", username));
		pairs.add(new BasicNameValuePair("Data[pwd]", password));
		pairs.add(new BasicNameValuePair("Data[remember]", "y"));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpResponse response = client.execute(post);
			int a = response.getStatusLine().getStatusCode();
			if (a == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					infoString = EntityUtils.toString(entity);
					System.out.println(infoString + "µÇÂ¼·µ»Ø½á¹û");
					List<Cookie> cookies = client.getCookieStore().getCookies();
					manageCookie manage = (manageCookie) getApplication();
					manage.setCookies(cookies);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return cookies;
	}

	class Asy extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			http(name, passwd);
			System.out.println(2);
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			progressDialog.hide();
			progressDialog.dismiss();
			if (infoString.equals("pwd_error")) {
				Toast toast = Toast.makeText(dengLu.this, "ÃÜÂë´íÎó",
						Toast.LENGTH_SHORT);
				LinearLayout toastView = (LinearLayout) toast.getView();
				ImageView imageCodeProject = new ImageView(
						getApplicationContext());
				int icon = android.R.drawable.ic_delete;
				imageCodeProject.setImageResource(icon);
				toastView.addView(imageCodeProject, 0);
				toast.show();
			} else if (infoString.equals("email_error")) {
				Toast toast = Toast.makeText(dengLu.this, "ÓÊÏä´íÎó",
						Toast.LENGTH_SHORT);
				LinearLayout toastView = (LinearLayout) toast.getView();
				ImageView imageCodeProject = new ImageView(
						getApplicationContext());
				int icon = android.R.drawable.ic_delete;
				imageCodeProject.setImageResource(icon);
				toastView.addView(imageCodeProject, 0);
				toast.show();
			} else if (infoString.equals("0")) {
				Toast.makeText(dengLu.this, "ÇëÌîÂúÐÅÏ¢", Toast.LENGTH_SHORT).show();
			} else {
				boolean autologin = login.isChecked();
				if (autologin) {
					Editor editor = sp.edit();
					editor.putString("uname", name);
					editor.putString("upswd", passwd);
					editor.putBoolean("auto", true);
					editor.commit();
				} else {
					Editor editor = sp.edit();
					editor.putString("uname", null);
					editor.putString("upswd", null);
					editor.putBoolean("auto", false);
					editor.commit();
				}
				Intent intent = new Intent();
				intent.setClass(dengLu.this, MainActivity.class);
				Toast toast = Toast.makeText(dengLu.this, "µÇÂ¼³É¹¦",
						Toast.LENGTH_SHORT);
				LinearLayout toastView = (LinearLayout) toast.getView();
				ImageView imageCodeProject = new ImageView(
						getApplicationContext());
				imageCodeProject
						.setImageResource(R.drawable.confirm_dialog_successful_icon);
				toastView.addView(imageCodeProject, 0);
				toast.show();
				startActivity(intent);
				finish();
			}
			super.onPostExecute(result);
		}
	}
}
