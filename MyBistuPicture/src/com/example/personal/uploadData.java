package com.example.personal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;


public class uploadData {
	
	private String cookie;
	public static String total1;
	
	private ArrayList<Map<String, Object>> uploadData;
	
	public uploadData(String cookie) {
		// TODO Auto-generated constructor stub
		this.cookie = cookie;
	}
	public String getinformation(String page) {
		String superString = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mybistu.sinaapp.com/api/user_success_upload.json");
		post.setHeader("cookie", "PHPSESSID=" + cookie);
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("page", page));
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
					superString = EntityUtils.toString(entity);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return superString;
	}
	
	public ArrayList<Map<String, Object>> dealuploadData(String information){
		uploadData = new ArrayList<Map<String,Object>>();
		try {
			JSONObject jsonObject = new JSONObject(information);
			total1 = jsonObject.getString("total");
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
				String id = jsonObject2.getString("id");
				String name = (new org.json.JSONTokener(jsonObject2.getString("name")).nextValue().toString());
				String img_main = jsonObject2.getString("img_main");
				String like_num = jsonObject2.getString("like_num");
				String img_large = jsonObject2.getString("img_large");
				String sex = jsonObject2.getString("sex");
				String entry = jsonObject2.getString("entry");
				String department = jsonObject2.getString("department");
				String pb_time  = jsonObject2.getString("pb_time");
				Bitmap bitmap = (new personal()).getBitmap(img_main);
				map.put("id", id);
				map.put("name",name );
				map.put("bitmap", bitmap);
				map.put("like_num", like_num);
				map.put("img_large", img_large);
				map.put("sex", sex);
				map.put("entry", entry);
				map.put("department", department);
				map.put("pb_time", pb_time);
				uploadData.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadData;
	}
}
