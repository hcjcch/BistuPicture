package com.example.personal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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


public class personSuper {
	
	private String cookie;
	public static String total;
	
	private List<personSuperData> personSuper = new ArrayList<personSuperData>();
	
	public personSuper(String cookie) {
		// TODO Auto-generated constructor stub
		this.cookie = cookie;
	}
	public String getinformation(String page) {
		String superString = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mybistu.sinaapp.com/api/user_like.php");
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
	
	public List<personSuperData> dealPersonSuper(String information){
		
		try {
			JSONObject jsonObject = new JSONObject(information);
			total = jsonObject.getString("total");
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			for (int i = 0; i < jsonArray.length(); i++) {
				personSuperData superData = new personSuperData();
				JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
				String id = jsonObject2.getString("id");
				String name = (new org.json.JSONTokener(jsonObject2.getString("name")).nextValue().toString());
				String img_main = jsonObject2.getString("img_main");
				String like_num = jsonObject2.getString("like_num");
				superData.set_id(id);
				superData.set_name(name);
				superData.set_imgmain(img_main);
				superData.set_likenum(like_num);
				personSuper.add(superData);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return personSuper;
	}
}
