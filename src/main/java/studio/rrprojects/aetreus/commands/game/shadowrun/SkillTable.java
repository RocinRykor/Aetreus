package studio.rrprojects.aetreus.commands.game.shadowrun;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import studio.rrprojects.aetreus.main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SkillTable {
    public static ArrayList<String> tableAttributes;

    public static void InitTables() {
        //Attribute Table
        tableAttributes = new ArrayList<>();
        PopulateTable(tableAttributes, "SR3E_attributes.json");
        //Skills Table
        //Weapons Table
        //Knowledge Skills
    }

    private static void PopulateTable(ArrayList<String> outputTable, String sourceFile) {
        String filePath = Main.getDirMainDir() + File.separator + "Shadowrun" + File.separator + sourceFile;

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
