package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class KnowledgeSkillContainer {
    JsonObject jsonObject;
    String category;
    String source;
    int level;
    HashMap<String, SkillSpecializationContainer> specializationList;

    public KnowledgeSkillContainer(JsonObject skill) {
        jsonObject = skill;
        category = skill.getString("category", "unknown");
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
