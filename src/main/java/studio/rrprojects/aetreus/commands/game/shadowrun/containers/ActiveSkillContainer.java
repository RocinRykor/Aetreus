package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class ActiveSkillContainer {

    JsonObject jsonObject;
    String attribute;
    boolean build_repair;
    String category;
    String defaults;
    String description;
    String source;
    int level;
    HashMap<String, SkillSpecializationContainer> specializationList;

    public ActiveSkillContainer(JsonObject skill) {
        jsonObject = skill;
        attribute = skill.getString("attribute", "unknown");
        build_repair = skill.getBoolean("build_repair", false);
        category = skill.getString("category", "unknown");
        defaults = skill.getString("defaults", "unknown");
        description = skill.getString("description", "unknown");
        source = skill.getString("source", "unknown");
        level = skill.getInt("level", 0);
        JsonObject specializations = skill.get("specializations").asObject();
        specializationList = new HashMap<>();
        for (JsonObject.Member specialization: specializations) {
            specializationList.put(specialization.getName(), new SkillSpecializationContainer(specialization.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean isBuild_repair() {
        return build_repair;
    }

    public void setBuild_repair(boolean build_repair) {
        this.build_repair = build_repair;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public HashMap<String, SkillSpecializationContainer> getSpecializationList() {
        return specializationList;
    }

    public void setSpecializationList(HashMap<String, SkillSpecializationContainer> specializationList) {
        this.specializationList = specializationList;
    }
}
