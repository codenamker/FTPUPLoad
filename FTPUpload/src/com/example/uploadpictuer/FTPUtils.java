package com.example.uploadpictuer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.R.bool;
import android.util.Log;

/**
 * 2016年02月29日12:59:00
 * 
 * 用於Android和FTP進行交換的工具類
 * 
 * 原作者@author xiaoyaomeng
 * 
 */
public class FTPUtils {
	private FTPClient ftpClient = null;
	private static FTPUtils ftpUtilsInstance = null;
	private String FTPUrl;
	private int FTPPort;
	private String UserName;
	private String UserPassword;

	private FTPUtils() {
		ftpClient = new FTPClient();
	}

	/*
	 * 得道類對象實例(因為只能有一個這樣的類對象，所以用單例模式)
	 */
	public static FTPUtils getInstance() {
		if (ftpUtilsInstance == null) {
			ftpUtilsInstance = new FTPUtils();
		}
		return ftpUtilsInstance;
	}

	/**
	 * 設置FTP
	 * 
	 * @param FTPUrl
	 *            FTP ip地址
	 * @param FTPPort
	 *            FTP 端口號碼
	 * @param UserName
	 *            登入FTP 帳號
	 * @param UserPassword
	 *            登入 FTP 密碼
	 * @return
	 */
	public boolean initFTPSetting(String FTPUrl, int FTPPort, String UserName,
			String UserPassword) {
		this.FTPUrl = FTPUrl;
		this.FTPPort = FTPPort;
		this.UserName = UserName;
		this.UserPassword = UserPassword;

		int reply;

		try {
			// 1.要連結的FTP的Url,Port
			ftpClient.connect(FTPUrl, FTPPort);

			// 2.登入FTP服務器
			ftpClient.login(UserName, UserPassword);

			// 3.看返回的值是不是230，如果是，表示成功
			reply = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				// 斷開
				ftpClient.disconnect();
				return false;
			}

			return true;

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 上傳文件
	 * 
	 * @param FilePath
	 *            要上傳文件所在SDCard的路徑
	 * @param FileName
	 *            要上傳的文件的文件名(如：Sim唯一標示碼)
	 * @return true成功，false失敗
	 */
	public boolean uploadFile(String FilePath, String FileName) {

		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
				return false;
			}
		}

		try {

			// 設置儲存路徑			
			ftpClient.makeDirectory("/data");
			ftpClient.changeWorkingDirectory("/data");

			// 設置上傳文件需要的一些基本信息
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//圖片

			// 文件上傳吧～
			FileInputStream fileInputStream = new FileInputStream(FilePath);
			ftpClient.storeFile(FileName, fileInputStream);

			// 關閉文件流
			fileInputStream.close();

			// 退出FTP，關閉ftpCLient的連結
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 下載文件
	 * 
	 * @param FilePath
	 *            要存放的文件的路徑
	 * @param FileName
	 *            遠端FTP伺服器上文件的名字
	 * @return true成功，false失敗
	 */

	public boolean downLoadFile(String FilePath, String FileName) {

		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
				return false;
			}
		}

		try {
			// 轉到指定下載目錄
			ftpClient.changeWorkingDirectory("/data");

			// 列出該目錄下所有文件
			FTPFile[] files = ftpClient.listFiles();

			// 找到指定文件
			for (FTPFile file : files) {
				if (file.getName().equals(FileName)) {
					// 根據絕對路徑初始化文件
					File localFile = new File(FilePath);
					if (!localFile.exists()) {
						Log.i("mytest", "沒有檔案需要創建");
						localFile.createNewFile();						
					}
					
					Log.i("mytest", "經過" + FilePath);
					// 輸出
					OutputStream outputStream = new FileOutputStream(localFile);
					ftpClient.setBufferSize(1024); 

					ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
					// 下載
					ftpClient.retrieveFile(file.getName(), outputStream);
					Log.i("mytest", "找到" + file.getName());
					// 關閉流
					outputStream.close();
				}
			}

			// 退出登入FTP，關閉ftpCLient的連接
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("mytest", "錯誤" + e);
			e.printStackTrace();
		}

		return true;
	}

}
