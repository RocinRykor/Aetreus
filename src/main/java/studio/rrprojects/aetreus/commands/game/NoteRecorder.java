package studio.rrprojects.aetreus.commands.game;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import studio.rrprojects.aetreus.commands.game.shadowrun.GameCommand;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NoteRecorder extends GameCommand {
    private File runningStatus;

    @Override
    public String getName() {
        return "Note";
    }

    @Override
    public String getAlias() {
        return "Note";
    }

    @Override
    public String getHelpDescription() {
        return "When ran, starts collecting notes for the session";
    }

    @Override
    public String getGame() {
        return "ShadowRun 3rd Edition";
    }

    String baseDir = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "Notes";
    ArrayList<MessageReceivedEvent> messageList;
    ArrayList<String> entryList;

    @Override
    public void executeMain(CommandContainer cmd) {
        super.executeMain(cmd);

        String input = cmd.MAIN_ARG;

        HashMap<String, Runnable> subcommands = new HashMap<>();
        subcommands.put("start", ()-> StartNote(cmd));
        subcommands.put("stop", ()-> StopNote(cmd));
        //subcommands.put("status", ()-> NoteStatus(cmd));
        //subcommands.put("add", ()-> AddNote(cmd));
        //subcommands.put("recap", ()-> RecapNote(cmd));
        //subcommands.put("List", ()-> NoteStatus(cmd));

        if (subcommands.containsKey(input)) {
            subcommands.get(input).run();
        }

    }

    private void StopNote(CommandContainer cmd) {
        if (runningStatus == null) {
            SendMessage("I'm sorry looks like there is not an active note being recorded. \n" +
                    "Please start an active note with \"&note start [Title]-[In-Game Date]\"", cmd.DESTINATION);
            return;
        }
        
        String outputString = "";
        for (String entry: entryList) {
            outputString += entry + "\n";
        }

        System.out.println(outputString);

        FileWriter writer = null;
        try {
            writer = new FileWriter(runningStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert writer != null;
            writer.append(outputString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        runningStatus = null;
        Main.getListener().RemoveGlobalListener();
    }

    private void StartNote(CommandContainer cmd) {
        if (runningStatus != null) {
            SendMessage("I'm sorry looks like there is already an active note being recorded. \n" +
                    "Please stop the active note with \"&note stop\", before starting a new one", cmd.DESTINATION);
            return;
        }

        messageList = new ArrayList<>();
        entryList = new ArrayList<>();

        InformationContainer noteInfo = new InformationContainer(cmd.SECONDARY_ARG, cmd);

        if (noteInfo.title == null || noteInfo.dateFinal == null) {
            return;
        }

        String currentDir = baseDir + File.separator + noteInfo.title;
        System.out.println("Current Directory: " + currentDir);
        File dir = new File(currentDir);
        if (!dir.exists()) {
            System.out.println("DOES Not EXIST!");
            boolean mkdirs = dir.mkdirs();
        }

        String notePath = currentDir + File.separator + noteInfo.dateFinal;
        File noteFile = new File(notePath);
        if (!noteFile.exists()) {
            try {
                boolean newFile = noteFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        runningStatus = noteFile;
        Main.getListener().AddGlobalListener(this);
    }

    public void ProcessEvent(MessageReceivedEvent event) {
        if (ValidChannel(event) || ValidPrefix(event)) {
            AddEntry(event.getMessage().getContentRaw());
        }

    }

    private void AddEntry(String string) {
        String input = string;
        String output;
        Boolean isNote = false;

        if (input.startsWith("-note")) {
          input = input.replaceFirst("-note ", "");
          isNote = true;
        }

        if (isNote) {
            output = "[" + input +"]";
        } else {
            output = input;
        }

        entryList.add(output);
    }

    private boolean ValidPrefix(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().toLowerCase().startsWith("-note");
    }

    private boolean ValidChannel(MessageReceivedEvent event) {
        return event.getChannel().getName().equalsIgnoreCase("BotTesting") || event.getChannel().getName().equalsIgnoreCase("Tabletop");
    }

    private class InformationContainer {
        String title;
        String dateFinal;
        public InformationContainer(String[] rawInput, CommandContainer cmd) {
            String input = ArrayToString(rawInput);

            Date date;
            if (input.contains("-")) {
                String[] split = input.split("-");
                title = split[0];
                String dateInput = split[1];
                try {
                    date = new SimpleDateFormat("MM/dd/yyyy").parse(dateInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                    SendMessage("I'm sorry I didn't understand that date please use the \"dd/MM/yyyy\" format for the date", cmd.DESTINATION);
                    return;
                }

                DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
                dateFinal = formatter.format(date);
                System.out.println(dateFinal);
            } else {
                SendMessage("I'm sorry I didn't understand that, please format your message as: \n" +
                        "\"&note start [Title]-[In-Game Date]\"", cmd.DESTINATION);
            }

            System.out.println(input);
        }

        private String ArrayToString(String[] input) {
            StringBuilder output = new StringBuilder(input[0]);

            for (int i = 1; i < input.length; i++) {
                output.append(" ").append(input[i]);
            }

            return String.valueOf(output);
        }
    }
}
