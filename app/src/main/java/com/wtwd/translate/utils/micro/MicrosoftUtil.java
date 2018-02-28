package com.wtwd.translate.utils.micro;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;




import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MicrosoftUtil {

    public static final String TAG = "MicrosoftUtil";

    public static final String OCR_SUBSCRIPTION_KEY = "b91a43a99b6748539f274562b8f96d08";
    public static final String TRANSLATE_SUBSCRIPTION_KEY = "c6acb3875fe148558e9e6eb1f221cb1b";
    public static final String SPEECH_SUBSCRIPTION_KEY = "0571368c17814d54a6cfe3231e6eccbb";


    private static String textResult = "";


	public static Map<String,String> ocr(String path,String from) {
		//HttpClient httpclient = HttpClients.createDefault();
		Map<String,String> resultMap = new HashMap<String,String>();
       /* try
        {


            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/vision/v1.0/ocr");

            builder.setParameter("language", "unk");
            builder.setParameter("detectOrientation ", "true");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", OCR_SUBSCRIPTION_KEY);

            // Request body
            byte[] binaryStr = ImageUtil.imageToBinary(path);
            HttpEntity reqEntity = new ByteArrayEntity(binaryStr);
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            resultMap.put("code", String.valueOf(code));
            if(code == 200) {
            if (entity != null) 
            {
            	String responseStr = EntityUtils.toString(entity);
				JSONObject jo = JSON.parseObject(responseStr);
            	JSONArray ja = jo.getJSONArray("regions");
            	Iterator<Object> it = ja.iterator();
            	StringBuilder result = null;
            	while(it.hasNext()) {//解析regions数组
            		JSONObject regionJo = (JSONObject) it.next();
            		JSONArray linesJa = regionJo.getJSONArray("lines");
            		Iterator<Object> linesIt = linesJa.iterator();
            		while(linesIt.hasNext()) {//解析lines数组
            			JSONObject wordsJo = (JSONObject) linesIt.next();
                		JSONArray wordsJa = wordsJo.getJSONArray("words");
                		Iterator<Object> wordsIt = wordsJa.iterator();
                		while(wordsIt.hasNext()) {//解析words数组
                			JSONObject textJo = (JSONObject) wordsIt.next();
                			String text = textJo.getString("text");
                			if(result == null) {//如果第一个字符串就不需要空格
                				result = new StringBuilder();
                				result.append(text);
                			}else {//不是第一个字符串需要空格
                				result.append(" ").append(text);
                			}
                		}
            		}
            	}
            	if(result != null) {
            		resultMap.put("result", result.toString());
            	}else {
            		resultMap.put("result", "");
            	}
                System.out.println(result.toString());
            }
            }else {
            	resultMap.put("result","");
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }*/
		return resultMap;
	}
	
	/*public static Map<String,String> translate(String text,String from,String to) throws Exception {
		//HttpClient httpclient = HttpClients.createDefault();
        Map<String,String> resultMap = new HashMap<String,String>();
         try
        {
       	URIBuilder builder = new URIBuilder("https://api.microsofttranslator.com/v2/Http.svc/Translate?text="+URLEncoder.encode(text,"UTF-8")+"&from="+from+"&to="+to);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", TRANSLATE_SUBSCRIPTION_KEY);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            int code = response.getStatusLine().getStatusCode();
            resultMap.put("code", String.valueOf(code));
            if(code == 200) {
            if (entity != null) 
            {
            	String responseStr = EntityUtils.toString(entity);
            	Document doc = Jsoup.parse(responseStr);
            	Element e_string = doc.getElementsByTag("string").get(0);
            	String result = e_string.text();
            	resultMap.put("result", result);
                System.out.println(responseStr);
            }
            }else {
            	resultMap.put("result","");
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }





		return resultMap;
	}*/

    /**
     *
     * @param text
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
	public static  String translate( final String text, final String from, final String to) {
        String url="";
        try {
             url = URLEncoder.encode(text,"UTF-8")+"&from="+from+"&to="+to;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e(TAG,url);
        OkGo.<String>get("https://api.microsofttranslator.com/v2/Http.svc/Translate?text="+url)
                .headers("Ocp-Apim-Subscription-Key",TRANSLATE_SUBSCRIPTION_KEY)
                .retryCount(0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG,response.body().toString());
                        try {
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = factory.newPullParser();
                            parser.setInput(new StringReader(response.body().toString()));
                            int eventType = parser.getEventType();
                            String name = "";
                            String version = "";
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                String nodeName = parser.getName();
                                switch (eventType) {
                                    // 开始解析某个结点
                                    case XmlPullParser.START_TAG: {
                                        if ("string".equals(nodeName)) {
                                            textResult = parser.nextText();
                                            Log.e(TAG,"结果是"+textResult);
                                        }
                                        break;
                                    }
                                }
                                eventType = parser.next();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG,"错误");
                    }
                });
	        return textResult;
    }



    public static String speak(String text,String language) {
		String path = "";
		String outputFormat = AudioOutputFormat.Audio16Khz64KbitMonoMp3;
        String genderName = Gender.Female;
        String voiceName = VoiceFontUtil.getVoiceName(language, genderName);
        System.out.println("voiceName ------------"+voiceName);
        try
        {

        	byte[] audioBuffer = TTSService.Synthesize(SPEECH_SUBSCRIPTION_KEY, text, outputFormat, language, genderName, voiceName);
        	
        	// write the pcm data to the file
           // Random random = new Random(20);
            Log.e(TAG,audioBuffer.length+" audioBuffer");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
            String filename = dateFormat.format(new Date(System.currentTimeMillis()));
        	path = filename+ ".mp3";
        	File outputAudio = new File(FilePath.getAudioPath()+File.separator+path);
        	Log.e(TAG,FilePath.getAudioPath()+File.separator+path);
        	FileOutputStream fstream = new FileOutputStream(outputAudio);
            fstream.write(audioBuffer);
            fstream.flush();
            fstream.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
		return FilePath.getAudioPath()+File.separator+path;
	}
	
//	private static boolean isUTF8(String key){
//        try {
//            key.getBytes("utf-8");
//            return true;
//        } catch (UnsupportedEncodingException e) {
//            return false;
//        }
//    }
	
	public static void main(String[] args) {
		try {
//		Map<String,String> map = translate("how are you","en-US","zh-CN");
//		speak(map.get("result"),"zh-CN");
//			ocr("D:\\work\\project\\upload\\image\\s6rxtrm6qi1t4w4iin35.jpg","unk");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
