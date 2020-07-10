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

    public int getSkillByName(String searchTerm) {
        if (skillList.containsKey(searchTerm)) {
            System.out.println("Level = " + skillList.get(searchTerm).level);
            return skillList.get(searchTerm).level;
        }

        //Assuming the initial search failed - perform a more detailed search
        System.out.println(String.format("ActiveSkills.java: Search of: %s failed! - Searching Manually!", searchTerm));
        for (Map.Entry<String, ActiveSkillContainer> skill : skillList.entrySet()) {
            //Look at the skill itself
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return skill.getValue().getLevel();
            }
            //Look at each Specialization for the skill
            JsonObject specializations = skill.getValue().jsonObject.get("specializations").asObject();
            for (JsonObject.Member spec:specializations) {
                if (spec.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return spec.getValue().asObject().getInt("level", 0);
                }
            }
        }

        //If both failed return Error and 0
        System.out.println(String.format("ERROR UNABLE TO FIND ATTRIBUTE %s", searchTerm));
        return 0;
    }

    public boolean containsSkill(String searchTerm) {
        for (Map.Entry<String, ActiveSkillContainer> skill : skillList.entrySet()) {
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Object getSkill(String searchTerm) {
        //Look at general Skill
        for (Map.Entry<String, ActiveSkillContainer> skill : skillList.entrySet()) {
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return new ActiveSkillMember(skill.getKey(), skill.getValue());
            }
            /*
            //Look at each Specialization for the skill
            JsonObject specializations = skill.getValue().jsonObject.get("specializations").asObject();
            for (JsonObject.Member spec:specializations) {
                if (spec.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return new ActiveSkillMember(spec.getName(), spec.getValue().asObject());
                }
            }
             */
        }

        //If both failed return Error and Null
        System.out.println(String.format("ERROR UNABLE TO FIND ATTRIBUTE %s", searchTerm));
        return null;
    }
}

/*
JsonObject value = skill.getValue().asObject();
            String name = skill.getName();

 */