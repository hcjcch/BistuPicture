package com.example.publicClass;


import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Application;

public class manageCookie extends Application{
	private List<Cookie> cookies;
	public List<Cookie> getCookies(){
		return cookies;
	}
	public void setCookies(List<Cookie> cookies){
		this.cookies = cookies;
	}
}
