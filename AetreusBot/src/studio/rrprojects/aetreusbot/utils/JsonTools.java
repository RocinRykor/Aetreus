package studio.rrprojects.aetreusbot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class JsonTools {

	public static LinkedHashMap<String, Integer> ConvertJsonDictToSIHashMap(JsonValue jsonValue) {
		if (jsonValue == null) {
			return null;
		}
		
		LinkedHashMap<String, Integer> tmp = new LinkedHashMap<>();
		
		for (Member member : jsonValue.asObject()) {
			tmp.put(member.getName(), member.getValue().asInt());
		}
		
		return tmp;
	}
	
	public static LinkedHashMap<String, String> ConvertJsonDictToSSHashMap(JsonValue jsonValue) {
		if (jsonValue == null) {
			return null;
		}
		
		LinkedHashMap<String, String> tmp = new LinkedHashMap<>();
		
		for (Member member : jsonValue.asObject()) {
			
			try {
				tmp.put(member.getName(), member.getValue().asString());
			} catch (Exception e) {
				tmp.put(member.getName(), member.getValue().toString());
			}
			
		}
		
		return tmp;
	}

	public static ArrayList<String> ConvertArray(JsonArray array) {
		if (array == null) {
			return null;
		}
		
		ArrayList<String> tmpArray = new ArrayList<>();
		
		for (JsonValue value : array) {
			tmpArray.add(value.asString());
		}
		
		return tmpArray;
	}

	public static JsonArray ReverseArray(ArrayList<String> personalityTraits) {
		JsonArray mainArray = new JsonArray();
		
		for (String string : personalityTraits) {
			mainArray.add(string);
		}
		
		return mainArray;
	}

}
