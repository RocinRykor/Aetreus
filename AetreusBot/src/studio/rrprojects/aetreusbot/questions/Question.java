package studio.rrprojects.aetreusbot.questions;

public class Question {
	public static QuestionContainer NewQuestion(String question, String type) {
		return new QuestionContainer(question, type, 0, 0, false, null);
	}
	
	public static QuestionContainer NewQuestion(String question, String type, boolean repeating) {
		return new QuestionContainer(question, type, 0, 0, repeating, null);
	}
	
	public static QuestionContainer NewQuestion(String question, String type, int rangeMin, int rangeMax) {
		return new QuestionContainer(question, type, rangeMin, rangeMax, false, null);
	}
	
	public static QuestionContainer NewQuestion(String question, String type, int rangeMin, int rangeMax, boolean repeating) {
		return new QuestionContainer(question, type, rangeMin, rangeMax, repeating, null);
	}
	
	public static QuestionContainer NewQuestion(String question, String type, String...acceptedStrings) {
		return new QuestionContainer(question, type, 0, 0, false, acceptedStrings);
	}
	
	public static QuestionContainer NewQuestion(String question, String type, boolean repeating, String...acceptedStrings) {
		return new QuestionContainer(question, type, 0, 0, repeating, acceptedStrings);
	}

	public static class QuestionContainer { //Stores all of the parsed variables for later use
		public final String question;
		public final String type;
		public final int rangeMin;
		public final int rangeMax;
		public final boolean repeating;
		public final String[] acceptedStrings;
		
		public QuestionContainer(String question, String type, int rangeMin, int rangeMax, boolean repeating, String...acceptedStrings) {
			this.question = question;
			this.type = type;
			this.rangeMin = rangeMin;
			this.rangeMax = rangeMax;
			this.repeating = repeating;
			this.acceptedStrings = acceptedStrings;
		}
	}

}
