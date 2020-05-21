package studio.rrprojects.aetreus.commands.admin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import studio.rrprojects.aetreus.discord.CommandContainer;

import java.util.ArrayList;

public class Delete extends AdminCommand {
    @Override
    public String getName() {
        return "Delete";
    }

    @Override
    public String getAlias() {
        return "Clear";
    }

    @Override
    public String getHelpDescription() {
        return "Admin command - Deletes a specified number (or All) messages in a p[articular text channel. \n" +
                "Note this command will not delete messages with an attachment or pin.";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        int deleteAmount = GetDeleteAmount(cmd.MAIN_ARG);

        if (!(cmd.DESTINATION instanceof TextChannel)) {
            System.out.println("Delete ERROR: Destination Channel is not a Text Channel - CANNOT DELETE!");
            return;
        }

        TextChannel destination = (TextChannel) cmd.DESTINATION;

        //Start by placing a message in the selected channel as a starting point and deleting it
        final Message[] startingMessage = {destination.sendMessage("Beginning Deletion: Please Stand By!").complete()};

        ArrayList<Message> messageList = new ArrayList<>();
        final int[] remainingAmount = {deleteAmount};
        final MessageHistory[] messageHistory = {startingMessage[0].getChannel().getHistoryBefore(startingMessage[0], 10).complete()};

        //Begin Deletion as a new thread
        new Thread(() -> {
            while (remainingAmount[0] > 0) {
                if (remainingAmount[0] >= 100) {
                    messageHistory[0] = startingMessage[0].getChannel().getHistoryBefore(startingMessage[0], 100).complete();
                    if (messageHistory[0].size() < 100) {
                        remainingAmount[0] = 0;
                    } else {
                        remainingAmount[0] -= messageHistory[0].size();
                    }
                } else {
                    messageHistory[0] = startingMessage[0].getChannel().getHistoryBefore(startingMessage[0], remainingAmount[0]).complete();
                    if (messageHistory[0].size() < remainingAmount[0]) {
                        remainingAmount[0] = 0;
                    } else {
                        remainingAmount[0] -= messageHistory[0].size();
                    }
                }

                messageList.addAll(messageHistory[0].getRetrievedHistory());
                startingMessage[0] = messageList.get(messageList.size() - 1);
            }

            System.out.println("Number of messages collected: " + messageList.size());

            for (Message message : messageList) {
                if (message.isPinned() || !message.getAttachments().isEmpty()) {
                    System.out.println("Message Protected - Skipped");
                } else {
                    try {
                        message.delete().queue();
                    } catch (Exception e) {
                        System.out.println("ERROR: CANNOT DELETE");
                    }

                }
            }

            System.out.println("Deletion Complete!");
        }).start();

        startingMessage[0].delete().complete();
    }

    // UTILS
    private int GetDeleteAmount(String input) {
        int defaultAmount = 10;

        if (input != null) {
            if(input.equalsIgnoreCase("all")) {
                return 9999;
            }

            try {
                Integer.parseInt(input);
                return Integer.parseInt(input);
            } catch (Exception e) {
                return defaultAmount;
            }
        } else {
            return defaultAmount;
        }

    }
}
