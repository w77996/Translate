package com.wtwd.translate.utils;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BluetoothSerialString {
    private static final String TAG = "BluetoothSerialString";
    public static final String item_src = "src";
    public static final String item_des = "des";
    private static final String item_recognition = "rec";
    private static final String item_translation = "tra";
    private static final int PACKAGE_LENGTH = 12;

    /**
     * get the jason string.
     * @param from          the src language
     * @param to            the des language
     * @param recognition   recognition result
     * @param translation   translation result
     * @param flag          true:all the argument   false: only the translation argument.
     * @return              the needed jason string.
     *
     * such as:
     *      getTranslationResultString("zh-CN", "en-US", "今天天气不错。", "Today's weather is fine.", true);
     * the result:
     *      {"src":"zh-CN","des":"en-US","rec":"今天天气不错。","tra":"Today's weather is fine."}
     *
     *      getTranslationResultString("zh-CN", "en-US", "今天天气不错。", "Today's weather is fine.", false);
     *  the result:
     *      {"des":"en-US","tra":"Today's weather is fine."}
     */
    public static String getTranslationResultString(String from, String to, String recognition, String translation, boolean flag) {
        try {
            JSONObject tobj = new JSONObject();
            if (flag) {
                tobj.put(item_src, from);
                tobj.put(item_des, to);
                tobj.put(item_recognition, recognition);
                tobj.put(item_translation, translation);
                return tobj.toString();
            }
            else {
                tobj.put(item_des, to);
                tobj.put(item_translation, translation);
                return tobj.toString();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] getTranslationResultByte(String from, String to, String recognition, String translation, boolean flag) {
        byte[] result = null;
        try {

            String src = "{\"src\":\"";
            byte[] srcbyte = src.getBytes();
            byte[] srcdata = from.getBytes();

            String des = "\",\"des\":\"";
            byte[] desbyte = des.getBytes();
            byte[] desdata = to.getBytes();

            String rec = "\",\"rec\":\"";
            byte[] recbyte = rec.getBytes();
            byte[] recdata = recognition.getBytes("UnicodeBigUnmarked");

            String tra = "\",\"tra\":\"";
            byte[] trabyte = tra.getBytes();
            byte[] tradata = translation.getBytes("UnicodeBigUnmarked");

            String end = "\"}";
            byte[] endbyte = end.getBytes();

            int leng = srcbyte.length + srcdata.length + desbyte.length + desdata.length + recbyte.length + recdata.length + trabyte.length + tradata.length+endbyte.length;
            System.out.println("leng "+leng+" data "+src+from+des+to+rec+recognition+tra+translation+end);
            result = new byte[leng];
            int index = 0;
            for (int i = 0; i < srcbyte.length; i++) {
                result[index] = srcbyte[i];
                index++;
            }
            for (int i = 0; i < srcdata.length; i++) {
                result[index] = srcdata[i];
                index++;
            }
            for (int i = 0; i < desbyte.length; i++) {
                result[index] = desbyte[i];
                index++;
            }
            for (int i = 0; i < desdata.length; i++) {
                result[index] = desdata[i];
                index++;
            }
            for (int i = 0; i < recbyte.length; i++) {
                result[index] = recbyte[i];
                index++;
            }
            for (int i = 0; i < recdata.length; i++) {
                result[index] = recdata[i];
                index++;
            }
            for (int i = 0; i < trabyte.length; i++) {
                result[index] = trabyte[i];
                index++;
            }
            for (int i = 0; i < tradata.length; i++) {
                result[index] = tradata[i];
                index++;
            }
            for (int i = 0; i < endbyte.length; i++) {
                result[index] = endbyte[i];
                index++;
            }
           Log.e(TAG,"tradata " +Arrays.toString(endbyte));
            // index --;
            System.out.println("result "+Arrays.toString(result)+" index"+index+" length");
            //Log.e(TAG,Arrays.toString(result));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * add the headdata to datas, the headdata is needed for the serial transport.
     * @param datas the byte[] needed the headdata.
     * @return      the combine data that can be send to serial device.
     */
    public static byte[] addHeadData(byte[] datas) {
        if (datas == null)
            return null;
        byte[] bytes = new byte[datas.length + 6];
        bytes[0] = (byte) 0x01;
        bytes[1] = (byte) 0x00;
        bytes[2] = (byte) 0xFC;
        bytes[3] = (byte) (datas.length + 2);
        bytes[4] = (byte) 0x01;
        bytes[5] = (byte) 0x52;
        for (int i = 0; i < datas.length; i++) {
            bytes[i + 6] = datas[i];
        }
        return bytes;
    }

    /**
     * divide the msg to byte arrays, add the headdata to these arrays, and then put them in the list.
     * @param msg   the src msg.
     * @return      the list contains the elements that can be send to serial device.
     */
    public static List<byte[]> dividedAndCombineString(String msg) {
        if (msg == null)
            return null;
        List<byte[]> list = new ArrayList<>();
        byte[] bytes = msg.getBytes();
        Log.e(TAG, "bytes:" + Arrays.toString(bytes));

        if (bytes.length <= PACKAGE_LENGTH) {
            byte[] temp = new byte[bytes.length + 2];
            temp[0] = 1;
            temp[1] = 0;
            for (int i = 0; i < bytes.length; i++) {
                temp[i + 2] = bytes[i];
            }
            list.add(addHeadData(temp));
            return list;
        }
        else {
            int count = bytes.length / PACKAGE_LENGTH;
            int sing = bytes.length % PACKAGE_LENGTH;
            int count_fix = count;
            if (sing != 0)
                count_fix = count + 1;
            for (int i = 0; i < count; i++) {
                byte[] temp = new byte[PACKAGE_LENGTH + 2];
                temp[0] = (byte) count_fix;
                temp[1] = (byte) i;
                for (int j = 0; j < PACKAGE_LENGTH; j++) {
                    temp[j + 2] = bytes[i * PACKAGE_LENGTH + j];
                    Log.e(TAG, "temp" + i + ":" + Integer.toHexString((byte)temp[j + 2]));
                }
                list.add(addHeadData(temp));
            }
            if (sing != 0) {
                byte[] temp = new byte[sing + 2];
                temp[0] = (byte) count_fix;
                temp[1] = (byte) (count_fix - 1);
                for (int j = 0; j < sing; j++) {
                    temp[j + 2] = bytes[count * PACKAGE_LENGTH + j];
                    Log.e(TAG, "temp" + (count_fix - 1) + ":" + Integer.toHexString((byte)temp[j + 2]));
                }
                list.add(addHeadData(temp));
            }
            return list;
        }
    }
}
