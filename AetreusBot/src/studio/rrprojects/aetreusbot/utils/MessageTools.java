package studio.rrprojects.aetreusbot.utils;

public class MessageTools {
	
	public static String CapatalizeFirst(String input) {
		String output = input.substring(0,1).toUpperCase() + input.substring(1);
		
		return output;
	}
}
