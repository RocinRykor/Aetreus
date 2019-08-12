package studio.rrprojects.aetreusbot.questions;

import java.util.ArrayList;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import studio.rrprojects.aetreusbot.questions.Question.QuestionContainer;

public class AnswerHandler {
	static ArrayList<Object> userAnswers = new ArrayList<>();
	static HashMap<String, Object> answerTable = new HashMap<>(); 
	static ArrayList<Object> collectedAnswers;
	static HashMap<String, Integer> userIDTable = new HashMap<>(); //User Progress
	
	static boolean isValid;
	static String message;
	static int userKey;
	
	public static ArrayList<Object> parseAnswer(User user, ArrayList<QuestionContainer> questions, String answer, String source, Channel channel) {
		message = "";
		
		String userReference = user.getId() + " - " + source;		
		if (!userIDTable.containsKey(userReference)) {
			userIDTable.put(userReference, -1);
		}
		
		userKey = userIDTable.get(userReference);
		
		isValid = false;
		
		//Grabs the question that the user is on and progress based on the following:
		if (userKey == -1) {
			//If the user has not started, display the beginning message explaining how to respond, 
			//advance them to the first question and then ask the first question
			message += "Hello, my name is Aetreus and I am here to serve. Let's begin by having you answer a couple questions.\n"
					+ "Please respond by typing the command again and placing your answer withen quoation marks. \n"
					+ "For example your response should look something like: &" + source + " \"Your Answer\" \n\n";
			
			AdvanceProgress(userReference);
			AskQuestion(questions);
		} else if (userKey >= 0 && userKey < questions.size()- 1) {
			//Once the user has begun, before they get to the final question, get their response and check its validity
			//If they pass, advance them and ask the next question.
			//If they fail, explain why but don't advance them (repeat the question)
			parseResponce(questions, answer, userReference);
			
			if (isValid) {
				AdvanceProgress(userReference);
				AskQuestion(questions);
			} else {
				message += "I'm sorry, that answer doesnt quite line up with what I was expecting, please try again \n";
			}
			
			
		} else if (userKey == questions.size()- 1) {
			//Once they get to the final question, grab the user response and check it's validity
			//If they pass, call the function that processes all of their responses accordingly
			//If they fail, explain why but don't advance them (repeat the question)
			
			parseResponce(questions, answer, userReference);
			
			if (isValid) {
				return Finalize(userReference, questions);
			} else {
				message += "I'm sorry, that answer doesnt quite line up with what I was expecting, please try again \n";
			}
			
		} else {
			System.out.println("Something went wrong!");
			System.out.println(userKey);
		}
		
		
		userIDTable.replace(userReference, userKey);
		//BotController.sendMessage(message, channel);
		
		return null;
	}

	private static ArrayList<Object> Finalize(String userReference, ArrayList<QuestionContainer> questions) {
		System.out.println("Finished");
		collectedAnswers = new ArrayList<>();
		System.out.println(answerTable.size());
		userIDTable.replace(userReference, -1);
		
		for (int i = 0; i < questions.size(); i++) {
			String key = userReference + " : " + i;
			collectedAnswers.add(answerTable.get(key));
			answerTable.remove(key);
		}
		
		return collectedAnswers;
	}

	private static void parseResponce(ArrayList<QuestionContainer> questions, String answer, String userReference) {
		QuestionContainer question = questions.get(userKey);
		String key = userReference + " : " + userKey;
		
		if (answer == null) {
			message += "Sorry, looks like you haven't written a response, or you did not place it in quotation marks. Please try again.";
			return;
		}
		
		if (question.repeating) {
			if (answer.toLowerCase().contains("skip")) {
				if(answerTable.containsKey(key)) {
					isValid = true;
					return;
				} else {
					isValid = false;
					message = "Sorry, this is a repeatable question and I need at least one valid answer before you can skip it."; 
					return;
				}
			}
		}
				
		String[] answerType = question.type.split(", ");
		
		String[] userAnswer = answer.split(", ");
		
		if (userAnswer.length < answerType.length) {
			message = "Sorry, this question is expecting multiple answers, please answer each part of the question in order with \", \" used to seperate the individual answers";
			isValid = false;
			return;
		}
		
		for (int i = 0; i < answerType.length; i++) {
			userAnswer[i] = SanitizeAnswer(userAnswer[i]);
			
			if (answerType[i].equalsIgnoreCase("String")) { //Check String
				
				if (question.acceptedStrings != null) { //Check valid answers
					System.out.println();
					for (String str : question.acceptedStrings) {
						if (userAnswer[i].equalsIgnoreCase(str)) {
							isValid = true;
						}
					}
				} else {
					isValid = true;
				}
				
			} else if (answerType[i].equalsIgnoreCase("Int")) { //Check Int
				
				userAnswer[i] = userAnswer[i].replaceAll("[^0-9\\-]", "");
				
				int intAnswer;
				
				try {
					intAnswer = Integer.parseInt(userAnswer[i]);
				} catch (Exception e) {
					return;
				}
				
				if (question.rangeMin != 0 && question.rangeMax != 0) { //Check Range
					
					if (intAnswer >= question.rangeMin && intAnswer <= question.rangeMax) {
						isValid = true;
					}
					
				} else {
					isValid = true;
				}
				
			} else if (answerType[i].equalsIgnoreCase("Boolean")) {
				
				userAnswer[i] = ReturnBoolean(userAnswer[i]);
				
				if (userAnswer[i] != null) {
					isValid = true;
				}
				
			}
			
			
			if (isValid) {
				if (answerTable.containsKey(key)) {
					String newAnswer = answerTable.get(key) + "; " + userAnswer[i];
					System.out.println(newAnswer);
					answerTable.replace(key, newAnswer);
				} else {
					answerTable.put(key, userAnswer[i]);
				}
			}
			
			if (question.repeating) {
				userKey -= 1;
			} 
		}
		
	}

	private static String ReturnBoolean(String string) {
		String[] trueStrings = {"true", "yes", "ja", "da", "oui", "affirmative"};
		String[] falseStrings = {"false", "no", "nein", "nyet", "non", "negative"};
		
		for (String str : trueStrings) {
			if (string.toLowerCase().contains(str)) {
				return "True";
			}
		}
		
		for (String str : falseStrings) {
			if (string.toLowerCase().contains(str)) {
				return "False";
			}
		}
		
		return null;
	}

	private static String SanitizeAnswer(String string) {
		return string.replaceAll("[^A-Za-z0-9 ,\\\\-\\\\+\\\\(\\\\)]", "");
	}

	private static void AskQuestion(ArrayList<QuestionContainer> questions) {
		message += questions.get(userKey).question;
	}

	private static void AdvanceProgress(String userReference) {
		userKey++;
	}
}
