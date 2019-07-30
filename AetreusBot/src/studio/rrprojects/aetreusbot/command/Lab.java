package studio.rrprojects.aetreusbot.command;

import java.util.ArrayList;
import java.util.Collections;

import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Lab extends Command {

	@Override
	public String getName() {
		return "Lab";
	}

	@Override
	public String getAlias() {
		return "L";
	}

	@Override
	public String getHelpDescription() {
		return "Tool for Path of Exile: Get a list of chests to open at the end of a lab run";
	}

	@Override
	public String getHomeChannel() {
		return "Rolling";
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
	public boolean deleteCallMessage() {
		return false;
	}
	public void executeMain(CommandContainer cmd) {
		int keyPool = 0;
		
		try {
			keyPool = Integer.parseInt(cmd.MAIN_ARG);
		} catch (Exception e) {
			SendMessage("ERROR: You need to specify a number of chests to open, please enter a number between 1-10", cmd.DESTINATION);
			return;
		}
		
		if (keyPool < 1 || keyPool > 10) {
			SendMessage("ERROR: OUT-OF-BOUNDS, please enter a number between 1-10", cmd.DESTINATION);
			return;
		}
		
		ArrayList<Integer> listResults = new ArrayList<>();			
		
		while (keyPool > 0) {
			int tempResult = (int) ((Math.random() * 10) + 1);
			if (!listResults.contains(tempResult)) {
				listResults.add(tempResult);
				keyPool -= 1;
			}
		}
		
		 Collections.sort(listResults);
		
		SendMessage("Open the following chests: \n" + listResults.toString(), cmd.DESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}
	
}
