package com.rocinrykor.aetreusbot.command;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Flood extends Command {

	@Override
	public String getName() {
		return "Flood";
	}

	@Override
	public String getDescription() {
		return "Floods the chat channel with a umber of messages - Admin Only";
	}

	@Override
	public String getAlias() {
		return "F";
	}

	@Override
	public String helpMessage() {
		return "Because certian commands require many messages to be properly tested, like the delete function, when using this command I will flood a channel with a lot of useless messages";
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
	public boolean deleteCallMessage() {
		return false;
	}

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd) {
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), event);
			return;
		}
		
		int messageAmount = GetAmount(primaryArg);
		
		new Thread() {
			public void run() {
				for (int i = 0; i < messageAmount; i++) {
					sendMessage("Message #: " + i, event);
					try {
						Thread.sleep(3 * 1000);
					} catch (Exception e) {
						System.out.println("ERROR");
					}
				}
			}
		}.start();
		
		
	}

	private int GetAmount(String primaryArg) {
		
		if (primaryArg != null) {
			 try {
					Integer.parseInt(primaryArg);
					return Integer.parseInt(primaryArg);
				} catch (Exception e) {
					return 10;
				}
		 } else {
			 return 10;
		 }
		
	}

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}

}