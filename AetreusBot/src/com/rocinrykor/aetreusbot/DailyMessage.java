package com.rocinrykor.aetreusbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class DailyMessage {
	static JDA jda;
	static TextChannel channel;
	static Message dailyMessage;
	static Message startingMessage;
	
	static String pattern = "yyyy-MM-dd";
	static SimpleDateFormat simpleDateFormat;
	String currentDate;
	
	public static void Update() {
		String currentDate = simpleDateFormat.format(new Date());
		
		String creationDate;
		try {
			creationDate = dailyMessage.getCreationTime().atZoneSameInstant(ZoneId.systemDefault()).toLocalDate().toString();
		} catch (Exception e) {
			creationDate = null;
		}
		
		System.out.println(currentDate + " | " + creationDate);
		
		if (creationDate != null) {
			if (creationDate.equalsIgnoreCase(currentDate)) {
				return;
			} else {
				dailyMessage.delete().complete();
				try {
					NewDailyMessage();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				NewDailyMessage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public static void Init() {
		jda = BotController.getJDA();
		
		simpleDateFormat = new SimpleDateFormat(pattern);
		
		//System.out.println(jda.getTextChannels().toString());
	
		channel = jda.getTextChannelsByName("general", true).get(0);
		boolean foundMessage = false;
		
		try {
			Long lastMessageID = channel.getLatestMessageIdLong();
			startingMessage = channel.getMessageById(lastMessageID).complete();
		} catch (Exception e) {
			startingMessage = channel.sendMessage("Starting!").complete();
			startingMessage.delete().queue();
		}
		
		String compare = "daily";
		
		if (startingMessage.getContentRaw().toString().toLowerCase().contains(compare)) {
			dailyMessage = startingMessage;
			foundMessage = true;
		}
		
		List<Message> messageList = channel.getHistoryBefore(startingMessage, 100).complete().getRetrievedHistory();
		
		for (int i = 0; i < messageList.size(); i++) {
			if (messageList.get(i).getAuthor().isBot()) {
				if (messageList.get(i).getContentRaw().toString().toLowerCase().contains(compare)) {
					dailyMessage = messageList.get(i);
					System.out.println("Found A Message");
					foundMessage = true;
				}
			}
		}
		
		if (!foundMessage) {
			try {
				NewDailyMessage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static void NewDailyMessage() throws IOException {
		
		System.out.println("Trying to make a new daily message!");

		URL url = new URL("https://inspirobot.me/api?generate=true");
		
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
	
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      
        String inputLine;
        String pictureURL = null;

        while ((inputLine = in.readLine()) != null) {
        	pictureURL = inputLine;
        }
        
        in.close();
        
        
        System.out.println(pictureURL);
        
        if (pictureURL != null) {
        	String message = "Daily Message: \n"
            		+ pictureURL;
        	
            dailyMessage = channel.sendMessage(message).complete();
        } else {
        	dailyMessage = null;
        }
        
        
	}

}
