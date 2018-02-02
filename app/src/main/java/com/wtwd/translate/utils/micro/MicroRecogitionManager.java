package com.wtwd.translate.utils.micro;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.textservice.SpellCheckerSession;

import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;
import com.wtwd.translate.activity.ChatActivity;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * time:2018/1/19
 * Created by w77996
 */

public class MicroRecogitionManager implements ISpeechRecognitionServerEvents {

    private static final String TAG = "MicroRecogitionManager";

    private final Context mContext;
    private MicroRecogitionManagerCallBack mMicroRecogitionManagerCallBack;
    /**
     * 微软语音状态
     */
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    public SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.ShortPhrase;
    }

    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    /**
     * 微软语音接口
     */
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;

    private static MicroRecogitionManager mInstance;

    private MicroRecogitionManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 微软语音单例
     * @param mContext
     * @return
     */
    public static MicroRecogitionManager getMicroRecogitionManager(Context mContext) {
        if (mInstance == null) {
            synchronized (MicroRecogitionManager.class) {
                if (mInstance == null) {
                    mInstance = new MicroRecogitionManager(mContext);
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置接口
     */
    public void setmicroRecogitionManagerCallBack(MicroRecogitionManagerCallBack microRecogitionManagerCallBack) {
        this.mMicroRecogitionManagerCallBack =microRecogitionManagerCallBack ;
    }

    public MicroRecogitionManagerCallBack getAudioStateChange() {
        return mMicroRecogitionManagerCallBack;
    }

    /**
     * 初始化微软语音合成
     * @param mLanguageType
     */
    public  void initSpeechRecognition(String mLanguageType){
        Log.e(TAG,"mLanguageType" + mLanguageType);
       /* if(null !=this.micClient){
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }*/
       mMicroRecogitionManagerCallBack.startInitSpeechRecognition();
        this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient((Activity) mContext, SpeechRecognitionMode.ShortPhrase , mLanguageType, this, "6d5a91fa9c614a33a681731279f2450c");
        if(this.micClient == null){
            Log.e(TAG,""+"this.micClient"+"  null");
        }else {
            Log.e(TAG,""+"this.micClient"+" not null");
        }
        Log.e(TAG,"对象"+this.micClient);
        this.micClient.setAuthenticationUri("");
        this.micClient.startMicAndRecognition();
    }
    /**
     * 初始化微软语音合成
     * @param mLanguageType
     */
    public void initFileRecognition(String mLanguageType,String mFilepath){
        this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                (Activity)mContext,
                SpeechRecognitionMode.ShortPhrase,
                mLanguageType,
                this,
                "6d5a91fa9c614a33a681731279f2450c");
        this.dataClient.setAuthenticationUri("");
        SendAudioHelper(mFilepath);
        //this.micClient.startMicAndRecognition();
    }

    private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, this.getMode(), filename);
        try
        {
            doDataReco.execute().get(20, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                InputStream fileStream = new FileInputStream(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);
                    Log.e(TAG,bytesRead+"read buffer ");
                    if (bytesRead > -1) {
                        // Send of audio data to service.
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            finally {
                dataClient.endAudio();
            }

            return null;
        }
    }

    @Override
    public void onPartialResponseReceived(String s) {
        Log.e(TAG,"onPartialResponseReceived :"+s.toString());
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (recognitionResult.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        recognitionResult.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient  && (this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
            Log.e(TAG,"对象"+this.micClient);
            Log.e(TAG,"micClient");
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //this.micClient.PlayWaveFile();
            //this.micClient = null;
        }
        Log.d(TAG,"********* Final n-BEST Results *********");
        for (int i = 0; i < recognitionResult.Results.length; i++) {
            Log.d(TAG,"[" + i + "]" + " Confidence=" + recognitionResult.Results[i].Confidence +
                    " Text=\"" + recognitionResult.Results[i].DisplayText + "\"");
        }
        // mAudioMediaPlayManager.stopRecorderUsePhone();
        if(recognitionResult.Results.length <= 0){
            Log.e(TAG,"recognitionResult 长度为0识别失败");
            mMicroRecogitionManagerCallBack.ononFinalResponseResultEmtity("null");
            return;
        }
        mMicroRecogitionManagerCallBack.onFinalResponseResult(recognitionResult.Results[0].DisplayText);
    }


    @Override
    public void onIntentReceived(String s) {
        Log.e(TAG,"onIntentReceived ： "+s);
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG,"微软语音合成错误 ： "+s.toString()+" "+ i);
        mMicroRecogitionManagerCallBack.onError(s);
        if(null!=this.micClient){
            Log.e(TAG,"微软语音合成错误,set client is null");
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }else{
            Log.e(TAG,"微软语音合成错误,client is not  null");
        }
    }

    @Override
    public void onAudioEvent(boolean b) {
        Log.e(TAG,"onAudioEvent ： "+b);
        mMicroRecogitionManagerCallBack.getOnAudioEvent(b);
    }

    public void releseClient(){
        if(null != this.micClient){
            this.micClient.endMicAndRecognition();
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }

    }

    public interface MicroRecogitionManagerCallBack{
        /**
         * 返回结果
         * @param result
         */
        void onFinalResponseResult(String result);

        /**
         * 返回结果为空
         * @param error
         */
        void ononFinalResponseResultEmtity(String error);

        /**
         * 错误
         * @param s
         */
        void onError(String s);

        void startInitSpeechRecognition();

        void getOnAudioEvent(boolean b);
    }
}
