package com.wtwd.translate.utils.micro;

import java.io.IOException;
import java.util.Properties;

/**
 * Properties读取工具类。
 * 读取Properties文件用
 * */
public class PropertiesPath {

	public static Properties getPro(String resourcePath) {
		Properties pro = null;
		try {
			pro = new Properties();
			pro.load(PropertiesPath.class.getClassLoader().getResourceAsStream(
					resourcePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro;
	}
	
	public static String getProPath(String resourcePath,String path) {
		Properties pro = null;
		String filePath = null;
		try {
			pro = new Properties();
			pro.load(PropertiesPath.class.getClassLoader().getResourceAsStream(
					resourcePath));
			filePath = pro.getProperty(path)+"/";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	
	public static String getProContent(String resourcePath,String path) {
		Properties pro = null;
		String filePath = null;
		try {
			pro = new Properties();
			pro.load(PropertiesPath.class.getClassLoader().getResourceAsStream(
					resourcePath));
			filePath = pro.getProperty(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
}
