package com.example.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.mybistupicyure.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class superAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> a;
    private int b;
    
    public superAdapter(Context Context,List<Map<String, Object>> data,int b) {
		// TODO Auto-generated constructor stub
    	this.mContext = Context;
    	a = data;
    	this.b = b;
	}




	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return a.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return a.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addData(ArrayList<Map<String, Object>> Data) {
		for (Map<String, Object> map : Data) {
			a.add(map);
		}
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.superpicturelistview, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView)arg1.findViewById(R.id.superpicture);
			viewHolder.mTextView = (TextView)arg1.findViewById(R.id.namelist);
			viewHolder.textView = (TextView)arg1.findViewById(R.id.like_numlist);
			arg1.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		String name = (String) a.get(arg0).get("name");
		viewHolder.mTextView.setText("ÐÕÃû:"+"\n"+name);
		String like_numlist = (String)a.get(arg0).get("like_num");
		viewHolder.textView.setText("ÔÞ:"+"\n"+like_numlist);
		Bitmap bitmap = (Bitmap) a.get(arg0).get("img");
		/*Matrix matrix = new Matrix();
		matrix.postScale(0.5f,0.5f);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);*/
		viewHolder.mImageView.setImageBitmap(bitmap);
		return arg1;
	}

	static class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
		TextView textView;
	}
}
