package com.example.mybistupicyure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mainDealJson {
	
	private List<Map<String, String>> mainInformationList = new ArrayList<Map<String,String>>();
	private String total;
	
	public void dealJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			total= jsonObject.getString("total");
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
				String sex = jsonObject2.getString("sex");
				String id = jsonObject2.getString("id");
				String name = (new org.json.JSONTokener(jsonObject2.getString("name")).nextValue().toString());
				//String name = jsonObject2.getString("name");
				String img_main = jsonObject2.getString("img_main");
				String img_large = jsonObject2.getString("img_large");
				String like_num = jsonObject2.getString("like_num");
				String entry = jsonObject2.getString("entry");
				String department = jsonObject2.getString("department");
				map.put("sex", sex);
				map.put("department", department);
				map.put("id", id);
				map.put("name", name);
				map.put("img_main", img_main);
				map.put("img_large", img_large);
				map.put("like_num", like_num);
				map.put("entry", entry);
				try {
					mainInformationList.add(map);
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Map<String, String>> get_mainInformationList() {
		return mainInformationList;
	}
	public String get_Total() {
		return total;
	}
}
