package studio.rrprojects.aetreus.commands.game.shadowrun;

import net.dv8tion.jda.api.EmbedBuilder;
import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MyMessageBuilder;
import studio.rrprojects.aetreus.utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ShadowrunThirdRoller extends GameCommand {
    @Override
    public String getName() {
        return "sr3roll";
    }

    @Override
    public String getAlias() {
        return "sr3r";
    }

    @Override
    public String getHelpDescription() {
        return "Rolls dice in accordance with Shadowrun 3rd edition, including Rule of Ones and Rule of Six";
    }

    @Override
    public String getGame() {
        return "Shadowrun 3rd Edition";
    }

    @Override
    public void executeMain(CommandContainer cmd) {
        if (cmd.MAIN_ARG.equalsIgnoreCase("")) {
            RunError(cmd);
            return;
        }

        int dicePool;
        int targetValue = 4;

        if (isStringInt(cmd.MAIN_ARG)) {
            dicePool = Integer.parseInt(cmd.MAIN_ARG);
        } else {
            RunError(cmd);
            return;
        }
        String testType;
        if (cmd.SECONDARY_ARG == null) {
            testType = "default";
        } else {
            testType = getTestType(cmd.SECONDARY_ARG[0]);
        }

        if (testType.equalsIgnoreCase("digit")) {
            targetValue = Integer.parseInt(cmd.SECONDARY_ARG[0]);
            if (targetValue < 2) { targetValue = 2; } //Target Value cannot be less than 2
        }

        HashMap<String, Runnable> subcommands = new HashMap<>();
        int finalDicePool = dicePool;
        int finalTargetValue = targetValue;
        subcommands.put("digit", () -> SuccessTest(finalDicePool, finalTargetValue, cmd));
        subcommands.put("default", () -> SuccessTest(finalDicePool, finalTargetValue, cmd));
        subcommands.put("i", () -> RollInitiative(finalDicePool, cmd));
        subcommands.put("o", () -> OpenTest(finalDicePool, cmd));
        subcommands.put("error", () -> RunError(cmd));

        subcommands.get(testType).run();
    }

    // TESTS
    public void RollDicePackage(RollCharacter.DicePackageSR dicePackage, CommandContainer commandContainer) {
        SuccessTest(dicePackage.dicePool, dicePackage.totalTarget, commandContainer);
    }

    private void SuccessTest(int dicePool, int targetValue, CommandContainer cmd) {
        DiceContainer diceContainer = BuildDiceContainer(dicePool, true);

        checkRuleOfOne(diceContainer);
        CompareToTarget(diceContainer, targetValue);

        String output = ResultBuilder(diceContainer);
        Color color = CalculateColor(diceContainer);
        EmbedMessage(output, color, cmd);
        //SendMessage(output, cmd.DESTINATION);
    }

    private void OpenTest(int dicePool, CommandContainer cmd) {
        DiceContainer diceContainer = BuildDiceContainer(dicePool, true);

        checkRuleOfOne(diceContainer);
        FindHighest(diceContainer);

        String output = ResultBuilder(diceContainer);
        EmbedMessage(output, Color.BLUE, cmd);
    }

    private void RollInitiative(int dicePool, CommandContainer cmd) {
        DiceContainer diceContainer = BuildDiceContainer(dicePool, false);

        if (!CalculateInitiative(diceContainer, cmd)) { //BREAK if this fails
            return;
        }

        String output = ResultBuilder(diceContainer);
        EmbedMessage(output, Color.CYAN, cmd);
    }

    // Rollers
    private DiceContainer BuildDiceContainer(int dicePool, boolean usesRuleOfSix) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int ones = 0;

        for (int i = 0; i < dicePool; i++) {
            int tempValue = RandomUtils.getRandomRange(1, 6);

            if (tempValue == 1) {
                ones++;
            } else if (tempValue == 6 && usesRuleOfSix) {
                tempValue = RuleOfSixRoller();
            }

            arrayList.add(tempValue);

        }

        Collections.sort(arrayList);

        return new DiceContainer(arrayList, ones, false, new MyMessageBuilder(), 0); //DiceResults, Ones Count, is Rule of One (AKA Critical Glitch, Notes)
    }

    private int RuleOfSixRoller() {
        /*
        Rule of Six:
        If a die comes up as 6, reroll the die and add the new roll to the previous.
        Continue doing so until something other than a 6 is rolled
        Allows for success tests with a target greater than 6 to be possible
         */

        int startValue = 6;
        int tmpValue = 6;

        while (tmpValue == 6) {
            tmpValue = RandomUtils.getRandomRange(1, 6);
            startValue += tmpValue;
        }

        return startValue;
    }

    // UTILS
    private String getTestType(String s) {
      String input = s.toLowerCase();

      if (isStringInt(input)) {
          return "digit";
      } else if (input.startsWith("i")) {
          return "i";
      } else if (input.startsWith("o")) {
          return "o";
      } else { return "error"; }
    }

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void checkRuleOfOne(DiceContainer diceContainer) {
        if (diceContainer.DICE_RESULTS.size() == diceContainer.ONES) {
            diceContainer.RuleOfOnes = true;
        }
    }

    private void CompareToTarget(DiceContainer diceContainer, int targetValue) {
        int successes = 0;

        for (int i: diceContainer.DICE_RESULTS) {
            if (i >= targetValue) {
                successes++;
            }
        }

        diceContainer.SUCCESSES = successes;

        diceContainer.MESSAGE.add("> Target Value: " + targetValue);
        diceContainer.MESSAGE.addWithUnderLine("Number of Successes: " + successes);

        if (successes == 0) {
            diceContainer.MESSAGE.add("====== < FAILURE > ======");
        }
    }

    private void FindHighest(DiceContainer diceContainer) {
        ArrayList<Integer> tmp = diceContainer.DICE_RESULTS;

        int highestValue = tmp.get(tmp.size()-1);

        diceContainer.MESSAGE.addWithUnderLine("Open Test - Target Value: " + highestValue);
    }

    private boolean CalculateInitiative(DiceContainer diceContainer, CommandContainer cmd) {
        if (cmd.SECONDARY_ARG.length < 2) {
            RunError(cmd);
            return false;
        }

        int modValue;
        if (isStringInt(cmd.SECONDARY_ARG[1])) {
            modValue = Integer.parseInt(cmd.SECONDARY_ARG[1]);
        } else {
            RunError(cmd);
            return false;
        }

        int baseScore = 0;
        for (int i: diceContainer.DICE_RESULTS) {
            baseScore += i;
        }

        diceContainer.MESSAGE.add("> Total Dice Result: " + baseScore);
        diceContainer.MESSAGE.add("> Mod Value: " + modValue);
        diceContainer.MESSAGE.addBlankLine();
        diceContainer.MESSAGE.addWithUnderLine("Total Initiative: " + (baseScore + modValue));

        return true;
    }

    public void RollInit(int valueInit, int valueReactionTotal, CommandContainer commandContainer) {
        DiceContainer diceContainer = BuildDiceContainer(valueInit, false);

        int baseScore = 0;
        for (int i: diceContainer.DICE_RESULTS) {
            baseScore += i;
        }

        diceContainer.MESSAGE.add("> Total Dice Result: " + baseScore);
        diceContainer.MESSAGE.add("> Mod Value: " + valueReactionTotal);
        diceContainer.MESSAGE.addBlankLine();
        diceContainer.MESSAGE.addWithUnderLine("Total Initiative: " + (baseScore + valueReactionTotal));

        String output = ResultBuilder(diceContainer);
        EmbedMessage(output, Color.CYAN, commandContainer);
    }

    private String ResultBuilder(DiceContainer diceContainer) {
        /*
        String output = "Dice Pool: " + diceContainer.DICE_RESULTS.size() + "\n"
                + "======================\n\n"
                + "> BREAKDOWN \n"
                + "> Results: " + diceContainer.DICE_RESULTS.toString() + "\n\n"
                + diceContainer.NOTE;
                
         */
        diceContainer.MESSAGE.addWithUnderLineAt(0, "Dice Pool: " + diceContainer.DICE_RESULTS.size());
        diceContainer.MESSAGE.addBlankLineAt(2);
        diceContainer.MESSAGE.addAt(3, "> BREAKDOWN");
        diceContainer.MESSAGE.addAt(4, "> Results: " + diceContainer.DICE_RESULTS.toString());
        diceContainer.MESSAGE.addBlankLineAt(5);
        
        if (diceContainer.RuleOfOnes) {
            diceContainer.MESSAGE.add("< = = = WARNING - CRITICAL GLITCH! = = = >");
        }

        return diceContainer.MESSAGE.build(true);
    }

    private void EmbedMessage(String output, Color color, CommandContainer cmd) {
        EmbedBuilder build = new EmbedBuilder();
        build.setAuthor(cmd.AUTHOR.getName(), cmd.AUTHOR.getAvatarUrl(), cmd.AUTHOR.getEffectiveAvatarUrl());
        build.setColor(color);
        build.setDescription(output);

        cmd.DESTINATION.sendMessage(build.build()).queue();
    }

    private Color CalculateColor(DiceContainer diceContainer) {
        if (diceContainer.SUCCESSES == 0) {
            if (diceContainer.RuleOfOnes) {
                return Color.RED;
            }
            return Color.ORANGE;
        }

        //Sliding Green Scale Calculations
        float dicePool = diceContainer.DICE_RESULTS.size();
        float successes = diceContainer.SUCCESSES;
        float slidingGreenFloat = (successes/dicePool);
        //System.out.println(slidingGreenFloat);
        slidingGreenFloat *= 255;
        int slidingGreenValue = Math.round(slidingGreenFloat);
        if (slidingGreenValue < 0) { slidingGreenValue = 0; }
        if (slidingGreenValue > 255) { slidingGreenValue = 255; }

        //System.out.println(slidingGreenValue);
        return new Color(0, slidingGreenValue,0);
    }

    // ERROR MSGs
    private void RunError(CommandContainer cmd) {
        SendMessage("I'm sorry, there was an error", cmd.DESTINATION);
    }

    // Container
    private static class DiceContainer {
        ArrayList<Integer> DICE_RESULTS;
        int ONES;
        boolean RuleOfOnes;
        MyMessageBuilder MESSAGE;
        int SUCCESSES;

        public DiceContainer(ArrayList<Integer> DICE_RESULTS, int ONES, boolean RuleofOnes, MyMessageBuilder MESSAGE, int SUCCESSES) {
            this.DICE_RESULTS = DICE_RESULTS;
            this.ONES = ONES;
            this.RuleOfOnes = RuleofOnes;
            this.MESSAGE = MESSAGE;
            this.SUCCESSES = SUCCESSES;
        }
    }
}
