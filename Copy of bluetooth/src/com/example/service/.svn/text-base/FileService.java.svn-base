package com.example.service;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * 此类用来为文件相关操作进行服务
 * 
 * @author Administrator
 * 
 */
public class FileService {
	/**
	 * 此方法用来在内存卡上存储数据,创建目录.
	 * 
	 * @return result 返回目录表示目录是否创建成功
	 * @author Administrator
	 * 
	 */
	public boolean createDir(String path) {
		boolean result = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File file = new File(path);
			result = file.mkdir();
			System.out.println("目录创建成功!");
		} else {
			System.out.println("内存卡不可用!");
		}
		return result;
	}

	/**
	 * 本方法实现创建文件
	 * 
	 * @param path
	 * @return 文件不存在时候,文件是否创建成功
	 * @throws IOException
	 */
	public boolean createFile(String path) throws IOException {
		boolean result = false;
		File f = new File(path);
		// 如果文件不存在,创建目录
		if (!f.exists()) {
			f.createNewFile();
		}
		// 文件存在,也返回真
		if (f.exists()) {
			result = true;
		}
		return result;
	}
}
