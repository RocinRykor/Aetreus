package studio.rrprojects.aetreus.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

public class AudioListener extends AudioEventAdapter {

	private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
	private final MusicPlayer player;
	
	public AudioListener(MusicPlayer player){
		this.player = player;
	}
	
	public BlockingQueue<AudioTrack> getTracks() {
		return tracks;
	}
	
	public int getTrackSize(){
		return tracks.size();
	}
	
	public void nextTrack(){
		 if(tracks.isEmpty()){
		 	//Guild guild = player.getGuild();
			//if(player.getGuild().getAudioManager().getConnectedChannel() != null)
				//BotController.ResetPresence();
			return;
		}
		
		player.getAudioPlayer().startTrack(tracks.poll(), false);
	}
	

	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) nextTrack();
	}
	
	public void queue(AudioTrack track) {
		if (!player.getAudioPlayer().startTrack(track, true)) tracks.offer(track);
	}

}
