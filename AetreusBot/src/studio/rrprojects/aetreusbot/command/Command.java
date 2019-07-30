package studio.rrprojects.aetreusbot.command;

import java.util.ArrayList;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.DungeonsAndDragons;
import studio.rrprojects.aetreusbot.shadowrun.Shadowrun;

public abstract class Command {

	public static ArrayList<Command> commands;

	public static void init() {
		commands = new ArrayList<>();
		commands.add(new Ping());
		commands.add(new Message());
		commands.add(new Roll());
		commands.add(new Audio());
		commands.add(new Lab());
		
		//Shadowrun
		commands.add(new Shadowrun());
		
		//Dungeons and Dragons
		commands.add(new DungeonsAndDragons());
	}
	
	public abstract String getName();
	
	public abstract String getAlias();
	
	public abstract String getHelpDescription();
	
	//This is the text channel that the command should be forwarded to if the command is posted in a restricted channel.
	public abstract String getHomeChannel();
	
	public abstract boolean isChannelRestricted();
	
	public abstract boolean isAdminOnly();
	
	public abstract boolean deleteCallMessage();
	
	public void executeMain(CommandContainer cmd) {};
	
	public void executeHelp() {};
}
