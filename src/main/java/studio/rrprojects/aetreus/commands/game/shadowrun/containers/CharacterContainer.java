package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import java.io.*;

public class CharacterContainer {
    public JsonObject mainObj;

    Character character;
    Attributes attributes;
    Skills skills;
    Inventory inventory;
    JsonObject spells;
    ConditionContainer condition;
    Contacts contacts;


    public CharacterContainer BuildCharacter(String filePath) throws IOException {
        FileReader reader = new FileReader(new File((filePath)));
        mainObj = (JsonObject) Json.parse(reader);

        character = new Character(mainObj.get("character").asObject(), this);
        attributes = new Attributes(mainObj.get("attributes").asObject(), this);
        skills = new Skills(mainObj.get("skills").asObject(), this);
        inventory = new Inventory(mainObj.get("inventory").asObject(), this);
        spells = mainObj.get("spells").asObject();
        condition = new ConditionContainer(mainObj.get("condition").asObject(), this);
        contacts = new Contacts(mainObj.get("contacts").asObject(), this);

        return this;
    }


    public JsonObject getMainObj() {
        return mainObj;
    }

    public void setMainObj(JsonObject mainObj) {
        this.mainObj = mainObj;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public JsonObject getSpells() {
        return spells;
    }

    public void setSpells(JsonObject spells) {
        this.spells = spells;
    }

    public ConditionContainer getCondition() {
        return condition;
    }

    public void setCondition(ConditionContainer condition) {
        this.condition = condition;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void WriteTo(String filePath) {
        FileWriter writer;
        try {
            writer = new FileWriter(new File(filePath));
            mainObj.writeTo(writer, WriterConfig.PRETTY_PRINT);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
