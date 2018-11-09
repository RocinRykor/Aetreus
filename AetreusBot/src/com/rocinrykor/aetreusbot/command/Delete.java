package com.rocinrykor.aetreusbot.command;

import java.util.List;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Delete extends Command {

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
	public String helpMessage() {
		return "Is the text channels getting a little too bloated? Do you want to start fresh or just tidy them up before the next tabletop session? \n"
				+ "Well by using the delete command folled by the number of messages you wish to delete, I will start tidying up the channel message by message. \n"
				+ "Do not worry, I will ignore any important message, that is to say any message with an attachment or that is pinned to the channel. \n\n"
				+ "Please note that this is a bit of a slow process, about 1 message every 5 seconds or so, otherwise the server gets a bit bogged down.";
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
	public boolean deleteCallMessage() {
		return false;
	}
	
	String messageID = "";

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd) {
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), event);
			return;
		}

		messageID = event.getMessage().getId();
		
		int deleteAmount = GetDeleteAmount(primaryArg);
		
		try {
			event.getMessage().delete().queue();
		} catch (Exception e) {
			System.out.println("ERROR: CANNOT DELETE");
		}
		
		new Thread(new Runnable() {
		    public void run() {
		    	int refreshTime = 5;
		    	
		    	int remainingAmount = deleteAmount;
		    	
		    	while (remainingAmount > 0) {
		    		
		    		MessageHistory messageHistory = event.getChannel().getHistoryBefore(messageID, 10).complete();
		    		
		    		if (remainingAmount >= 100) {
		    			messageHistory = event.getChannel().getHistoryBefore(messageID, 100).complete();
		    			System.out.println(messageHistory.size());
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
		    		
					List<Message> messageList = messageHistory.getRetrievedHistory();
		        	
		        	System.out.println("Starting Deletion of "+ messageList.size() +" Messages, Exptected completion in "+ messageList.size() * refreshTime +" Seconds.");
		        	System.out.println("Remaining Amount: " + remainingAmount);
		        	
			        for (int i = 0; i < messageList.size(); i++) {
						boolean isPinned = messageList.get(i).isPinned();
						boolean isEmpty = messageList.get(i).getAttachments().isEmpty();
												
			        	if (isPinned || !isEmpty) {
			        		System.out.println("Message protected - skipping");
			        		messageID = messageList.get(i).getId();
			        	} else {
			        		try {
			        			messageList.get(i).delete().queue();
			        			Thread.sleep(refreshTime * 1000);
							} catch (Exception e) {
								System.out.println("ERROR: CANNOT DELETE");
							}
			        	}
					}
			        
			        System.out.println("Deletion Complete!");
		    	}
		    }
		}).start();
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

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}

}
