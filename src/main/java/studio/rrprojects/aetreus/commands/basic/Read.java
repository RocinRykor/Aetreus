package studio.rrprojects.aetreus.commands.basic;

import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Read extends BasicCommand {
    @Override
    public String getName() {
        return "Read";
    }

    @Override
    public String getAlias() {
        return "read";
    }

    @Override
    public String getHelpDescription() {
        return "Uses the TTS Engine to read a predefined text file - More of proof of concept than actual command";
    }

    Audio audio = new Audio();

    @Override
    public void executeMain(CommandContainer cmd) {

        // Load the File
        String fileName = Main.getDirMainDir() + File.separator + "Read.txt";

        String contents = "";
        try {
            contents = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        audio.SpeakString(contents, cmd);
    }
}
