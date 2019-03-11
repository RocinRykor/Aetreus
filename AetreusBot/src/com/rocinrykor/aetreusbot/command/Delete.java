package com.rocinrykor.aetreusbot.command;

import java.util.ArrayList;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Delete extends Command {
	
	private static ArrayList<Message> messageList;
	private static MessageHistory messageHistory;
	private static int remainingAmount;
	
	@Override
	public String getName() {
		return "Delete";
	}

	@Override
	public String getDescription() {
		return "Deletes a set number of messages on the server";
	}

	@Override
	public String getAlias() {
		return "Del";
	}
	
	@Override
	public String getHomeChannel() {
		return "bottesting";
	}

	@Override
	public String helpMessage() {
		return "Is the text channels getting a little too bloated? Do you want to start fresh or just tidy them up before the next tabletop session? \n"
				+ "Well by using the delete command folled by the number of messages you wish to delete, I will start tidying up the channel message by message. \n"
				+ "Do not worry, I will ignore any important message, that is to say any message with an attachment or that is pinned to the channel. \n";
	}

	@Override
	public boolean isAdminOnly() {
		return true;
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}
	
	@Override
	public boolean isAdultResricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	String messageID = "";

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd, MessageChannel channel) {
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), channel);
			return;
		}

		messageID = event.getMessage().getId();
		
		int deleteAmount = GetDeleteAmount(primaryArg);
		
		messageList = new ArrayList<>();
			
    	remainingAmount = deleteAmount;
    	
    	messageHistory = event.getChannel().getHistoryBefore(messageID, 10).complete();
		
    	new Thread() {
    		public void run() {
    			while (remainingAmount > 0) {
            		if (remainingAmount >= 100) {
            			messageHistory = event.getChannel().getHistoryBefore(messageID, 100).complete();
            			if (messageHistory.size() < 100) {
            				remainingAmount = 0;
            			} else {
            				remainingAmount -= messageHistory.size();
            			}
            		} else {
            			messageHistory = event.getChannel().getHistoryBefore(messageID, remainingAmount).complete();
            			if (messageHistory.size() < remainingAmount) {
            				remainingAmount = 0;
            			} else {
            				remainingAmount -= messageHistory.size();
            			}
            		}
            		
        			messageList.addAll(messageHistory.getRetrievedHistory());
        			messageID = messageList.get(messageList.size() - 1).getId();
            	}
        	    
        	    System.out.println("Number of messages collected: " + messageList.size());
        	    
        	    for (Message message : Delete.messageList) {
        	    	if (message.isPinned() || !message.getAttachments().isEmpty()) {
        	    		System.out.println("Message Protected - Skipped");
        	    	} else {
        	    		try {
        	    			message.delete().queue();
						} catch (Exception e) {
							System.out.println("ERROR: CANNOT DELETE");
						}
        	    		
        	    	}
        	    }
        	    
        	    System.out.println("Deletion Complete!");
    		}
    	}.start();
    }
	
	private int GetDeleteAmount(String Arg) {
		 int defaultAmount = 10;
		 
		 if (Arg != null) {
			 String input = Arg;
			 
			 try {
					Integer.parseInt(input);
					return Integer.parseInt(input);
				} catch (Exception e) {
					return defaultAmount;
				}
		 } else {
			 return defaultAmount;
		 }
		 	
	}

	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}

}
