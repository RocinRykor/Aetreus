package studio.rrprojects.aetreusbot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Nyan extends Command {

	@Override
	public String getName() {
		return "Nyan";
	}

	@Override
	public String getAlias() {
		return "Cat";
	}

	@Override
	public String getHelpDescription() {
		return "Generates catgirl images - use 'lewd' arguement for NSFW version - Adult Only";
	}

	@Override
	public String getHomeChannel() {
		return "NSFW";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean isAdultRestricted() {
		return true;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	public void executeMain(CommandContainer cmd) {
	boolean lewd = false;
		
		if (cmd.MAIN_ARG.equalsIgnoreCase("lewd")) {
			lewd = true;
		}
		
		NewCatgirlPicture(lewd, cmd);
	}

	private void NewCatgirlPicture(boolean lewd, CommandContainer cmd) {
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
        
        SendMessage(message, cmd.DESTINATION, cmd.AUTHOR);  
	}
	
	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}

}
