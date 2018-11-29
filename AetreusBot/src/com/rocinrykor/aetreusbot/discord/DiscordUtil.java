package com.rocinrykor.aetreusbot.discord;

public class DiscordUtil {

	public static String MessageToCode(String message) {
		
		String codeBlock = "```css\n"
				+ message + "\n"
				+ "```";
		
		return codeBlock;
	}

	public static int SlidingColorScale(int countHit, int dicePool) {
		int colorMax = 255;
		double colorCurrent = ((double)countHit / dicePool) * 255;
		
		int color = (int) (colorMax - colorCurrent);
		
		return color;
	}

}
