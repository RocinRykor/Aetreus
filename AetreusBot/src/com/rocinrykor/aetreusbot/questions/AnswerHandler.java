package com.rocinrykor.aetreusbot.questions;

import java.util.ArrayList;
import java.util.HashMap;

import com.rocinrykor.aetreusbot.BotController;
import com.rocinrykor.aetreusbot.questions.Question.QuestionContainer;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AnswerHandler {
	static ArrayList<Object> userAnswers = new ArrayList<>();
	static HashMap<String, Object> answerTable = new HashMap<>(); 
	static ArrayList<Object> collectedAnswers;
	static HashMap<String, Integer> userIDTable = new HashMap<>(); //User Progress
	
	static boolean isValid;
	static String message;
	static int userKey;
	
	public static ArrayList parseAnswer(User user, ArrayList<QuestionContainer> questions, String answer, String source, MessageReceivedEvent event, MessageChannel channel) {
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
		BotController.sendMessage(message, channel);
		
		return null;
	}

	private static ArrayList Finalize(String userReference, ArrayList<QuestionContainer> questions) {
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
	
	/*
	 * @Override
	public void called(CommandContainer cmd) {
		String primaryArg = cmd.primaryArg;
		MessageReceivedEvent event = cmd.event;
		
		if (BotController.CheckMessage(event)) {
			if (primaryArg.equalsIgnoreCase("help")) {
				helpAction(cmd);
			} else {
				mainAction(cmd);
			}
		}
	}

	@Override
	public void mainAction(CommandContainer cmd) {
		String primaryArg = cmd.primaryArg;
		String[] secondaryArg = cmd.secondaryArg;
		String trimmedNote = cmd.trimmedNote;
		MessageReceivedEvent event = cmd.event;
		
		String userID = event.getAuthor().getId();
		finalMessage = "";
		
		if (userIDTable.containsKey(userID)) {
			ProgressQuestion(userID, trimmedNote);
		} else {
			userIDTable.put(userID, -1);
			ProgressQuestion(userID, trimmedNote);
		}
		
		SendMessage(finalMessage, event);
	
	}
	
	@Override
	public Runnable helpAction(CommandContainer cmd) {
		String HelpString = "Use this command to get started using the bot!";
		SendMessage(HelpString, cmd.event);
		return null;
	}
	
	private void SendMessage(String message, MessageReceivedEvent event) {
		BotController.NewMessage(message, event);
	}

	private void AddQuestion(Integer number, String question, String type) {
		//Adds a question to the relevant tables to ensure question and type are linked correctly
		questionNumber.put(number, question);
		questionType.put(number, type);
	}
	
	private Integer GetQuestionNumber(String userID) {
		//Looks ups user's progress to return the question they are currently on
		Integer qNumber = userIDTable.get(userID);
		return qNumber;
	}
	
	private String GetQuestion(String userID) {
		//Gets the actual question based on question number
		String question = questionNumber.get(GetQuestionNumber(userID));
		return question;
	}
	
	private void AskQuestion(String userID) {
		//Bot add the next question to its message
		finalMessage += GetQuestion(userID);
	}
	
	private void GetResponse(String userID, String trimmedNote) {
		userResponse =  "";
		
		if (CheckTrimmedNote(trimmedNote)) {
			//Sanitize the input *Fuck you, Trent* :P
			String sanitizedString = SanitizeInput(trimmedNote);
			
			//Check the sanatized string against the question type to confirm its validity
			if (GetQuestionType(userID).equalsIgnoreCase("String")) {
				isValid = true;
				userResponse = sanitizedString;
			} else if (GetQuestionType(userID).equalsIgnoreCase("Int")) {
				userResponse = CheckInt(sanitizedString);
			} else {
				userResponse = CheckBoolean(sanitizedString);
			}
			
			//If the string passes the validity check, store it in the final table
			if (isValid) {
				StoreResponse(userID, userResponse);
			} 
		} else {
			finalMessage += "I'm sorry, if you wrote a response I can't see it. \n"
					+ "Please place your response withen quotation marks. \n";
		}
		
	}

	private boolean CheckTrimmedNote(String trimmedNote) {
		if (!(trimmedNote == null)) {
			return true;
		} else {
			return false;
		}
	}

	private void StoreResponse(String userID, String userResponse) {
		//Stores the user's response - links it to a combination of their id and the original question number for tracking purposes
		String combinedKey = userID + "-" + GetQuestionNumber(userID);
		userResponseTable.put(combinedKey, userResponse);
	}
	
	private String SanitizeInput(String trimmedNote) {
		//Removes anything that is not a letter, number, -, or () to avoid any escape codes
		String cleanString = trimmedNote.replaceAll("[^A-Za-z0-9\\-\\+\\(\\)]", "");
		return cleanString;
	}

	private String GetQuestionType(String userID) {
		//Uses question number to find what type of answer the bot is expecting
		String type = questionType.get(GetQuestionNumber(userID));
		return type;
	}

	private String CheckBoolean(String sanitizedString) {
		String cutString;
		
		//Validity check, passes if and if statement is either "True" or "False", any additional characters will cause a failure
		if (sanitizedString.equalsIgnoreCase("true")) {
			isValid = true;
			return sanitizedString;
		} else if (sanitizedString.equalsIgnoreCase("false")) {
			isValid = true;
			return sanitizedString;
		} else {
			finalMessage += "It looks like your answer does not contain the words True or False. \n";
			isValid = false;
			return "ERROR BOOLEAN";
		}
		
	}

	private String CheckInt(String sanitizedString) {
		String justNumbers = "";
		
		//Validity check, removes anything that is not a number, passes only if there are number present after everything else has been removed
		justNumbers = sanitizedString.replaceAll("[^0-9]", "");
		if (justNumbers.length() > 0) {
			isValid = true;
			return justNumbers;
		} else {
			finalMessage += "There doesnt seem to be any number's there \n";
			isValid = false;
			return "ERROR NUMBERS";
		}
	}
	
	private void NextStep(String userID) {
		int currentNum = GetQuestionNumber(userID);
		//Advances the selected user to the next question
		int newNum = currentNum + 1;
		userIDTable.replace(userID, newNum);
	}
	
	private void ProgressQuestion(String userID, String trimmedNote) {
		isValid = false;
		
		//Grabs the question that the user is on and progress based on the following:
		if (GetQuestionNumber(userID) == -1) {
			//If the user has not started, display the beginning message explaining how to respond, 
			//advance them to the first question and then ask the first question
			finalMessage += "Hello, my name is Aetreus and I am here to serve. Let's begin by having you answer a couple questions.\n"
					+ "Please respond by typing the &start command again and placing your answer withen quoation marks. \n"
					+ "For example your response should look something like: &start \"Blue\" \n";
			NextStep(userID);
			AskQuestion(userID);
		} else if (GetQuestionNumber(userID) >= 0 && GetQuestionNumber(userID) < questionNumber.size() - 1) {
			//Once the user has begun, before they get to the final question, get their response and check its validity
			//If they pass, advance them and ask the next question.
			//If they fail, explain why but don't advance them (repeat the question)
			GetResponse(userID, trimmedNote);
			
			if (isValid) {
				NextStep(userID);
				AskQuestion(userID);
			} else {
				finalMessage += "I'm sorry, that answer doesnt quite line up with what I was expecting, please try again \n";
			}
			
			
		} else if (GetQuestionNumber(userID) == questionNumber.size () - 1) {
			//Once they get to the final question, grab the user response and check it's validity
			//If they pass, call the function that processes all of their responses accordingly
			//If they fail, explain why but don't advance them (repeat the question)
			GetResponse(userID, trimmedNote);
			
			if (isValid) {
				FinishQuestions(userID);
			} else {
				finalMessage += "I'm sorry, that answer doesnt quite line up with what I was expecting, please try again \n";
			}
			
		} else {
			System.out.println("Something went wrong!");
			System.out.println(GetQuestionNumber(userID));
		}
	}

	private void FinishQuestions(String userID) {
		String[] answer = new String[questionNumber.size()];
		
		//Grab the user's responses from the table, store it in an array and then remove them from the table (cleanup)
		for (int i = 0; i < answer.length; i++) {
			String key = userID + "-" + i;
			answer[i] = userResponseTable.get(key);
			userResponseTable.remove(key);
		}
		
		//Reset the user's progress on the question table so that they may repeat the process
		userIDTable.replace(userID, -1);
		
		//Process the answers accordingly
		finalMessage += "Thank you for taking the time to answer my questions. \n"
				+ "Sadly, I am unable to do anything with your responses at this time, please check back later.";
	}
	 * */
}
