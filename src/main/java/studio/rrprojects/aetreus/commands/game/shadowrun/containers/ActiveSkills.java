package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ActiveSkills {
    HashMap<String, ActiveSkillContainer> skillList = new HashMap<>();
    JsonObject jsonObject;

    public ActiveSkills(JsonObject active) {
        jsonObject = active;
        for (JsonObject.Member skill: active) {
            skillList.put(skill.getName(), new ActiveSkillContainer(skill.getValue().asObject()));
        }

    }

    public HashMap<String, ActiveSkillContainer> getSkillList() {
        return skillList;
    }

    public void setSkillList(HashMap<String, ActiveSkillContainer> skillList) {
        this.skillList = skillList;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getAllSkills() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, ActiveSkillContainer> skill : skillList.entrySet()) {
            StringBuilder tmpString = new StringBuilder(String.format("%s: %d", skill.getKey(), skill.getValue().level));
            if (skill.getValue().specializationList.size() > 0) {
                for (Map.Entry<String, SkillSpecializationContainer> spec:
                skill.getValue().specializationList.entrySet()) {
                    tmpString.append(String.format(", %s: %d", spec.getKey(), spec.getValue().level));
                }
            }
            output.append(tmpString.append("\n"));
        }
        return String.valueOf(output);
    }
}

/*
JsonObject value = skill.getValue().asObject();
            String name = skill.getName();

 */