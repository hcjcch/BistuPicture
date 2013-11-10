package com.example.mybistupicyure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class getJsonMain {
	
	public String getJson(String page) {
		String JsonMain = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mybistu.sinaapp.com/api/home.json");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("page", page));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
			//post.setHeader("cookie", "PHPSESSID=" + cookie);
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
					JsonMain = EntityUtils.toString(entity);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonMain;
	}
}
