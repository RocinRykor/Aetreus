package studio.rrprojects.aetreusbot.command;

import java.util.ArrayList;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Delete extends Command {

	private static ArrayList<Message> messageList;
	private static MessageHistory messageHistory;
	private static int remainingAmount;
	private static Message startingMessage;

	@Override
	public String getName() {
		return "Delete";
	}

	@Override
	public String getAlias() {
		return "Del";
	}

	@Override
	public String getHelpDescription() {
		return "Deletes the specified number of previous messages - Admin Only";
	}

	@Override
	public String getHomeChannel() {
		return "bottesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}

	@Override
	public boolean isAdminOnly() {
		return true;
	}
	
	@Override
	public boolean isAdultRestricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	public void executeMain(CommandContainer cmd) {
		String primaryArg = cmd.MAIN_ARG;
		
		int deleteAmount = GetDeleteAmount(primaryArg);
		
		startingMessage = cmd.DESTINATION.getJDA().getTextChannelById(cmd.DESTINATION.getId()).sendMessage("Starting Deletion Process").complete();
		
		startingMessage.delete().complete();
		
		messageList = new ArrayList<>();
			
    	remainingAmount = deleteAmount;
    	
    	messageHistory = startingMessage.getChannel().getHistoryBefore(startingMessage, 10).complete();
		
    	new Thread() {
    		public void run() {
    			while (remainingAmount > 0) {
            		if (remainingAmount >= 100) {
            			messageHistory = startingMessage.getChannel().getHistoryBefore(startingMessage, 100).complete();
            			if (messageHistory.size() < 100) {
            				remainingAmount = 0;
            			} else {
            				remainingAmount -= messageHistory.size();
            			}
            		} else {
            			messageHistory = startingMessage.getChannel().getHistoryBefore(startingMessage, remainingAmount).complete();
            			if (messageHistory.size() < remainingAmount) {
            				remainingAmount = 0;
            			} else {
            				remainingAmount -= messageHistory.size();
            			}
            		}
            		
        			messageList.addAll(messageHistory.getRetrievedHistory());
        			startingMessage = messageList.get(messageList.size() - 1);
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
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}

}
