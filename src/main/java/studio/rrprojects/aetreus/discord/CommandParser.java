package studio.rrprojects.aetreus.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;

public class CommandParser {

    public CommandContainer Parse(String input, MessageReceivedEvent event) {
        System.out.println("Processing: " + input);
        String raw = input;

        //remove any extra space after the prefix before the first command.
        raw = RemoveExtraSpaces(raw);
        raw = RemoveFrontSpaces(raw);

        //remove anything after the first word
        String[] breakdown = raw.split(" ");
        String mainCommand = breakdown[0];

        String commandMods = RemoveFrontSpaces(raw.replaceFirst(mainCommand, ""));
        return ModParser(mainCommand, commandMods, event);

    }

    // UTILS
    private String RemoveExtraSpaces(String input) {
        return input.trim().replaceAll("[ ]{2,}", " "); //Remove extra spaces
    }

    private String RemoveFrontSpaces(String input) {
        while (input.startsWith(" ")) {
            input = input.replaceFirst(" ", "");
        }
        return input;
    }

    // CONTAINERS
    private CommandContainer ModParser(String command, String raw, MessageReceivedEvent event) {
        //All Info that will be needed in the final container
        /*
         * Author
         * Channel
         * Time
         *
         * Destination
         *
         * Main Arg
         * Secondary Arg
         * Note
         * */

        User AUTHOR;
        MessageChannel CHANNEL;
        OffsetDateTime TIME;

        MessageChannel DESTINATION;

        JDA JDA;

        String MAIN_ARG;
        String[] SECONDARY_ARG = null;
        String NOTE_ARG;
        String TRIMMED_RAW;
        String TRIMMED_NOTE = null;


        AUTHOR = event.getAuthor();
        CHANNEL = event.getTextChannel();
        TIME = event.getMessage().getTimeCreated();
        JDA = event.getJDA();
        DESTINATION = CHANNEL;


        if (raw.contains("\"")) { //Check if beheaded string has a note and if so extract it.
            int quotationIndex = raw.indexOf("\"");

            TRIMMED_RAW = raw.substring(0, quotationIndex);
            NOTE_ARG = raw.substring(quotationIndex);

            TRIMMED_NOTE = NOTE_ARG.replace("\"", "");

        } else {
            TRIMMED_RAW = raw;
        }

        String[] argBreakdown = TRIMMED_RAW.split(" ");

        if (argBreakdown.length > 0) {
            MAIN_ARG = argBreakdown[0];

            if (argBreakdown.length > 1) {
                SECONDARY_ARG = new String[argBreakdown.length - 1];
                System.arraycopy(argBreakdown, 1, SECONDARY_ARG, 0, SECONDARY_ARG.length);
            }

        } else {
            MAIN_ARG = null;
            SECONDARY_ARG = null;
        }


        return new CommandContainer(AUTHOR, CHANNEL, TIME, DESTINATION, JDA, command, MAIN_ARG, SECONDARY_ARG, TRIMMED_RAW, TRIMMED_NOTE, event);
    }
}


