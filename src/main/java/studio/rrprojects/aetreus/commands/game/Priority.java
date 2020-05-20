package studio.rrprojects.aetreus.commands.game;

import studio.rrprojects.aetreus.discord.CommandContainer;
import studio.rrprojects.aetreus.utils.MyMessageBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Priority extends GameCommand {
    @Override
    public String getName() {
        return "Priority";
    }

    @Override
    public String getAlias() {
        return "pri";
    }

    @Override
    public String getHelpDescription() {
        return "Tool for the SR3 Priority Table, used when making any new Shadowrun Characters";
    }

    @Override
    public String getGame() {
        return "ShadowRun 3rd Edition";
    }

    HashMap<String, String> tableRace;
    HashMap<String, String> tableMagic;
    HashMap<String, String> tableAttributes;
    HashMap<String, String> tableSkills;
    HashMap<String, String> tableResources;

    @Override
    public void executeMain(CommandContainer cmd) {
        PriorityTableContainer priority = new PriorityTableContainer();

        priority.getRace(tableRace);
        priority.getMagic(tableMagic);
        priority.getAttributes(tableAttributes);
        priority.getSkills(tableSkills);
        priority.getResources(tableResources);

        SendMessage(priority.Build(new MyMessageBuilder()), cmd.DESTINATION);
    }

    public void Initialize() {
        //Race Table
        tableRace = new HashMap<>();
        tableRace.put("Troll", "C");
        tableRace.put("Elf", "C");
        tableRace.put("Dwarf", "D");
        tableRace.put("Ork", "D");
        tableRace.put("Human", "E");

        //Magic Table
        tableMagic = new HashMap<>();
        tableMagic.put("Full Magician", "A");
        tableMagic.put("Aspected Magician", "B");
        tableMagic.put("Adept", "B");
        tableMagic.put("Not Magic", "E");

        //Attribute Table
        tableAttributes = new HashMap<>();
        tableAttributes.put("A", "30");
        tableAttributes.put("B", "27");
        tableAttributes.put("C", "24");
        tableAttributes.put("D", "21");
        tableAttributes.put("E", "18");

        //Skill Table
        tableSkills = new HashMap<>();
        tableSkills.put("A", "50");
        tableSkills.put("B", "40");
        tableSkills.put("C", "34");
        tableSkills.put("D", "30");
        tableSkills.put("E", "27");

        //Resources Table
        tableResources = new HashMap<>();
        tableResources.put("A", "1,000,000¥");
        tableResources.put("B", "400,000¥");
        tableResources.put("C", "90,000¥");
        tableResources.put("D", "20,000¥");
        tableResources.put("E", "5,000¥");
    }

    private static class PriorityTableContainer {
        ArrayList<String> priorityPool = new ArrayList<>();
        HashMap<String, String> optionTable = new HashMap<>();
        HashMap<String, String> valueTable = new HashMap<>();

        PriorityTableContainer(){
            priorityPool.add("A");
            priorityPool.add("B");
            priorityPool.add("C");
            priorityPool.add("D");
            priorityPool.add("E");

            optionTable.put("Race", "");
            optionTable.put("Magic", "");
            optionTable.put("Attributes", "");
            optionTable.put("Skills", "");
            optionTable.put("Resources", "");

            valueTable.put("Race", "");
            valueTable.put("Magic", "");
            valueTable.put("Attributes", "");
            valueTable.put("Skills", "");
            valueTable.put("Resources", "");
        }

        public void getRace(HashMap<String, String> raceMap) {
            ArrayList<String> raceArray = new ArrayList<>();
            raceArray.add("Human");
            raceArray.add("Dwarf");
            raceArray.add("Ork");
            raceArray.add("Elf");
            raceArray.add("Troll");
            Collections.shuffle(raceArray);

            String selectedValue = raceArray.get(0);
            String selectedOption = raceMap.get(selectedValue);

            valueTable.replace("Race", selectedValue);
            optionTable.replace("Race", selectedOption);


            priorityPool.remove(selectedOption);
        }

        public void getMagic(HashMap<String, String> magicMap) {
            ArrayList<String> magicArray = new ArrayList<>();
            magicArray.add("Not Magic");
            magicArray.add("Adept");
            magicArray.add("Aspected Magician");
            magicArray.add("Full Magician");
            Collections.shuffle(magicArray);

            String selectedValue = magicArray.get(0);
            String selectedOption = magicMap.get(selectedValue);

            if (!priorityPool.contains(selectedOption)) {
                while (!priorityPool.contains(selectedOption)){
                    selectedValue = magicArray.get(0);
                    selectedOption = magicMap.get(selectedValue);
                }
            }

            optionTable.replace("Magic", selectedOption);
            valueTable.replace("Magic", selectedValue);

            priorityPool.remove(selectedOption);
        }

        public void getAttributes(HashMap<String, String> attributesMap) {
            Collections.shuffle(priorityPool);

            String selectedOption = priorityPool.get(0);
            String selectedValue = attributesMap.get(selectedOption);

            optionTable.replace("Attributes", selectedOption);
            valueTable.replace("Attributes", selectedValue);

            priorityPool.remove(0);
        }

        public void getSkills(HashMap<String, String> skillsMap) {
            Collections.shuffle(priorityPool);

            String selectedOption = priorityPool.get(0);
            String selectedValue = skillsMap.get(selectedOption);

            optionTable.replace("Skills", selectedOption);
            valueTable.replace("Skills", selectedValue);

            priorityPool.remove(0);
        }

        public void getResources(HashMap<String, String> resourcesMap) {
            Collections.shuffle(priorityPool);

            String selectedOption = priorityPool.get(0);
            String selectedValue = resourcesMap.get(selectedOption);

            optionTable.replace("Resources", selectedOption);
            valueTable.replace("Resources", selectedValue);

            priorityPool.remove(0);
        }

        public String Build(MyMessageBuilder myMessageBuilder) {
            for (Map.Entry <String, String> value: valueTable.entrySet())
                myMessageBuilder.add(String.format("%s: %s (%s)", value.getKey(), value.getValue(), optionTable.get(value.getKey())));

            return myMessageBuilder.build(false);
        }
    }
}
