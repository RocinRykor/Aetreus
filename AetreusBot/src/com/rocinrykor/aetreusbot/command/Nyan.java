package com.rocinrykor.aetreusbot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Nyan extends Command{

	@Override
	public String getName() {
		return "Nyan";
	}

	@Override
	public String getDescription() {
		return "Cursed Command - Admin Only";
	}

	@Override
	public String getAlias() {
		return "Nyan";
	}
	
	@Override
	public String getHomeChannel() {
		return "nsfw";
	}

	@Override
	public String helpMessage() {
		return "Generates a catgirl image. use the arguement \"lewd\" for a NSFW version";
	}

	@Override
	public boolean isAdminOnly() {
		return true;
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}
	
	@Override
	public boolean isAdultResricted() {
		return true;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd, MessageChannel channel) {
		boolean lewd = false;
		
		if (primaryArg.equalsIgnoreCase("lewd")) {
			lewd = true;
		}
		
		NewCatgirlPicture(lewd, event, channel);
	}

	private void NewCatgirlPicture(boolean lewd, MessageReceivedEvent event, MessageChannel channel) {
		URL url = null;
		String savedLine = null;
		
		try {
			if (lewd) {
				url = new URL("https://nekos.life/lewd");
			} else {
				url = new URL("https://nekos.life/");
			}
		} catch (Exception e) {
			
		}
		
		URLConnection connection = null;
		
		try {
			connection = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
	
        BufferedReader in = null;
        
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
      
        
		String inputLine;
        try {
			while ((inputLine = in.readLine()) != null)
			    if (inputLine.contains("cdn.nekos.life")) {
			    	savedLine = inputLine;
			    	break;
			    }
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        System.out.println(savedLine);
        
        String urlCatGirl = null;
        if (lewd) {
        	urlCatGirl = savedLine.substring(savedLine.indexOf("http"), savedLine.indexOf("alt")-2);
		} else {
			urlCatGirl = savedLine.substring(savedLine.indexOf("http"), savedLine.length()-3);
		}
        
        
        String message = "Generated Catgirl Image: " + urlCatGirl;
        
        System.out.println(message);
        
        sendMessage(message, channel);
	}

	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}
}
