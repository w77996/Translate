package com.wtwd.translate.utils.micro;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import static com.wtwd.translate.utils.Utils.createFile;
import static com.wtwd.translate.utils.Utils.existSDCard;

public class FilePath {
	private String filePath;
	private String imagePath;
    public static String getImgLocalPath(){
	   String path = PropertiesPath.getProPath("configuration.properties","image.path");
	   File file = new File(path);
	   if(!file.exists()){
		   file.mkdirs();
	   }
	   return PropertiesPath.getProPath("configuration.properties","image.path");
    }
    
    public static String getAudioLocalPath(){
	   String path = PropertiesPath.getProPath("configuration.properties","audio.path");
	   File file = new File(path);
	   if(!file.exists()){
		   file.mkdirs();
	   }
	   return PropertiesPath.getProPath("configuration.properties","audio.path");
    }


    public static String getAudioPath(){
		File voiceFile;
		if (existSDCard())
			voiceFile = new File(Environment.getExternalStorageDirectory(), "/voice");
		else voiceFile = Environment.getDataDirectory();
		Log.e("FilePath",voiceFile.getAbsolutePath());
		//voiceFile = createFile(voiceFile, "voice_", ".3pg");
		// mVoiceFilePath = Environment.getExternalStorageState()+ File.separator + System.currentTimeMillis() + ".3pg";

		return voiceFile.getAbsolutePath();
	}


    public static String getViolateImgLocalPath() {
       String path = PropertiesPath.getProPath("configuration.properties","violateimage.path");
  	   File file = new File(path);
  	   if(!file.exists()){
  		   file.mkdirs();
  	   }
  	   return PropertiesPath.getProPath("configuration.properties","violateimage.path");
    }
   
	public String getFilePath() {
		return filePath;
	}
   public static String getImgHttpTempPath(){
	   return PropertiesPath.getProPath("configuration.properties","image.temp.http");
   }
   /**
    * 获取图片网络路径
    * @return
    */
   public static String getImgHttpPath(){
	   return PropertiesPath.getProPath("configuration.properties","image.http");
   }
   
   public static String getAudioHttpPath() {
	   return PropertiesPath.getProPath("configuration.properties","audio.http");
   }
   
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
