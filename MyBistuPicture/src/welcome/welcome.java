package welcome;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.mybistupicyure.MainActivity;
import com.example.mybistupicyure.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class welcome extends Activity {
	private static final long SPLASH_DELAY_MILLIS = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		if (isNetworkConnected(this)) {
			if (testNet()) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						go();
					}
				}, SPLASH_DELAY_MILLIS);
			} else {
				Toast.makeText(welcome.this, "网络未连接！", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}
		else {
			Toast.makeText(welcome.this, "网络未连接！", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}

	private void go() {
		Intent intent = new Intent(welcome.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean testNet() {
		boolean a = true;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://www.baidu.com");
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = "utf-8";
				String content = "";
				content = EntityUtils.toString(entity, charset);
				if (content.contains("DOCTYPE")) {
					a = true;
				} else {
					a = false;
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			a = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			a = false;
			e.printStackTrace();
		}
		return a;
	}

	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
