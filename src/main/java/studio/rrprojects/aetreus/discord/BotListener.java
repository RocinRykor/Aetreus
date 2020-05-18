package studio.rrprojects.aetreus.discord;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import studio.rrprojects.aetreus.commands.CommandController;
import studio.rrprojects.aetreus.config.ConfigController;
import studio.rrprojects.aetreus.main.Main;

import javax.annotation.Nonnull;

public class BotListener extends ListenerAdapter {
    CommandController command;

    public void Initialize() {
        command = Main.getCommand();
    }


    // EVENTS
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        String inputRaw = event.getMessage().getContentRaw();
        boolean isBot = event.getAuthor().isBot();
        String inputPrefix = CheckForPrefix(inputRaw);
        String inputBeheaded;

        if (inputPrefix != null && !isBot) {
            inputBeheaded = inputRaw.replaceFirst(inputPrefix, "");
        } else {
            return;
        }

        command.ProcessInput(inputBeheaded, event);
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
