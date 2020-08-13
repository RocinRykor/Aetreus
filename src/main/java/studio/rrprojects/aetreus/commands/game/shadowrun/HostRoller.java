package studio.rrprojects.aetreus.commands.game.shadowrun;

import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HostRoller extends GameCommand {
    @Override
    public String getName() {
        return "Host";
    }

    @Override
    public String getAlias() {
        return "Host";
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    @Override
    public String getGame() {
        return "Shadowrun 3rd Edition";
    }

    HashMap<String, String> diffLevels;
    HashMap<String, RatingContainer> tableHost;

    @Override
    public void executeInitial(CommandContainer cmd) {
        super.executeInitial(cmd);
    }

    @Override
    public void Initialize() {
        super.Initialize();
        tableHost = new HashMap<>();
        tableHost.put("Easy", new RatingContainer(1, 3, 1, 3, 7));
        tableHost.put("Medium", new RatingContainer(1, 6, 2, 3, 9));
        tableHost.put("Hard", new RatingContainer(2, 6, 1, 6, 12));
        tableHost.put("Unbreakable", new RatingContainer(2, 9, 2, 6, 15));

        diffLevels = new HashMap<>();
        diffLevels.put("easy", "Easy");
        diffLevels.put("low", "Easy");
        diffLevels.put("simple", "Easy");
        diffLevels.put("light", "Easy");
        diffLevels.put("soft", "Easy");
        diffLevels.put("medium", "Medium");
        diffLevels.put("med", "Medium");
        diffLevels.put("avg", "Medium");
        diffLevels.put("average", "Medium");
        diffLevels.put("moderate", "Medium");
        diffLevels.put("firm", "Medium");
        diffLevels.put("mod", "Medium");
        diffLevels.put("hard", "Hard");
        diffLevels.put("heavy", "Hard");
        diffLevels.put("strong", "Hard");
        diffLevels.put("brutal", "Hard");
        diffLevels.put("knox", "Unbreakable");
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        super.executeMain(cmd);
        String targetSearch = "easy";
        if (cmd.MAIN_ARG != null) {
            targetSearch = cmd.MAIN_ARG.toLowerCase();
        }

        String targetKey;
        targetKey = diffLevels.getOrDefault(targetSearch, "Easy");

        String message = "Generating New Host: " + targetKey + "\n" +
                "";
        message += GenerateHost(targetKey);
        SendMessage(message,cmd.DESTINATION);
    }

    private String GenerateHost(String target) {
        RatingContainer container = tableHost.get(target);
        int securityRating = 0;
        ArrayList<Integer> systemRatings = new ArrayList<>();

        for (int i = 0; i < container.securityDicePool; i++) {
            securityRating += RandomUtils.getRandomRange(1,3);
        }
        securityRating += container.securityMod;

        for (int i = 0; i < 5; i++) {
            int systemValue = 0;
            for (int j = 0; j < container.systemDicePool; j++) {
                systemValue += RandomUtils.getRandomRange(1, container.systemDiceValue);
            }
            systemValue += container.systemMod;
            systemRatings.add(systemValue);
        }

        return String.format("System: %d/%d/%d/%d/%d/%d", securityRating, systemRatings.get(0), systemRatings.get(1), systemRatings.get(2), systemRatings.get(3), systemRatings.get(4));
    }

    private static class RatingContainer {
        int securityDicePool;
        int securityMod;
        int systemDicePool;
        int systemDiceValue;
        int systemMod;

        public RatingContainer(int securityDicePool, int securityMod, int systemDicePool, int systemDiceValue, int systemMod) {
            this.securityDicePool = securityDicePool;
            this.securityMod = securityMod;
            this.systemDicePool = systemDicePool;
            this.systemDiceValue = systemDiceValue;
            this.systemMod = systemMod;
        }
    }
}
