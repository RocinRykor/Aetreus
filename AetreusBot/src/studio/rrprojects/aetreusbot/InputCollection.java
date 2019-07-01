package studio.rrprojects.aetreusbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreusbot.command.CommandParser;
import studio.rrprojects.aetreusbot.gui.MainWindowController;

public class InputCollection {
	private static String[] inputArray = new String[100];
	
	public static void ParseNewInput(String newInput) {
		ParseNewInput(newInput, null);
	}

	public static void ParseNewInput(String newInput, MessageReceivedEvent event) {
		System.out.println(newInput);
		UpdateArray(newInput);
		
		CommandParser.initialParse(newInput, event);
	}

	public static void UpdateArray(String newInput) {
		for (int i = inputArray.length-1; i >= 0; i--) {
			
			if (i == inputArray.length-1) {
				inputArray[i] = null;
			} else if (i > 0) {
				inputArray[i+1] = inputArray[i];
			} else { 
				inputArray[i+1] = inputArray[i];
				inputArray[i] = newInput + " \n"; //appends new line to all new additions to GUI Console
			}
		}
		
		MainWindowController.UpdateConsoleArea(inputArray);
	}

	public static void ClearArray() {
		for (int i = 0; i < inputArray.length; i++) {
			inputArray[i] = null;
		}
		MainWindowController.UpdateConsoleArea(inputArray);
	}

}
