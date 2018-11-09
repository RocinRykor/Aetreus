package com.rocinrykor.aetreusbot.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SendMessage {

	public void sendMessage(String message, MessageReceivedEvent event, boolean isTTS) {
		event.getChannel().sendMessage(message).tts(isTTS).queue();
	}

}
