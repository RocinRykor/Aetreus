package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Character {
    JsonObject jsonObject;
    String name;
    String race;
    String sex;
    String description;
    String notes;
    int age;
    int karma;
    Career career;

    public Character(JsonObject character) {
        jsonObject = character;
        name = character.getString("name", "unknown");
        race = character.getString("race", "unknown");
        sex = character.getString("sex", "unknown");
        description = character.getString("description", "unknown");
        notes = character.getString("notes", "unknown");
        age = character.getInt("age", 0);
        karma = character.getInt("karma", 0);
        career = new Career(character.get("career").asObject());
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }
}
