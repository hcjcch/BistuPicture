package com.example.mybistupicyure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;


import com.example.dengLu.dengLu;
import com.example.detail.detailedMainInformation;
import com.example.mybistupicyure.LazyScrollView.OnScrollListener;
import com.example.publicClass.manageCookie;
import com.example.upload.UploadfileActivity;
import com.example.zhuce.logon;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static int changePage = 1;
	private int allpage;
	private int total;
	private Button formpage;
	private Button nextpage;
	private TextView logon;
	private String cookie;
	private TextView personal;
	private TextView dengLuButton;
	private SlideMenu slideMenu;
	private TextView uploadfile;
	
	private LazyScrollView waterfall_scroll;//自定义类
	
	private LinearLayout waterfall_container;//包含着三列
	private ArrayList<LinearLayout> waterfall_items;//三列
	
	private Display display; //提供关于屏幕尺寸和分辨率的信息
	private List<Map<String, String>> mainInformationList;

	private int itemWidth;//每列宽度

	private int column_count = 3;// 显示列数
	private int page_count = 15;// 每次加载15张图片
	private int current_page = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		display = this.getWindowManager().getDefaultDisplay();//计算屏幕宽度
		itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小（三列）
		logon = (TextView)findViewById(R.id.logon);
		formpage = (Button)findViewById(R.id.formPage);
		nextpage = (Button)findViewById(R.id.nextPage);
		formpage.setVisibility(View.GONE);
		nextpage.setVisibility(View.GONE);
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImg = (ImageView) findViewById(R.id.title_bar_menu_btn);
		
		menuImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.title_bar_menu_btn:
					if (slideMenu.isMainScreenShowing()) {
						slideMenu.openMenu();
					} else {
						slideMenu.closeMenu();
					}
					break;
				}
			}
		});
		
		dengLuButton = (TextView)findViewById(R.id.dengLuButton);
		dengLuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,dengLu.class);
				startActivityForResult(intent,1);
			}
		});
		
		personal = (TextView)findViewById(R.id.personal);
		personal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,com.example.personal.personal.class);
				startActivity(intent);
			}
		});
		
		uploadfile = (TextView)findViewById(R.id.uploadfile);
		uploadfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,UploadfileActivity.class);
				startActivity(intent);
			}
		});
		formpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				current_page = 0;
				if (changePage == 1) {
					Toast.makeText(MainActivity.this, "已经是第一页", Toast.LENGTH_SHORT).show();
				}else {
					waterfall_container.removeAllViews();
					InitLayout((--changePage)+"");
				}
			}
		});
		logon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, logon.class);
				startActivity(intent);
			}
		});
		nextpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				current_page = 0;
				if (changePage > allpage) {
					Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
				}else {
					waterfall_container.removeAllViews();
					InitLayout((++changePage)+"");
					
				}
			}
		});
		InitLayout(changePage+"");
	}
	
	private void InitLayout(String page) {
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		waterfall_scroll.getView();
		waterfall_scroll.setOnScrollListener(new OnScrollListener() {
			
			
			@Override
			public void onTop() {
				// 滚动到最顶端
				Log.d("LazyScroll", "Scroll to top");
			}

			@Override
			public void onScroll() {
				formpage.setVisibility(View.GONE);
				nextpage.setVisibility(View.GONE);
				// 滚动丿				Log.d("LazyScroll", "Scroll");
			}

			@Override
			public void onBottom() {
				// 滚动到最低端
				AddItemToContainer(++current_page, page_count);//参数为当前页数和每页的张数
			}
		});
		
		waterfall_container = (LinearLayout) this
				.findViewById(R.id.waterfall_container);
		
		waterfall_items = new ArrayList<LinearLayout>();

		//制造三列图片
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					itemWidth, LayoutParams.WRAP_CONTENT);
			// itemParam.width = itemWidth;
			// itemParam.height = LayoutParams.WRAP_CONTENT;
			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);
			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}

		//各图片名称
		cookie = getCookie();
		String mainInformation = (new getJsonMain()).getJson(page);
		mainDealJson deal = new mainDealJson();
		deal.dealJson(mainInformation);
		mainInformationList = deal.get_mainInformationList();
		total = Integer.parseInt(deal.get_Total());
		System.out.println(total);
		allpage = total/90+1;
		if (current_page == 0) {
			AddItemToContainer(++current_page, page_count);
		}
		
	}
	
	private void AddItemToContainer(int pageindex, int pagecount) {//第一次加载pageindex==1,pagecount == 15
		int j = 0;
		int imagecount = mainInformationList.size();
		for (int i = (pageindex - 1 )* pagecount; i < pagecount * (pageindex )
				&& i < imagecount; i++) {
			j = j >= column_count ? j = 0 : j;
			AddImage(mainInformationList.get(i).get("img_main"), j++,i);
		}
		if (current_page>=6) {
			formpage.setVisibility(View.VISIBLE);
			nextpage.setVisibility(View.VISIBLE);
		}
	}
	
	private void AddImage(String filename, int columnIndex,final int i) {
		ImageView item = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.waterfallitem, null);//找到已找到一张图片放在imagView上
		waterfall_items.get(columnIndex).addView(item);
		ImageLoaderTask task = new ImageLoaderTask(item,cookie,itemWidth,filename);
		task.execute();
		item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("sex", mainInformationList.get(i).get("sex"));
				intent.putExtra("name",mainInformationList.get(i).get("name"));
				intent.putExtra("img_large",mainInformationList.get(i).get("img_large"));
				intent.putExtra("like_num", mainInformationList.get(i).get("like_num"));
				intent.putExtra("entry",mainInformationList.get(i).get("entry"));
				intent.putExtra("department", mainInformationList.get(i).get("department"));
				intent.setClass(MainActivity.this, detailedMainInformation.class);
				startActivity(intent);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	
	
}
