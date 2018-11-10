package com.rocinrykor.aetreusbot.command;

import java.awt.Color;
import java.util.HashMap;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.baxter.BaxterController;
import com.rocinrykor.aetreusbot.baxter.Meter;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import gnu.trove.impl.PrimeFinder;
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
		return "It seems that what this server was truely missing was a small furry mascot. \n"
				+ "So I present to you: Baxter! \n\n"
				+ "To have Baxter join the server use \"baxter start\" \n"
				+ "To have him leave again us \"baxter stop\" \n"
				+ "To get an overview of baxter's current status use \"baxter status\" \n"
				+ "Otherwise, use any of the following commands to interact with Baxter by using \"baxter X\" \n"
				+ "Just replace X with any of the following: Play, Toys, Sleep, Nap, Feed, Treats, Bathe, or Wash";
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
	
	public static final Color VERY_LIGHT_GREEN = new Color(102,255,102);
	public static HashMap<String, String> baxterCommands;

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
		} else if (baxterCommands.containsKey(primaryArg.toLowerCase())); {
			Interaction(primaryArg, event);
		}
	}

	private void Interaction(String primaryArg, MessageReceivedEvent event) {
		String releventMeter = baxterCommands.get(primaryArg.toLowerCase());
		
		for (Meter meter : Meter.meters) {
			if (meter.getName().equals(releventMeter)) {
				if (meter.getIsRegenerating()) {
					return;
				}
				
				meter.setRegenAmount(500);
				meter.setIsRegenerating(true);
			}
		}
	}

	private void ReportStatus(MessageReceivedEvent event) {
		
		if (!BaxterController.isBaxterOnline()) {
			return;
		}
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("Baxter Status");
		builder.setColor(HealthToColor());
		
		for (Meter meter : Meter.meters) { 
			builder.addField(meter.getName(), "Meter Level: " + meter.getMeterLevel(), false);
		}
		
		event.getChannel().sendMessage(builder.build()).queue();
		
	}

	private Color HealthToColor() {
		int meterValue = Meter.meters.get(Meter.meters.size() -1).getMeterLevel();
		
		if (meterValue > 800) {
			return Color.GREEN;
		} else if (meterValue > 600 && meterValue <= 800) {
			return VERY_LIGHT_GREEN;
		} else if (meterValue > 400 && meterValue <= 600) {
			return Color.YELLOW;
		} else if (meterValue > 200 && meterValue <= 400) {
			return Color.ORANGE;
		} else {
			return Color.RED;
		}
	}

	@Override
	public void sendMessage(String message, MessageReceivedEvent event) {
		BotController.sendMessage(message, event);
	}

	public static void InitCommands() {
		baxterCommands = new HashMap<>();
		
		baxterCommands.put("feed", "Hunger");
		baxterCommands.put("treats", "Hunger");
		baxterCommands.put("bathe", "Hygiene");
		baxterCommands.put("wash", "Hygiene");
		baxterCommands.put("sleep", "Sleep");
		baxterCommands.put("nap", "Sleep");
		baxterCommands.put("play", "Playfulness");
		baxterCommands.put("toys", "Playfulness");
	}

}
