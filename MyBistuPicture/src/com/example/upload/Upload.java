package com.example.upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Upload {
	private String cookie = null;
	private String srcPath = null;
	public Upload(String cookie,String srcPath) {
		// TODO Auto-generated constructor stub
		this.cookie = cookie;
		this.srcPath = srcPath;
	}
	String uploadFile() throws IOException {
		String uploadUrl = "http://www.mybistu.sinaapp.com/request/upload.php";
		String result = "";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "----------Ij5ei4cH2KM7KM7ei4cH2gL6Ef1Ij5";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setRequestProperty("Cookie", this.cookie);
			// httpURLConnection.setChunkedStreamingMode(128 * 1024);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"Filedata\"; filename=\""
					+ srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			dos.writeBytes(end);
			FileInputStream fis = new FileInputStream(srcPath);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();
			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			result = br.readLine();
			System.out.println("返回结果" + result);
			//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			dos.close();
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接异常" + end.toString());
		}
		return result;
	}
}
