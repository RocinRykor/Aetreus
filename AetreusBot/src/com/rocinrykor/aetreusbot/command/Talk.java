package com.rocinrykor.aetreusbot.command;

import java.awt.Color;
import java.util.ArrayList;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.command.CommandParser.CommandContainer;
import com.rocinrykor.aetreusbot.questions.AnswerHandler;
import com.rocinrykor.aetreusbot.questions.Question;
import com.rocinrykor.aetreusbot.questions.Question.QuestionContainer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Talk extends Command {
	ArrayList<QuestionContainer> questions;
	public Talk() {
		questions = new ArrayList<>();
		
		questions.add(Question.NewQuestion("What is your name?", "String"));
		//questions.add(Question.NewQuestion("Good now, pick from this list: Red, Green, or Blue.", "String", "Red", "Green", "Blue"));
		questions.add(Question.NewQuestion("Ok now time for a complex question. \n"
				+ "Imagine you are generating a character for D&D, and this question was for detimemining equipment. \n"
				+ "Respond with an item name and it's quantity seperated by \", \"", "String, Int"));
		questions.add(Question.NewQuestion("Give me the RGB (0-255 for each color) value of your favorite color, seperated by  \", \". \n"
				+ "Your answer should look like \'&talk \"152, 200, 30\" \'", "Int, Int, Int", 0, 255));
		//questions.add(Question.NewQuestion("Pick any number that you want?", "Int"));
		//questions.add(Question.NewQuestion("Now, pick a new number that is a positive number between 1-100", "Int", 1, 100));
		//questions.add(Question.NewQuestion("This is a repeating question, type anything you want, to finish repsond with \"skip\"", "String", true));
		//questions.add(Question.NewQuestion("Finally, Is my name Aetreus?", "Boolean"));
		
	}

	@Override
	public String getName() {
		return "Talk";
	}

	@Override
	public String getDescription() {
		return "Have a conversation with Aetreus";
	}

	@Override
	public String getAlias() {
		return "T";
	}
	
	@Override
	public String getHomeChannel() {
		return "bottesting";
	}

	@Override
	public String helpMessage() {
		return "With each use of this command, I will ask you a question, by placing your answer in quotation marks, you and I can have a conversation.";
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
	}
	
	@Override
	public boolean isAdultResricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}

	@Override
	public void execute(String primaryArg, String[] secondaryArg, String trimmedNote, MessageReceivedEvent event,
			CommandContainer cmd, MessageChannel channel) {

		if (primaryArg.equalsIgnoreCase("help")) {
			sendMessage(helpMessage(), channel);
			return;
		}
		User user = event.getAuthor();
		ArrayList<Object> userAnswers = AnswerHandler.parseAnswer(user, questions, trimmedNote, "Talk", event, channel);
		
		if (userAnswers != null) {
			ReportAnswers(userAnswers, event);
		}
	}

	private void ReportAnswers(ArrayList<Object> collectedAnswers, MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("List of Answers");
		
		String[] colorInt = collectedAnswers.get(2).toString().split("; ");
		Color newColor = new Color(Integer.parseInt(colorInt[0]), Integer.parseInt(colorInt[1]), Integer.parseInt(colorInt[2]));
		
		builder.setColor(newColor);
		
		for (int i = 0; i < collectedAnswers.size(); i++) {
			builder.addField("Question #" + i, collectedAnswers.get(i).toString(), false);
		}
		
		event.getChannel().sendMessage(builder.build()).queue();
	}

	public void sendMessage(EmbedBuilder builder, MessageChannel channel) {
		BotController.sendMessage(builder, channel);
	}

	public void sendMessage(String message, MessageChannel channel) {
		BotController.sendMessage(message, channel);
	}

}
