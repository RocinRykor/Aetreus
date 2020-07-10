package studio.rrprojects.aetreus.discord;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import studio.rrprojects.aetreus.commands.CommandController;
import studio.rrprojects.aetreus.config.ConfigController;
import studio.rrprojects.aetreus.main.Main;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class BotListener extends ListenerAdapter {
    CommandController command;
    HashMap<String, String> shortcutPrefixTable;

    public void Initialize() {
        command = Main.getCommand();
        InitPrefixTable();
    }

    private void InitPrefixTable() {
        //This Table will allow for shortcuts
        shortcutPrefixTable = new HashMap<>();
        shortcutPrefixTable.put("%", "&character roll ");
        shortcutPrefixTable.put("$", "&character money ");
        shortcutPrefixTable.put(">", "&character load ");
    }


    // EVENTS
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String inputRaw = event.getMessage().getContentRaw();
        boolean isBot = event.getAuthor().isBot();

        String processedInput;
        try {
            processedInput = processShortcutPrefix(inputRaw);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        //System.out.println("processedInput = " + processedInput);
        String inputPrefix = CheckForPrefix(processedInput);
        String inputBeheaded;

        if (inputPrefix != null && !isBot) {
            inputBeheaded = processedInput.replaceFirst(inputPrefix, "");
        } else {
            return;
        }

        //System.out.println("InputBeheaded = " + inputBeheaded);
        command.ProcessInput(inputBeheaded, event);
    }

    private String processShortcutPrefix(String input) {
        if (input.length() == 0) {
          return input;
        }

        String key = String.valueOf(input.charAt(0));

        String output;
        if (shortcutPrefixTable.containsKey(key)) {

            String value = shortcutPrefixTable.get(key);
            output = input.replaceAll(key, value);
        } else {
            output = input;
        }

        //System.out.println("Input: " + input);
        //System.out.println("Output: " + output);
        return output;
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        super.onReady(event);
        Main.Ready();
    }

    // UTILS
    private String CheckForPrefix(String inputRaw) {
        ConfigController config = Main.getConfig();
        if (inputRaw.startsWith(config.getBotPrefix())) {
            return config.getBotPrefix();
        } else if (inputRaw.startsWith(config.getBotSecondPrefix())) {
            return config.getBotSecondPrefix();
        } else {
            return null;
        }
    }
}
