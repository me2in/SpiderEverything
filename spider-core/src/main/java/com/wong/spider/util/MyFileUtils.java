package com.wong.spider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * 文件操作工具类
 * @author weien
 *
 */
public class MyFileUtils {
	
	public static boolean makedir(String filePath){
		File dir = new File(filePath);
		if(!dir.exists()){
			return dir.mkdirs();
		}
		return true;
	}
	
	public static boolean isEmptyFile(String filePath){
		File f = new File(filePath);
		return !f.exists() || f.length() == 0;
	}
	
	public static boolean makeParentdir(String filePath){
		File dir = new File(filePath).getParentFile();
		if(!dir.exists()){
			return dir.mkdirs();
		}
		return true;
	}
	
	public static boolean isExists(String filePath){
		return new File(filePath).exists();
	}
	
	public static boolean isDirExists(String filePath){
		return new File(filePath).getParentFile().exists();
	}
	
	public static void writeString(String filePath,String str){
		if(!isDirExists(filePath)){
			makeParentdir(filePath);
		}
		try {
			FileUtils.writeStringToFile(new File(filePath), str,StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeStringAppend(String filePath,String str){
		if(!isDirExists(filePath)){
			makeParentdir(filePath);
		}
		try {
			FileUtils.writeStringToFile(new File(filePath), str,StandardCharsets.UTF_8,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String filePath,byte[] data){
		if(!isDirExists(filePath)){
			makeParentdir(filePath);
		}
		try {
			FileUtils.writeByteArrayToFile(new File(filePath), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readFromFile(String filePath) throws IOException{
		File file = new File(filePath);
		if(file.exists() && file.canRead()){
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line = "";
			StringBuffer result = new StringBuffer();
			while((line=br.readLine())!=null){
				result.append(line+"\n");
			}
			return result.toString();
		}
		return "";
	}
	
	public static void main(String[] args) throws IOException {
//		MyFileUtils.readFromFile("C:/Users/weien/Desktop/spider/info.html");
		MyFileUtils.writeStringAppend("F:/movieSpiderV2/圣诞坏公公2 Bad Santa 2 (2016)/movie_info.txt", "fdsfsdfsdfasd");
	}
}
