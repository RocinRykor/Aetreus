package studio.rrprojects.aetreus.discord;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import studio.rrprojects.aetreus.commands.CommandController;
import studio.rrprojects.aetreus.commands.game.NoteRecorder;
import studio.rrprojects.aetreus.config.ConfigController;
import studio.rrprojects.aetreus.main.Main;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class BotListener extends ListenerAdapter {
    CommandController command;
    HashMap<String, String> shortcutPrefixTable;
    NoteRecorder globalListener;

    public void Initialize() {
        command = Main.getCommand();
        InitPrefixTable();
    }

    private void InitPrefixTable() {
        //This Table will allow for shortcuts
        shortcutPrefixTable = new HashMap<>();
        shortcutPrefixTable.put("%", "&character roll ");
        shortcutPrefixTable.put("_", "&character money ");
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
            ProcessGlobalListener(event); //Check for Notes
            //Check if message contains a curly brace code
            if (CheckForCurlyBraces(processedInput)) {
                ProcessCurlyBrackets(processedInput, event);
            }
            return;
        }

        //System.out.println("InputBeheaded = " + inputBeheaded);
        command.ProcessInput(inputBeheaded, event);
    }

    private void ProcessCurlyBrackets(String input, MessageReceivedEvent event) {
        //Remove the curly bracket segment from the code and try running it as a command.
        int firstPos = input.indexOf("{") + 1; //Adds one so that it clips off the bracket
        int secondPos = input.indexOf("}");

        if (secondPos < firstPos) {
            return;
        }

        String substring = input.substring(firstPos, secondPos);

        command.ProcessInput(substring, event, "rolling");
    }

    private boolean CheckForCurlyBraces(String input) {
        return input.contains("{") && input.contains("}");
    }

    private void ProcessGlobalListener(MessageReceivedEvent event) {
        if (globalListener != null) {
            globalListener.ProcessEvent(event);
        }
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

    public void AddGlobalListener(NoteRecorder listener) {
        globalListener = listener;
    }

    public void RemoveGlobalListener() {
        globalListener = null;
    }
}
