package com.rocinrykor.aetreusbot.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RollTracker {
	static int[][] rollsArray = new int[30][21];
	
	
	
	

	public static void TrackRoll(String userID, int dieValue) {
		int userInt = Integer.parseInt(userID.substring(0, 8));
		int arrayPos = FindUser(userInt);
			
		if (arrayPos >= 0) {
			rollsArray[arrayPos][dieValue] += 1;
		}
	}
	
	private static int FindUser(int userID) {
		
		for (int i = 0; i < rollsArray.length; i++) {
			if (rollsArray[i][0] == userID) {
				return i;
			} else if (rollsArray[i][0] == 0) {
				rollsArray[i][0] = userID;
				return i;
			}
			
		}
		
		return -1;
	}
	
	public static String ReportStats(String userID, MessageReceivedEvent event) {
		String resultsMessage = "";		
		
		int userInt = Integer.parseInt(userID.substring(0, 8));
		int arrayPos = FindUser(userInt);
		
		resultsMessage += "Here are all of the d20 rolls for " + event.getAuthor() + " for my uptime: \n";
		for (int i = 1; i < rollsArray[0].length; i++) {
			resultsMessage += i + "'s: " + rollsArray[arrayPos][i] + "\n";
		}
		
		return resultsMessage;
	}
}
