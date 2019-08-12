package studio.rrprojects.aetreusbot.command;

import java.awt.Color;
import java.util.ArrayList;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.questions.AnswerHandler;
import studio.rrprojects.aetreusbot.questions.Question;
import studio.rrprojects.aetreusbot.questions.Question.QuestionContainer;
import studio.rrprojects.aetreusbot.utils.NewMessage;

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
	public String getAlias() {
		return "T";
	}

	@Override
	public String getHelpDescription() {
		return "Have a conversation with Aetreus";
	}

	@Override
	public String getHomeChannel() {
		return "BotTesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return true;
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
	
	public void executeMain(CommandContainer cmd) {
		String primaryArg = cmd.MAIN_ARG;
		String trimmedNote = cmd.TRIMMED_NOTE;
		Channel channel = cmd.DESTINATION;
		
		
		if (primaryArg.equalsIgnoreCase("help")) {
			//sendMessage(helpMessage(), channel);
			return;
		}
		
		User user = cmd.AUTHOR;
		ArrayList<Object> userAnswers = AnswerHandler.parseAnswer(user, questions, trimmedNote, "Talk", channel);
		
		if (userAnswers != null) {
			ReportAnswers(userAnswers, cmd);
		}
	}

	private void ReportAnswers(ArrayList<Object> collectedAnswers, CommandContainer cmd) {
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setTitle("List of Answers");
		
		String[] colorInt = collectedAnswers.get(2).toString().split("; ");
		Color newColor = new Color(Integer.parseInt(colorInt[0]), Integer.parseInt(colorInt[1]), Integer.parseInt(colorInt[2]));
		
		builder.setColor(newColor);
		
		for (int i = 0; i < collectedAnswers.size(); i++) {
			builder.addField("Question #" + i, collectedAnswers.get(i).toString(), false);
		}
		
		//event.getChannel().sendMessage(builder.build()).queue();
	}
	
	private void SendMessage(String message, Channel DESTINATION, User user) {
		NewMessage.send(message, DESTINATION, user);
	}

}
