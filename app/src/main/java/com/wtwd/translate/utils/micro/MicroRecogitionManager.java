package com.wtwd.translate.utils.micro;

import android.app.Activity;
import android.content.Context;
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
    public void initSpeechRecognition(String mLanguageType){
        this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient((Activity) mContext, SpeechRecognitionMode.ShortPhrase , mLanguageType, this, "c96c2a771c6e4548a24e269884889478");
        this.micClient.setAuthenticationUri("");
        this.micClient.startMicAndRecognition();


    }


    @Override
    public void onPartialResponseReceived(String s) {

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
        Log.e(TAG,"微软语音合成错误 ： "+s);
    }

    @Override
    public void onAudioEvent(boolean b) {
        Log.e(TAG,"onAudioEvent ： "+b);
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
    }
}
