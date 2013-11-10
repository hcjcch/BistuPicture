package com.example.upload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dengLu.dengLu;
import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.personal.personal;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class uploadInformation extends Activity {

	private String cookie;
	private String pictureInformation;
	private Button startUpload;
	private String sexString;
	private Spinner sexSpinner;
	private Spinner departmentSpinner;
	private String department;
	private Spinner entrySpinner;
	private String entryString;
	private TextView suoshuxueyuan;
	private TextView ruxueshijian;
	private EditText nameTextView;
	private String nameString;
	private EditText descriptionTextView;
	private String descriptionString;
	private Bitmap mainBitmap;
	private ProgressDialog progressDialog;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uploadinformation);
		imageView = (ImageView) findViewById(R.id.title_bar_menu_btn);
		cookie = getCookie();
		progressDialog = new ProgressDialog(uploadInformation.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在加载！");
		progressDialog.setIndeterminate(false);
		if (cookie == null) {
			Toast.makeText(uploadInformation.this, "与服务器中断,请重新连接",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(uploadInformation.this, dengLu.class);
			startActivity(intent);
			finish();
		}
		sexSpinner = (Spinner) findViewById(R.id.sex);
		departmentSpinner = (Spinner) findViewById(R.id.department);
		entrySpinner = (Spinner) findViewById(R.id.entry);
		suoshuxueyuan = (TextView) findViewById(R.id.suoshuxueyuan);
		ruxueshijian = (TextView) findViewById(R.id.ruxueshijian);
		nameTextView = (EditText) findViewById(R.id.name);
		descriptionTextView = (EditText) findViewById(R.id.description);
		startUpload = (Button) findViewById(R.id.startupload);

		Intent intent = getIntent();
		pictureInformation = intent.getStringExtra("information");

		sexSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sexString = arg0.getItemAtPosition(arg2).toString();
				if (sexString.equals("女神")) {
					sexString = "2";
				} else if (sexString.equals("宅男")) {
					sexString = "1";
				} else {
					sexString = "4";
				}
				if (sexString.equals("4")) {
					suoshuxueyuan.setVisibility(View.GONE);
					departmentSpinner.setVisibility(View.GONE);
					ruxueshijian.setVisibility(View.GONE);
					entrySpinner.setVisibility(View.GONE);
				}
				if (sexString.equals("2") || sexString.equals("1")) {
					suoshuxueyuan.setVisibility(View.VISIBLE);
					departmentSpinner.setVisibility(View.VISIBLE);
					ruxueshijian.setVisibility(View.VISIBLE);
					entrySpinner.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(uploadInformation.this, "没有选中任何分类",
						Toast.LENGTH_SHORT).show();
			}
		});
		departmentSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						department = arg0.getItemAtPosition(arg2).toString();
						ArrayList<String> departmeniList = new ArrayList<String>();
						departmeniList.add("机电学院");
						departmeniList.add("光电学院");
						departmeniList.add("自动化学院");
						departmeniList.add("通信学院");
						departmeniList.add("计算机学院");
						departmeniList.add("经济管理学院");
						departmeniList.add("信息管理学院");
						departmeniList.add("人文社科系");
						departmeniList.add("外国语学院");
						departmeniList.add("理学院");
						departmeniList.add("研究生部");
						departmeniList.add("其它院系");
						int i = 1;
						for (String string : departmeniList) {
							if (department.equals(string)) {
								department = i + "";
							}
							i++;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(uploadInformation.this, "没有选中任何学院",
								Toast.LENGTH_SHORT).show();
					}
				});
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(uploadInformation.this, MainActivity.class);
				startActivity(intent);
			}
		});
		entrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				entryString = arg0.getItemAtPosition(arg2).toString();
				ArrayList<String> entry = new ArrayList<String>();
				entry.add("2013");
				entry.add("2012");
				entry.add("2011");
				entry.add("2010");
				entry.add("2009");
				entry.add("2008");
				entry.add("2007");
				entry.add("2006");
				entry.add("2005");
				entry.add("2004");
				entry.add("2003");
				entry.add("2002");
				entry.add("2001");
				entry.add("2000");
				entry.add("1999");
				entry.add("1998");
				entry.add("1997");
				entry.add("1996");
				int i = 2013;
				for (String string : entry) {
					if (entryString.equals(string)) {
						entryString = i + "";
					}
					i--;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(uploadInformation.this, "没有选中任何入学时间",
						Toast.LENGTH_SHORT).show();
			}
		});

		nameTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (nameTextView.getText().toString().equals("照片名称")) {
					nameTextView.setText("");
				}
			}
		});

		descriptionTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (descriptionTextView.getText().toString().equals("描述(选填)")) {
					descriptionTextView.setText("");
				}
			}
		});
		startUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				(new AsyncTask2()).execute();
			}
		});
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

	// 获取本地图片
	private Bitmap getLoacalBitmap(String url) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Map<String, String> map(String resultString) {
		Map<String, String> maps = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(resultString);
			String result = jsonObject.getString("result");
			String img_large = jsonObject.getString("img_large");
			String img_main = jsonObject.getString("img_main");
			String height = jsonObject.getString("height");
			maps.put("result", result);
			maps.put("img_large", img_large);
			maps.put("img_main", img_main);
			maps.put("height", height);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maps;
	}

	class AsyncTask2 extends AsyncTask<String, String, String> {

		String backString;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			cookie = getCookie();
			Map<String, String> map1 = new HashMap<String, String>();
			// 将第一次的数据解析
			Map<String, String> map = new HashMap<String, String>();
			map = map(pictureInformation);
			String img_large = map.get("img_large");
			String img_main = map.get("img_main");
			String height = map.get("height");
			System.out.println(img_large);
			System.out.println(img_main);
			System.out.println(height);
			nameString = nameTextView.getText().toString();
			descriptionString = descriptionTextView.getText().toString();
			uploadSecond uploadSecond = new uploadSecond(img_main, sexString,
					entryString, img_large, height, nameString,
					descriptionString, department);
			String resultString = uploadSecond.uploadFile(cookie);
			return resultString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result.equals("success")) {
				Toast toast = Toast.makeText(uploadInformation.this, "恭喜,上传成功",
						Toast.LENGTH_SHORT);
				LinearLayout toastView = (LinearLayout) toast.getView();
				ImageView imageCodeProject = new ImageView(
						getApplicationContext());
				imageCodeProject
						.setImageResource(R.drawable.confirm_dialog_successful_icon);
				toastView.addView(imageCodeProject, 0);
				if (mainBitmap != null && !mainBitmap.isRecycled()) {
					mainBitmap.recycle();
					System.gc();
					mainBitmap = null;
				}
				Intent intent = new Intent();
				intent.setClass(uploadInformation.this, MainActivity.class);
				toast.show();
				progressDialog.hide();
				startActivity(intent);
				finish();
				super.onPostExecute(result);
			} else {
				Toast.makeText(uploadInformation.this, "sorry,上传失败!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
