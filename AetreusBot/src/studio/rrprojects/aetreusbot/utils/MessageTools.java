package studio.rrprojects.aetreusbot.utils;

public class MessageTools {
	
	public static String CapatalizeFirst(String input) {
		String output = input.substring(0,1).toUpperCase() + input.substring(1);
		
		return output;
	}
	
	public static String BlockText(String input) {
		return BlockText(input, null);
	}

	public static String BlockText(String input, String type) {
		String blockType = "";
		if (type != null) {
			blockType = type;
		}
		
		String output = "```" + blockType + "\n" + input 
				+ "\n```";
		
		return output;
	}
}
