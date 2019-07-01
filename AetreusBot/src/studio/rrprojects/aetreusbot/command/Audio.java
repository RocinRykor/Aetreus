package studio.rrprojects.aetreusbot.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.discord.BotListener;
import studio.rrprojects.aetreusbot.music.MusicManager;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Audio extends Command {

	@Override
	public String getName() {
		return "Audio";
	}

	@Override
	public String getAlias() {
		return "A";
	}
	
	@Override
	public String getHomeChannel() {
		return "bottesting";
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
		return true;
	}
	
	private final MusicManager manager = new MusicManager();

	@Override
	public void executeMain(CommandContainer cmd) {
		String primaryArg = cmd.MAIN_ARG;
		Channel channel = cmd.CHANNEL;
		Guild guild = channel.getGuild();
		MessageReceivedEvent event = BotListener._event;

		if (primaryArg.equalsIgnoreCase("help")) {
			SendMessage(getHelpDescription(), channel);
			return;
		} else if (primaryArg.equalsIgnoreCase("join")) {
			JoinChannel(cmd);
			manager.loadTrack(guild.getTextChannelsByName(getHomeChannel(), true).get(0), "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\Audio Tracks\\I_AM_HERE.mp3");
		} else if (primaryArg.equalsIgnoreCase("leave")) {
			LeaveChannel(cmd);
		} else if (primaryArg.equalsIgnoreCase("play")) {
			LoadTrack(cmd);
		} else if (primaryArg.equalsIgnoreCase("skip")) {
			SkipTrack(cmd);
		} else if (primaryArg.equalsIgnoreCase("volume")) {
			ChangeVolume(cmd);
		} else {
			SendMessage("I don't understand your command.", channel);
		}
	}

	private void ChangeVolume(CommandContainer cmd) {
		String[] secondaryArg = cmd.SECONDARY_ARG;
		Channel channel = cmd.CHANNEL;
		Guild guild = channel.getGuild();
		
		int volume = 50;
		if (secondaryArg != null) {
			try {
				volume = Integer.parseInt(secondaryArg[0]);
			} catch (Exception e) {
				String message = "Volume amount not specified. \n"
						+ "Current volume: " + manager.getPlayer(guild).getAudioPlayer().getVolume();
				
				SendMessage(message, channel);
				return;
			}
		} else {
			String message = "Volume amount not specified. \n"
					+ "Current volume: " + manager.getPlayer(guild).getAudioPlayer().getVolume();
			
			SendMessage(message, channel);
			return;
		}
		
		volume = VolumeClamping(volume);
		
		manager.getPlayer(guild).getAudioPlayer().setVolume(volume);
	}

	private int VolumeClamping(int volume) {
		int passOne = Math.max(0, volume);
		int passTwo = Math.min(100, passOne);
	
		return passTwo;
	}

	private void LoadTrack(CommandContainer cmd) {
		String[] secondaryArg = cmd.SECONDARY_ARG;
		Channel channel = cmd.CHANNEL;
		Guild guild = channel.getGuild();
		User user = cmd.AUTHOR;
		String source = cmd.TRIMMED_NOTE;
		TextChannel textChannel = (TextChannel) cmd.CHANNEL;
		
		if(guild == null) return;
		
		JoinChannel(cmd);
		
		if (source != null) {
			manager.loadTrack(textChannel, source);
		} else {
			source = "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\Audio Tracks\\ERROR.mp3";
			manager.loadTrack(textChannel, source);
			
		}
	}

	private void SkipTrack(CommandContainer cmd) {
		String[] secondaryArg = cmd.SECONDARY_ARG;
		Channel channel = cmd.CHANNEL;
		Guild guild = channel.getGuild();
		User user = cmd.AUTHOR;
		TextChannel textChannel = (TextChannel) cmd.CHANNEL;
		
		/**/
		String source = "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\ForceStop.wav";
		System.out.println("Attempting to skip track.");
		manager.loadTrack(textChannel, source);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		manager.getPlayer(guild).skipTrack();
	}

	private void LeaveChannel(CommandContainer cmd) {
		Guild guild = cmd.CHANNEL.getGuild();
		if (guild.getAudioManager().isConnected()) {
			guild.getAudioManager().closeAudioConnection();
		}
	}

	private void JoinChannel(CommandContainer cmd) {
		Guild guild = cmd.CHANNEL.getGuild();
		User user = cmd.AUTHOR;	
		
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
			VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
			if(voiceChannel == null){
				SendMessage("You must be connect to a voice channel.", cmd.DESTINATION);
			}
			guild.getAudioManager().openAudioConnection(voiceChannel);
		}
	}

	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}

	@Override
	public String getHelpDescription() {
		return "Ever want to play a sound to the entire voice channel? \n"
				+ "Perhaps something to announce to everyone that you have arrived \n"
				+ "Maybe, you wish to sing a song, or delcare the longevity of various musical genres. \n"
				+ "Well then, never fear, because I am here! \n\n"
				+ "To designate a song to play type in the the following command \"&audio play\" and then put a link to the track withen quotation marks. \n"
				+ "Example &audio play \"https://www.youtube.com/watch?v=ttKn1eGKTew\"";
	}
	
}
