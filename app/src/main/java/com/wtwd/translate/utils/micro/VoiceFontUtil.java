package com.wtwd.translate.utils.micro;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class VoiceFontUtil {
	private static Map<String,String> voiceMap = new HashMap<String,String>();
	static {
		voiceMap.put("ar-EG*,Female", "Microsoft Server Speech Text to Speech Voice (ar-EG, Hoda)");
		voiceMap.put("ar-SA,Male", "Microsoft Server Speech Text to Speech Voice (ar-SA, Naayf)");
		voiceMap.put("ca-ES,Female", "Microsoft Server Speech Text to Speech Voice (ca-ES, HerenaRUS)");
		voiceMap.put("cs-CZ,Male", "Microsoft Server Speech Text to Speech Voice (cs-CZ, Jakub)");
		voiceMap.put("da-DK,Female", "Microsoft Server Speech Text to Speech Voice (da-DK, HelleRUS)");
		voiceMap.put("de-AT,Male", "Microsoft Server Speech Text to Speech Voice (de-AT, Michael)");
		voiceMap.put("de-CH,Male", "Microsoft Server Speech Text to Speech Voice (de-CH, Karsten)");
		voiceMap.put("de-DE,Female", "Microsoft Server Speech Text to Speech Voice (de-DE, Hedda)");
		voiceMap.put("de-DE,Male", "Microsoft Server Speech Text to Speech Voice (de-DE, Stefan, Apollo)");
		voiceMap.put("el-GR,Male", "Microsoft Server Speech Text to Speech Voice (el-GR, Stefanos)");
		voiceMap.put("en-AU,Female", "Microsoft Server Speech Text to Speech Voice (en-AU, Catherine)");
		voiceMap.put("en-CA,Female", "Microsoft Server Speech Text to Speech Voice (en-CA, Linda)");
		voiceMap.put("en-GB,Female", "Microsoft Server Speech Text to Speech Voice (en-GB, Susan, Apollo)");
		voiceMap.put("en-GB,Male", "Microsoft Server Speech Text to Speech Voice (en-GB, George, Apollo)");
		voiceMap.put("en-IE,Male", "Microsoft Server Speech Text to Speech Voice (en-IE, Sean)");
		voiceMap.put("en-IN,Female", "Microsoft Server Speech Text to Speech Voice (en-IN, Heera, Apollo)");
		voiceMap.put("en-IN,Male", "Microsoft Server Speech Text to Speech Voice (en-IN, Ravi, Apollo)");
		voiceMap.put("en-US,Female", "Microsoft Server Speech Text to Speech Voice (en-US, ZiraRUS)");
		voiceMap.put("en-US,Male", "Microsoft Server Speech Text to Speech Voice (en-US, BenjaminRUS)");
		voiceMap.put("es-ES,Female", "Microsoft Server Speech Text to Speech Voice (es-ES, Laura, Apollo)");
		voiceMap.put("es-ES,Male", "Microsoft Server Speech Text to Speech Voice (es-ES, Pablo, Apollo)");
		voiceMap.put("es-MX,Female", "Microsoft Server Speech Text to Speech Voice (es-MX, HildaRUS)");
		voiceMap.put("es-MX,Male", "Microsoft Server Speech Text to Speech Voice (es-MX, Raul, Apollo)");
		voiceMap.put("fi-FI,Female", "Microsoft Server Speech Text to Speech Voice (fi-FI, HeidiRUS)");
		voiceMap.put("fr-CA,Female", "Microsoft Server Speech Text to Speech Voice (fr-CA, Caroline)");
		voiceMap.put("fr-CH,Male", "Microsoft Server Speech Text to Speech Voice (fr-CH, Guillaume)");
		voiceMap.put("fr-FR,Female", "Microsoft Server Speech Text to Speech Voice (fr-FR, Julie, Apollo)");
		voiceMap.put("fr-FR,Male", "Microsoft Server Speech Text to Speech Voice (fr-FR, Paul, Apollo)");
		voiceMap.put("he-IL,Male", "Microsoft Server Speech Text to Speech Voice (he-IL, Asaf)");
		voiceMap.put("hi-IN,Female", "Microsoft Server Speech Text to Speech Voice (hi-IN, Kalpana, Apollo)");
		voiceMap.put("hi-IN,Male", "Microsoft Server Speech Text to Speech Voice (hi-IN, Hemant)");
		voiceMap.put("hu-HU,Male", "Microsoft Server Speech Text to Speech Voice (hu-HU, Szabolcs)");
		voiceMap.put("id-ID,Male", "Microsoft Server Speech Text to Speech Voice (id-ID, Andika)");
		voiceMap.put("it-IT,Male", "Microsoft Server Speech Text to Speech Voice (it-IT, Cosimo, Apollo)");
		voiceMap.put("ja-JP,Female", "Microsoft Server Speech Text to Speech Voice (ja-JP, Ayumi, Apollo)");
		voiceMap.put("ja-JP,Male", "Microsoft Server Speech Text to Speech Voice (ja-JP, Ichiro, Apollo)");
		voiceMap.put("ko-KR,Female", "Microsoft Server Speech Text to Speech Voice (ko-KR, HeamiRUS)");
		voiceMap.put("nb-NO,Female", "Microsoft Server Speech Text to Speech Voice (nb-NO, HuldaRUS)");
		voiceMap.put("nl-NL,Female", "Microsoft Server Speech Text to Speech Voice (nl-NL, HannaRUS)");
		voiceMap.put("pl-PL,Female", "Microsoft Server Speech Text to Speech Voice (pl-PL, PaulinaRUS)");
		voiceMap.put("pt-BR,Female", "Microsoft Server Speech Text to Speech Voice (pt-BR, HeloisaRUS)");
		voiceMap.put("pt-BR,Male", "Microsoft Server Speech Text to Speech Voice (pt-BR, Daniel, Apollo)");
		voiceMap.put("pt-PT,Female", "Microsoft Server Speech Text to Speech Voice (pt-PT, HeliaRUS)");
		voiceMap.put("ro-RO,Male", "Microsoft Server Speech Text to Speech Voice (ro-RO, Andrei)");
		voiceMap.put("ru-RU,Female", "Microsoft Server Speech Text to Speech Voice (ru-RU, Irina, Apollo)");
		voiceMap.put("ru-RU,Male", "Microsoft Server Speech Text to Speech Voice (ru-RU, Pavel, Apollo)");
		voiceMap.put("sk-SK,Male", "Microsoft Server Speech Text to Speech Voice (sk-SK, Filip)");
		voiceMap.put("sv-SE,Female", "Microsoft Server Speech Text to Speech Voice (sv-SE, HedvigRUS)");
		voiceMap.put("th-TH,Male", "Microsoft Server Speech Text to Speech Voice (th-TH, Pattara)");
		voiceMap.put("tr-TR,Female", "Microsoft Server Speech Text to Speech Voice (tr-TR, SedaRUS)");
		voiceMap.put("zh-CN,Female", "Microsoft Server Speech Text to Speech Voice (zh-CN, HuihuiRUS)");
		voiceMap.put("zh-CN,Male", "Microsoft Server Speech Text to Speech Voice (zh-CN, Kangkang, Apollo)");
		voiceMap.put("zh-HK,Female", "Microsoft Server Speech Text to Speech Voice (zh-HK, Tracy, Apollo)");
		voiceMap.put("zh-HK,Male", "Microsoft Server Speech Text to Speech Voice (zh-HK, Danny, Apollo)");
		voiceMap.put("zh-TW,Female", "Microsoft Server Speech Text to Speech Voice (zh-TW, Yating, Apollo)");
		voiceMap.put("zh-TW,Male", "Microsoft Server Speech Text to Speech Voice (zh-TW, Zhiwei, Apollo)");
	}
			
	public static String getVoiceName(String locale,String gender) {
		String voiceName = voiceMap.get(locale+","+gender);
		Log.e("VoiceFontUtil","VoiceFontUtil "+locale+" locale "+" gender "+gender);
		if(TextUtils.isEmpty(voiceName)) {//如果女声不存在，取男声，都不存在，默认
			voiceName = voiceMap.get(locale+","+Gender.Male);
			Log.e("VoiceFontUtil","voiceName "+voiceName);
			if(TextUtils.isEmpty(voiceName)) {
				voiceName = "Microsoft Server Speech Text to Speech Voice (en-US, ZiraRUS)";
			}
		}
		return voiceName;
	}

}
