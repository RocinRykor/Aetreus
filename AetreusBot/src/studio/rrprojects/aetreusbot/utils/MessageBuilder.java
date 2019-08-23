package studio.rrprojects.aetreusbot.utils;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;

public class MessageBuilder {

	public static EmbedBuilder BuildMessage(String title, String header, String message, Color color) {
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setColor(color);
		builder.setTitle(title);
		builder.addField(header, message, true);
		
		return builder;
	}
}
