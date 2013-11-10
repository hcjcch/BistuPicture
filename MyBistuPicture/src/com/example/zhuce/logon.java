package com.example.zhuce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.personal.personal;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class logon extends Activity {

	private EditText emial;
	private EditText passwd;
	private EditText confirmpasswd;
	private EditText nickname;
	private RadioGroup sexGroup;
	private RadioButton manButton;
	private RadioButton womanButton;
	private String emialString;
	private String passwdString;
	private String confirmpasswdString;
	private String nicknameString;
	private String sexString;
	private Button logonButton;
	private String infoString;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logon);
		emial = (EditText) findViewById(R.id.editText1);
		passwd = (EditText) findViewById(R.id.editText2);
		confirmpasswd = (EditText) findViewById(R.id.editText3);
		nickname = (EditText) findViewById(R.id.editText4);
		sexGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		manButton = (RadioButton) findViewById(R.id.radio0);
		womanButton = (RadioButton) findViewById(R.id.radio1);
		logonButton = (Button) findViewById(R.id.button1);
		imageView = (ImageView)findViewById(R.id.title_bar_menu_btn);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(logon.this, MainActivity.class);
				startActivity(intent);
			}
		});
		emial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (emial.getText().toString().equals("邮箱(建议使用常用邮箱)")) {
					emial.setText("");
				}
			}
		});
		
		passwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (passwd.getText().toString().equals("密码(建议6~18位字母与数字组合)")) {
					passwd.setText("");
				}
			}
		});
		confirmpasswd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (confirmpasswd.getText().toString().equals("确认密码")) {
					confirmpasswd.setText("");
				}
			}
		});
		nickname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nickname.getText().toString().equals("昵称")) {
					nickname.setText("");
				}
			}
		});
		sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == manButton.getId()) {
					sexString = "1";
				} else {
					sexString = "2";
				}
			}
		});

		logonButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				emialString = emial.getText().toString();

				if (checkEmail(emialString)) {
					confirmpasswdString = confirmpasswd.getText().toString();
					passwdString = passwd.getText().toString();

					nicknameString = nickname.getText().toString();
					if (sexGroup.getCheckedRadioButtonId() == manButton.getId()) {
						sexString = "1";
					} else if (sexGroup.getCheckedRadioButtonId() == womanButton
							.getId()) {
						sexString = "2";
					}
					if (confirmpasswdString.equals(passwdString)) {
						if (confirmpasswdString == null
								|| nicknameString.equals("昵称")
								|| nicknameString == "") {
							Toast.makeText(logon.this, "每一项都必须填",
									Toast.LENGTH_SHORT).show();
						} else {
							logonLe();
							if (infoString.equals("0")) {
								Toast.makeText(logon.this, "注册失败",
										Toast.LENGTH_SHORT).show();
							}
							else {
								Toast.makeText(logon.this, infoString,
										Toast.LENGTH_SHORT).show();
							}
						}

					} else {
						Toast.makeText(logon.this, "两次输入的密码不一样",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(logon.this, "请输入正确的邮箱", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private String logonLe() {
		System.out.println(1);
		HttpClient client = new DefaultHttpClient();
		System.out.println(2);
		HttpPost post = new HttpPost(
				"http://mybistu.sinaapp.com/request/register.php");
		System.out.println(3);
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		System.out.println(4);
		pairs.add(new BasicNameValuePair("Data[email]", emialString));
		pairs.add(new BasicNameValuePair("Data[pwd]", passwdString));
		pairs.add(new BasicNameValuePair("Data[nickname]", nicknameString));
		pairs.add(new BasicNameValuePair("Data[sex]", sexString));
		System.out.println(5);
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
			System.out.println(6);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("UnsupportedEncodingException" + e.getMessage());
			e.printStackTrace();
		}
		try {
			System.out.println(7);
			HttpResponse response = client.execute(post);
			System.out.println(8);
			int a = response.getStatusLine().getStatusCode();
			System.out.println(9);
			if (a == 200) {
				System.out.println(10);
				HttpEntity entity = response.getEntity();
				System.out.println(entity + "{}{}}{}{}{}");
				if (entity != null) {
					infoString = EntityUtils.toString(entity);
					System.out.println(infoString + "注册返回结果");
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("ClientProtocolException" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private boolean checkEmail(String email) {
		boolean flag = true;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
