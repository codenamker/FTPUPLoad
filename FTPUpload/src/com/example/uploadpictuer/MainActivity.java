package com.example.uploadpictuer;

import java.io.ByteArrayOutputStream;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 2016年02月29日12:59:00 測試demo
 * 
 * 1.文件上傳到FTP服務器 
 * 2.從FTP服務器上下載文件
 * 
 * 所需jar包：commons-net-3.3.jar
 * 將commons-net-3.3.jar放於libs中
 * 
 * 原作者@author xiaoyaomeng
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	//Buttons
	private Button buttonUpLoad = null;
	private Button buttonDownLoad = null;
	
	//FTP工具類
	private FTPUtils ftpUtils = null;
	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//獲取控件對象
		buttonUpLoad = (Button) findViewById(R.id.button_upload);
		buttonDownLoad = (Button) findViewById(R.id.button_download);
		imageView=(ImageView) findViewById(R.id.imageView1);
		
		//設置控件對應相应函数
		buttonUpLoad.setOnClickListener(this);
		buttonDownLoad.setOnClickListener(this);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		//初始化和FTP服務器交互的類
		InitFTPServerSetting();
	}

	public void InitFTPServerSetting() {
		// TODO Auto-generated method stub
		ftpUtils = FTPUtils.getInstance();
		boolean flag = ftpUtils.initFTPSetting("自填IP位置", 21, "自填使用者名稱", "自填使用者密碼");
	
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
							case R.id.button_upload: {
								//上傳圖片
								ftpUtils.uploadFile(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "自填檔案位置"
				, "自填檔案名稱");
							}
								break;
							case R.id.button_download: {
								//下載圖片
								ftpUtils.downLoadFile(android.os.Environment
										.getExternalStorageDirectory().getAbsolutePath()
										+ "自填檔案位置"+"自填檔案副檔名Ex. .png "
										, "自填存成什麼樣的檔案名字與副檔名Ex. test.png");
								
								  
				                  Bitmap bm = BitmapFactory.decodeFile(android.os.Environment
											.getExternalStorageDirectory().getAbsolutePath()
											+ "自填檔案路徑檔名與副檔名");
				                 
				                  imageView.setImageBitmap(bm);
							}
								break;
							default:
								break;
					
			}
	}
}
