package studio.rrprojects.aetreus.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreus.audio.MusicManager;
import studio.rrprojects.aetreus.discord.BotListener;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

import java.nio.channels.Channel;

public class Audio extends BasicCommand {
    @Override
    public String getName() {
        return "Audio";
    }

    @Override
    public String getAlias() {
        return "A";
    }

    @Override
    public String getHelpDescription() {
        return "Plays music from Youtube and other sources";
    }

    private final MusicManager manager = new MusicManager();

    @Override
    public void executeMain(CommandContainer cmd) {
        String primaryArg = cmd.MAIN_ARG;
        MessageChannel channel = cmd.CHANNEL;
        Guild guild = channel.getJDA().getGuilds().get(0);
        //MessageReceivedEvent event = BotListener._event;

        if (primaryArg.equalsIgnoreCase("help")) {
            SendMessage(getHelpDescription(), channel);
            return;
        } else if (primaryArg.equalsIgnoreCase("join")) {
            JoinChannel(cmd);
           // manager.loadTrack(guild.getTextChannelsByName("bottesting", true).get(0), "C:\\Users\\Rocin Rykor\\Documents\\Aetreus Bot\\Audio Tracks\\I_AM_HERE.mp3");
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
        MessageChannel channel = cmd.CHANNEL;
        Guild guild = channel.getJDA().getGuilds().get(0);

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
        MessageChannel channel = cmd.CHANNEL;
        Guild guild = channel.getJDA().getGuilds().get(0);
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
        MessageChannel channel = cmd.CHANNEL;
        Guild guild = channel.getJDA().getGuilds().get(0);
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
        Guild guild = cmd.CHANNEL.getJDA().getGuilds().get(0);
        if (guild.getAudioManager().isConnected()) {
            guild.getAudioManager().closeAudioConnection();
        }
    }

    private void JoinChannel(CommandContainer cmd) {
        Guild guild = cmd.CHANNEL.getJDA().getGuilds().get(0);
        User user = cmd.AUTHOR;

        if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
            VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
            if(voiceChannel == null){
                SendMessage("You must be connect to a voice channel.", cmd.DESTINATION);
            }
            guild.getAudioManager().openAudioConnection(voiceChannel);
        }
    }
}
