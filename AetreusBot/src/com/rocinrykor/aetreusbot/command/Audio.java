package com.rocinrykor.aetreusbot.command;

import javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.music.MusicManager;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Audio extends Command {

	@Override
	public String getName() {
		return "Audio";
	}

	@Override
	public String getDescription() {
		return "Plays sounds in the voice channel of the user";
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
	public String helpMessage() {
		return "Ever want to play a sound to the entire voice channel? \n"
				+ "Perhaps something to announce to everyone that you have arrived \n"
				+ "Maybe, you wish to sing a song, or delcare the longevity of various musical genres. \n"
				+ "Well then, never fear, because I am here! \n\n"
				+ "To designate a song to play type in the the following command \"&audio play\" and then put a link to the track withen quotation marks. \n"
				+ "Example &audio play \"https://www.youtube.com/watch?v=ttKn1eGKTew\"";
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
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd, MessageChannel channel) {
		
		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), channel);
			return;
		} else if (primaryArg.equalsIgnoreCase("join")) {
			JoinChannel(cmd.guild, cmd.user, event, channel);
			manager.loadTrack(event.getTextChannel(), "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\Audio Tracks\\I_AM_HERE.mp3");
		} else if (primaryArg.equalsIgnoreCase("leave")) {
			LeaveChannel(cmd.guild);
		} else if (primaryArg.equalsIgnoreCase("play")) {
			LoadTrack(cmd.guild, cmd.user, trimmedNote, event, channel);
		} else if (primaryArg.equalsIgnoreCase("skip")) {
			SkipTrack(cmd.guild, event);
		} else if (primaryArg.equalsIgnoreCase("volume")) {
			ChangeVolume(cmd.guild, secondaryArg, event, channel);
		} else {
			sendMessage("I don't understand your command.", channel);
		}
	}

	private void ChangeVolume(Guild guild, String[] secondaryArg, MessageReceivedEvent event, MessageChannel channel) {
		int volume = 50;
		if (secondaryArg != null) {
			try {
				volume = Integer.parseInt(secondaryArg[0]);
			} catch (Exception e) {
				String message = "Volume amount not specified. \n"
						+ "Current volume: " + manager.getPlayer(guild).getAudioPlayer().getVolume();
				
				sendMessage(message, channel);
				return;
			}
		} else {
			String message = "Volume amount not specified. \n"
					+ "Current volume: " + manager.getPlayer(guild).getAudioPlayer().getVolume();
			
			sendMessage(message, channel);
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

	private void LoadTrack(Guild guild, User user, String trimmedNote, MessageReceivedEvent event, MessageChannel channel) {
		
		String source = trimmedNote;
		
		TextChannel textChannel = event.getTextChannel();
		
		if(guild == null) return;
		
		JoinChannel(guild, user, event, channel);
		
		if (source != null) {
			manager.loadTrack(textChannel, source);
		} else {
			source = "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\Audio Tracks\\ERROR.mp3";
			manager.loadTrack(textChannel, source);
			
		}
	}

	private void SkipTrack(Guild guild, MessageReceivedEvent event) {
		/**/
		String source = "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\ForceStop.wav";
		System.out.println("Attempting to skip track.");
		TextChannel textChannel = event.getTextChannel();
		manager.loadTrack(textChannel, source);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		manager.getPlayer(guild).skipTrack();
	}

	private void LeaveChannel(Guild guild) {
		if (guild.getAudioManager().isConnected()) {
			guild.getAudioManager().closeAudioConnection();
		}
	}

	private void JoinChannel(Guild guild, User user, MessageReceivedEvent event, MessageChannel channel) {
		
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
			VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
			if(voiceChannel == null){
				BotController.sendMessage("You must be connect to a voice channel.", channel);;
			}
			guild.getAudioManager().openAudioConnection(voiceChannel);
			
			TextChannel textChannel = event.getTextChannel();
		}
	}

	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}

	@Override
	public boolean isAdultResricted() {
		return false;
	}
	
}
