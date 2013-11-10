package com.example.upload;

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



public class uploadSecond {
	
	private String img_main;
	private String sex;
	private String entry;
	private String img_large;
	private String height;
	private String name;
	private String description;
	private String department;

	public uploadSecond(String img_main, String sex, String entry,
			String img_large, String height, String name, String description,
			String department) {
		// TODO Auto-generated constructor stub
		this.img_large = img_large;
		this.img_main = img_main;
		this.sex = sex;
		this.entry = entry;
		this.height = height;
		this.name = name;
		this.department = department;
		this.description = description;
	}
	
	String uploadFile(String cookie) {
		String infoString = null;
		System.out.println(img_main+"-------------");
		System.out.println(img_large+"-------------");
		System.out.println(department+"-------------");
		System.out.println(description+"-------------");
		System.out.println(name+"-------------");
		System.out.println(entry+"-------------");
		String urlString = "http://www.mybistu.sinaapp.com/request/upload.php";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		post.setHeader("cookie", "PHPSESSID=" + cookie);
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("Data[sex]", sex));
		pairs.add(new BasicNameValuePair("Data[department]", department));
		pairs.add(new BasicNameValuePair("Data[entry]", entry));
		pairs.add(new BasicNameValuePair("Data[img_large]", img_large));
		pairs.add(new BasicNameValuePair("Data[img_main]", img_main));
		pairs.add(new BasicNameValuePair("Data[height]", height));
		pairs.add(new BasicNameValuePair("Data[name]", name));
		pairs.add(new BasicNameValuePair("Data[description]", description));
		System.out.println(pairs.toString());
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("编码异常" + e.toString());
		}
		try {
			HttpResponse response = client.execute(post);
			int a = response.getStatusLine().getStatusCode();
			if (a == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String charset = "utf-8";
					infoString = EntityUtils.toString(entity, charset);
					System.out.println("二次上传" + infoString);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ClientProtocolException异常" + e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("输入输出流异常" + e.toString());
		}finally{
			 client.getConnectionManager().shutdown();
		}
		return infoString;
	}
}
