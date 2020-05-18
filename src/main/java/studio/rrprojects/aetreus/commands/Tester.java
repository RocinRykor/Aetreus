package studio.rrprojects.aetreus.commands;

import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MessageUtils;

import java.util.ArrayList;

public class Tester extends BasicCommand {
    @Override
    public String getName() {
        return "Tester";
    }

    @Override
    public String getAlias() {
        return "test";
    }

    @Override
    public String getHelpDescription() {
        return "This is what I use to test any new function. If you run it I cannot promise it will actually do anything noticeable";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
    }
}
