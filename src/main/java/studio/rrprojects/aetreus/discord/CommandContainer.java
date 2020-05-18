package studio.rrprojects.aetreus.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;

public class CommandContainer {
        public final User AUTHOR;
        public final MessageChannel CHANNEL;
        public final OffsetDateTime TIME;
        public final MessageChannel DESTINATION;
        public final JDA JDA;
        public final String MAIN_COMMAND;
        public final String MAIN_ARG;
        public final String[] SECONDARY_ARG;
        public final String TRIMMED_RAW;
        public final String TRIMMED_NOTE;
        public final MessageReceivedEvent EVENT;


        public CommandContainer(User AUTHOR, MessageChannel CHANNEL, OffsetDateTime TIME, MessageChannel DESTINATION, JDA JDA, String MAIN_COMMAND, String MAIN_ARG, String[] SECONDARY_ARG, String TRIMMED_RAW, String TRIMMED_NOTE, MessageReceivedEvent EVENT) {
            this.AUTHOR = AUTHOR;
            this.CHANNEL = CHANNEL;
            this.TIME = TIME;
            this.DESTINATION = DESTINATION;
            this.JDA = JDA;
            this.MAIN_COMMAND = MAIN_COMMAND;
            this.MAIN_ARG = MAIN_ARG;
            this.SECONDARY_ARG = SECONDARY_ARG;
            this.TRIMMED_RAW = TRIMMED_RAW;
            this.TRIMMED_NOTE = TRIMMED_NOTE;
            this.EVENT = EVENT;
        }
}
