package studio.rrprojects.aetreus.utils;

import net.dv8tion.jda.api.entities.MessageChannel;

public class MessageUtils {
    public static void SendMessage(String input, MessageChannel channel) {
        channel.sendMessage(input).queue();
    }

    public static String BlockText(String input, String type) {
        String blockType = "";
        if (type != null) {
            blockType = type;
        }

        String output = "```" + blockType + "\n" + input
                + "\n```";

        return output;
    }
}
