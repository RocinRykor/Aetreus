package com.rocinrykor.aetreusbot.command;

import java.awt.Color;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.baxter.BaxterController;
import com.rocinrykor.aetreusbot.baxter.Meter;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Baxter extends Command {

	@Override
	public String getName() {
		return "Baxter";
	}

	@Override
	public String getDescription() {
		return "Take care a precious little kitten by the name of Baxter. Feed him, play with him, keep him happy.";
	}

	@Override
	public String getAlias() {
		return "Bax";
	}

	@Override
	public String helpMessage() {
		return "BAXTER HELP";
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd) {
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), event);
			return;
		} else if (primaryArg.equalsIgnoreCase("start")) {
			BaxterController.StartBaxter();
			return;
		} else if (primaryArg.equalsIgnoreCase("stop")) {
			BaxterController.StopBaxter();
			return;
		} else if (primaryArg.equalsIgnoreCase("status")) {
			ReportStatus(event);
		}
	}

	private void ReportStatus(MessageReceivedEvent event) {
		
		if (!BaxterController.isBaxterOnline()) {
			return;
		}
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("Baxter Status");
		builder.setColor(Color.YELLOW);
		
		for (Meter meter : Meter.meters) { 
			builder.addField(meter.getName(), "Meter Level: " + meter.getMeterLevel(), false);
		}
		
		event.getChannel().sendMessage(builder.build()).queue();
		
	}

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}

}
