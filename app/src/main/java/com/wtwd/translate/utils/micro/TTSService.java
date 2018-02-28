package com.wtwd.translate.utils.micro;
import android.nfc.Tag;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.util.Log.e;

public class TTSService {

    private static String ttsServiceUri = "https://speech.platform.bing.com/synthesize";

    /**
     * Synthesize the voice through the specified parameters.
     */
    public static byte[] Synthesize(String key,String textToSynthesize, String outputFormat, String locale, String genderName, String voiceName) throws Exception {



        Authentication auth = new Authentication(key);
        String accessToken = auth.GetAccessToken();
        URL url = new URL(ttsServiceUri);
        HttpsURLConnection webRequest = (HttpsURLConnection)url.openConnection();
        webRequest.setDoInput(true);
        webRequest.setDoOutput(true);
        webRequest.setConnectTimeout(5000);
        webRequest.setReadTimeout(15000);
        webRequest.setRequestMethod("POST");

        webRequest.setRequestProperty("Content-Type", "application/ssml+xml");
        webRequest.setRequestProperty("X-Microsoft-OutputFormat", outputFormat);
        webRequest.setRequestProperty("Authorization", "Bearer " + accessToken);
//        webRequest.setRequestProperty("X-Search-AppId", "07D3234E49CE426DAA29772419F436CA");
//        webRequest.setRequestProperty("X-Search-ClientID", "1ECFAE91408841A480F00935DC390960");
//        webRequest.setRequestProperty("User-Agent", "TTSAndroid");
        webRequest.setRequestProperty("Accept", "*/*");

        String body = XmlDom.createDom(locale, genderName, voiceName, textToSynthesize);
        Log.e("TTSService","body "+body);
        byte[] bytes = body.getBytes();
        webRequest.setRequestProperty("content-length", String.valueOf(bytes.length));
        webRequest.connect();

        DataOutputStream dop = new DataOutputStream(webRequest.getOutputStream());

        dop.write(bytes);
        Log.e("TTSService","dop "+dop.size());








        dop.flush();
        dop.close();
        Log.e("TTSService","webRequest "+webRequest.getContentLength());
        InputStream inSt = webRequest.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inSt, "utf-8"));
//        StringBuilder builder = new StringBuilder();
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            builder.append(line).append("\n");
//        }
//        String content = builder.toString();
        ByteArray ba = new ByteArray();
        Log.e("TTSService","inSt "+inSt);

        int rn2 = 0;
        int bufferLength = 4096;
        byte[] buf2 = new byte[bufferLength];
        while ((rn2 = inSt.read(buf2, 0, bufferLength)) > 0) {
            ba.cat(buf2, 0, rn2);
        }
        Log.e("TTSService","ba "+ba.getLength());
        inSt.close();
        webRequest.disconnect();

        return ba.getArray();
    }
}
