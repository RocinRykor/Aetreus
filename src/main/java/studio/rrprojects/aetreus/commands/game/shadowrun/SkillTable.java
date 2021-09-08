package studio.rrprojects.aetreus.commands.game.shadowrun;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import studio.rrprojects.aetreus.main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SkillTable {
    public static ArrayList<String> tableAttributes;
    public static ArrayList<String> tableActiveSkills;
    public static ArrayList<String> tableKnowledgeSkills;

    public static void InitTables() {
        //Attribute Table
        tableAttributes = new ArrayList<>();
        PopulateTable(tableAttributes, "SR3E_attributes.json");
        //Skills Table
        tableActiveSkills = new ArrayList<>();
        PopulateTableWithSpecializations(tableActiveSkills, "SR3E_active_skills.json");
        tableKnowledgeSkills = new ArrayList<>();
        PopulateTable(tableKnowledgeSkills, "SR3E_knowledge_skills.json");
        //Weapons Table
        //Knowledge Skills
    }

    private static void PopulateTableWithSpecializations(ArrayList<String> outputTable, String sourceFile) {
        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "JSON_Tables" + File.separator + sourceFile;

        FileReader reader = null;
        try {
            reader = new FileReader(new File((filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonObject mainObj = null;
        try {
            mainObj = (JsonObject) Json.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (JsonObject.Member member: mainObj) {
            outputTable.add(member.getName());
            String specializations = member.getValue().asObject().getString("specialization", "NONE");
            String[] breakdown = specializations.split(",");
            outputTable.addAll(Arrays.asList(breakdown));
        }
    }

    private static void PopulateTable(ArrayList<String> outputTable, String sourceFile) {
        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + "JSON_Tables" + File.separator + sourceFile;

        FileReader reader = null;
        try {
            reader = new FileReader(new File((filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonObject mainObj = null;
        try {
            mainObj = (JsonObject) Json.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (JsonObject.Member member: mainObj) {
            outputTable.add(member.getName());
        }
    }
}
