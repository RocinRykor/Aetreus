package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class SkillSpecializationContainer {
    JsonObject jsonObject;
    int level;

    public SkillSpecializationContainer(JsonObject specialization) {
        jsonObject = specialization;
        level = specialization.getInt("level", 0);
    }

    public JsonObject getJsonObject() {

        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
