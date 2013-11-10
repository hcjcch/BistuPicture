package com.example.detail;

import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;
import com.example.publicClass.downloadPicture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class detailedMainInformation extends Activity {

	private String img_large;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private LinearLayout linearLayout;
	private Bitmap bitmap;
	private ImageView imageView2;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detailedmaininformation);
		progressDialog = new ProgressDialog(detailedMainInformation.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("努力加载中！");
		progressDialog.setIndeterminate(false);
		linearLayout = (LinearLayout)findViewById(R.id.li);
		textView1 = (TextView) findViewById(R.id.name);
		textView2 = (TextView) findViewById(R.id.sex);
		textView3 = (TextView) findViewById(R.id.entry);
		textView4 = (TextView) findViewById(R.id.department);
		textView5 = (TextView) findViewById(R.id.like_num);
		textView1.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		textView3.setVisibility(View.GONE);
		textView4.setVisibility(View.GONE);
		textView5.setVisibility(View.GONE);
		imageView2 = (ImageView) findViewById(R.id.title_bar_menu_btn);
		final Intent intent = getIntent();
		String sex = intent.getStringExtra("sex");
		String name = intent.getStringExtra("name");
		img_large = intent.getStringExtra("img_large");
		String like_num = intent.getStringExtra("like_num");
		String entry = intent.getStringExtra("entry");
		String department = intent.getStringExtra("department");
		textView1.setText(name + "   ");
		imageView2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1.setClass(detailedMainInformation.this,
						MainActivity.class);
				startActivity(intent1);
			}
		});
		if (sex.equals("4")) {
			textView2.setText("校园奇景");
			textView3.setVisibility(View.GONE);
			textView4.setVisibility(View.GONE);
		} else if (sex.equals("1")) {
			textView2.setText("♂");
		} else {
			textView2.setText("♀");
		}
		textView3.setText(entry + "   ");
		if (department.equals("1")) {
			textView4.setText("机电学院");
		} else if (department.equals("2")) {
			textView4.setText("光电学院");
		} else if (department.equals("3")) {
			textView4.setText("自动化学院");
		} else if (department.equals("4")) {
			textView4.setText("通信学院");
		} else if (department.equals("5")) {
			textView4.setText("计算机学院");
		} else if (department.equals("6")) {
			textView4.setText("经济管理学院");
		} else if (department.equals("7")) {
			textView4.setText("信息管理学院");
		} else if (department.equals("8")) {
			textView4.setText("人文社科系");
		} else if (department.equals("9")) {
			textView4.setText("外国语学院");
		} else if (department.equals("10")) {
			textView4.setText("理学院");
		} else if (department.equals("11")) {
			textView4.setText("研究生部");
		} else if (department.equals("12")) {
			textView4.setText("其它院系");
		}

		textView5.setText("觉得很赞的人数有：" + like_num);
		linearLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					textView1.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					textView4.setVisibility(View.VISIBLE);
					textView5.setVisibility(View.VISIBLE);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					textView1.setVisibility(View.GONE);
					textView2.setVisibility(View.GONE);
					textView3.setVisibility(View.GONE);
					textView4.setVisibility(View.GONE);
					textView5.setVisibility(View.GONE);
				}
				return false;
			}
		});
		(new Asy()).execute();
	}

	@SuppressLint("NewApi")
	class Asy extends AsyncTask<String, String, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			bitmap = downloadPicture.downloadPicture("", img_large);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			Drawable drawable = new BitmapDrawable(result);
			linearLayout.setBackground(drawable);
			progressDialog.hide();
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

	}
}
