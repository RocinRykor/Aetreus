package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.InputCollection;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;


public class Adult extends Command{

	@Override
	public String getName() {
		return "Adult";
	}

	@Override
	public String getAlias() {
		return "Adult";
	}

	@Override
	public String getHelpDescription() {
		return "Grants the \"Adult\" role to the user, allowing them to access the restriced NSFW channel";
	}

	@Override
	public String getHomeChannel() {
		return "nsfw";
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}
	
	@Override
	public boolean isAdultRestricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	public void executeMain(CommandContainer cmd)  {
		
		if (cmd.AUTHOR.isFake()) {
			InputCollection.UpdateArray("ERROR: FAKE USERS CANNOT RUN THIS COMMAND!");
			return;
		}
		
		Guild guild = Controller.getJda().getGuilds().get(0);
		Member member = guild.getMember(cmd.AUTHOR);
		Role role = guild.getRolesByName("Adult", true).get(0);
		
		String introMessage = "The NSFW channel is a restricted channel which may contain pictures or videos of an adult nature. \n"
				+ "Before I give you access to this channel I need to know that you understand and are comfortable with such content. \n\n"
				+ "If you wish access to this channel and its contents please run this command again with the note of \"I Understand\" \n"
				+ "Example &adult \"I Understand\" \n\n"
				+ "Note: at any point in the future you can remove access by running this command again with the phrase \"Remove\" ";
		
		String accessGrantedMessage = "Alright, I have givin you access to the restricted content. All adult-only commands will now show up when you run the &help command. \n"
				+ "Please remember to keep all such commands confined to the NSFW channel.";
		
		String accessRemovalMessage = "Alright, I have removed your access. If at any time you wish to have it reinstated just use the &adult command again.";
		
		String message = introMessage;
		
		if (cmd.TRIMMED_NOTE != null) {
			if (cmd.TRIMMED_NOTE.equalsIgnoreCase("I Understand")) {
				GrantPermission(member, role, guild);
				message = accessGrantedMessage;
			} else if (cmd.TRIMMED_NOTE.equalsIgnoreCase("remove")) {
				RevokePermission(member, role, guild);
				message = accessRemovalMessage;
			}
		}
		
		cmd.AUTHOR.openPrivateChannel().complete()
			.sendMessage(message).queue();
		
		/*
		System.out.println(cmd.DESTINATION.getJDA().getGuilds().get(0).getMemberById(cmd.AUTHOR.getIdLong()));
		Member member = cmd.DESTINATION.getJDA().getGuilds().get(0).getMemberById(cmd.AUTHOR.getIdLong());
		Role role = cmd.DESTINATION.getJDA().getRolesByName("Adult", true).get(0);
		
		String introMessage = "The NSFW channel is a restricted channel which may contain pictures or videos of an adult nature. \n"
				+ "Before I give you access to this channel I need to know that you understand and are comfortable with such content. \n\n"
				+ "If you wish access to this channel and its contents please run this command again with the note of \"I Understand\" \n"
				+ "Example &adult \"I Understand\" \n\n"
				+ "Note: at any point in the future you can remove access by running this command again with the phrase \"Remove\" ";
		
		String accessGrantedMessage = "Alright, I have givin you access to the restricted content. All adult-only commands will now show up when you run the &help command. \n"
				+ "Please remember to keep all such commands confined to the NSFW channel.";
		
		String accessRemovalMessage = "Alright, I have removed your access. If at any time you wish to have it reinstated just use the &adult command again.";
		
		
		String message = introMessage;
		
		if (cmd.TRIMMED_NOTE != null) {
			if (cmd.TRIMMED_NOTE.equalsIgnoreCase("I Understand")) {
				GrantPermission(member, role, cmd);
				message = accessGrantedMessage;
			} else if (cmd.TRIMMED_NOTE.equalsIgnoreCase("remove")) {
				RevokePermission(member, role, cmd);
				message = accessRemovalMessage;
			}
		}
		
		cmd.AUTHOR.openPrivateChannel().complete()
			.sendMessage(message).queue();
		*/
	}

	private void GrantPermission(Member member, Role role, Guild guild) {
		try {
			guild.getController().addSingleRoleToMember(member, role).complete();
			System.out.println("Role Granted");
		} catch (Exception e) {
			System.out.println("ERROR: Unable to grant role");
		}
	}

	private void RevokePermission(Member member, Role role, Guild guild) {
		try {
			guild.getController().removeSingleRoleFromMember(member, role).complete();
			System.out.println("Role Revoked");
		} catch (Exception e) {
			System.out.println("ERROR: Unable to remove role");
		}
	}
}
