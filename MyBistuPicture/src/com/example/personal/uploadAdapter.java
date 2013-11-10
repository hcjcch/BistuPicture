package com.example.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import com.example.mybistupicyure.R;
import com.example.personal.superAdapter.ViewHolder;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class uploadAdapter extends BaseAdapter{

	private Context mContext;
	private List<Map<String, Object>> uploadData;
	public uploadAdapter(Context Context,List<Map<String, Object>> a) {
		// TODO Auto-generated constructor stub
		this.mContext = Context;
		this.uploadData = a;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return uploadData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void addData(ArrayList<Map<String, Object>> map) {
		for (Map<String, Object> map2 : map) {
			uploadData.add(map2);
		}
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.uploadpicturelistview, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView)arg1.findViewById(R.id.uploadpicture);
			viewHolder.mTextView = (TextView)arg1.findViewById(R.id.uploadname);
			viewHolder.textView = (TextView)arg1.findViewById(R.id.uploadtime);
			arg1.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		String name = (String) uploadData.get(arg0).get("name");
		viewHolder.mTextView.setText("姓名:"+"\n"+name);
		String uploadtime = (String)uploadData.get(arg0).get("pb_time");
		viewHolder.textView.setText("上传时间:"+"\n"+uploadtime);
		Bitmap bitmap = (Bitmap) uploadData.get(arg0).get("bitmap");
		/*Matrix matrix = new Matrix();
		matrix.postScale(0.5f,0.5f);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);*/
		viewHolder.mImageView.setImageBitmap(bitmap);
		return arg1;
	}
	private static class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
		TextView textView;
	}
}
