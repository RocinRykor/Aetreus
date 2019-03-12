package com.rocinrykor.aetreusbot.command;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Adult extends Command {

	@Override
	public String getName() {
		return "Adult";
	}

	@Override
	public String getDescription() {
		return "Grants the \"Adult\" role to the user, allowing them to access the restriced NSFW channel";
	}

	@Override
	public String getAlias() {
		return "Adult";
	}

	@Override
	public String getHomeChannel() {
		return "rolling";
	}

	@Override
	public String helpMessage() {
		return "WIP";
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
	public boolean isAdultResricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return true;
	}
	
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event, CommandContainer cmd, MessageChannel channel) {
		
		System.out.println(BotController.getGuild().getMemberById(event.getAuthor().getIdLong()));
		Member member = BotController.getGuild().getMemberById(event.getAuthor().getIdLong());
		Role role = event.getJDA().getRolesByName("Adult", true).get(0);
		
		String introMessage = "The NSFW is a restricted channel which may contain pictures or videos of an adult nature. \n"
				+ "Before I give you access to this channel I need to know that you understand and are confortable such content. \n\n"
				+ "If you wish access to this channel and it's contents please run this command with the note of \"I Understand\" \n"
				+ "Example &adult \"I Understand\" \n\n"
				+ "Note: at any point in the future you can remove access by running this command again with the phrase \"Remove\" ";
		
		String accessGrantedMessage = "Alright, I have givin you access to the restricted content. All adult-only commands will now show up when you run the &help command. \n"
				+ "Please remember to keep all such commands confined to the NSFW channel.";
		
		String accessRemovalMessage = "Alright, I have removed your access. If at any time you wish to have it reinstated just use the &adult command again.";
		
		
		String message = introMessage;
		if (primaryArg.equalsIgnoreCase("help")) {
			message = helpMessage();
		}
		
		
		if (trimmedNote != null) {
			if (trimmedNote.equalsIgnoreCase("I Understand")) {
				GrantPermission(member, role, event);
				message = accessGrantedMessage;
			} else if (trimmedNote.equalsIgnoreCase("remove")) {
				RevokePermission(member, role, event);
				message = accessRemovalMessage;
			}
		}
		
		event.getAuthor().openPrivateChannel().complete()
			.sendMessage(message).queue();
		
	}

	private void GrantPermission(Member member, Role role, MessageReceivedEvent event) {
		try {
			BotController.getGuild().getController().addSingleRoleToMember(member, role).complete();
			System.out.println("Role Granted");
		} catch (Exception e) {
			System.out.println("ERROR: Unable to grant role");
		}
	}

	private void RevokePermission(Member member, Role role, MessageReceivedEvent event) {
		try {
			BotController.getGuild().getController().removeSingleRoleFromMember(member, role).complete();
			System.out.println("Role Revoked");
		} catch (Exception e) {
			System.out.println("ERROR: Unable to remove role");
		}
	}

	@Override
	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
	}

	@Override
	public void sendMessage(String message, MessageChannel channel) {
	}

}
