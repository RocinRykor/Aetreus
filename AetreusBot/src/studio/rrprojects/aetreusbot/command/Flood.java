package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Flood extends Command{

	@Override
	public String getName() {
		return "Flood";
	}

	@Override
	public String getAlias() {
		return "Flood";
	}

	@Override
	public String getHelpDescription() {
		return "Spams a channel with a specified number of messages for testing purposes - Admin Only";
	}

	@Override
	public String getHomeChannel() {
		return "bottesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
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
		return false;
	}
	
	public void executeMain(CommandContainer cmd) {
		String primaryArg = cmd.MAIN_ARG;
		
		int messageAmount = GetAmount(primaryArg);
		
		new Thread() {
			public void run() {
				for (int i = 0; i < messageAmount; i++) {
					SendMessage("Message #: " + (i + 1), cmd.DESTINATION, cmd.AUTHOR);
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
	
	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}

}
