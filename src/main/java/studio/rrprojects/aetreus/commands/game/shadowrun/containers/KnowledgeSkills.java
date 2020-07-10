package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeSkills {
    HashMap<String, KnowledgeSkillContainer> skillList = new HashMap<>();
    JsonObject jsonObject;

    public KnowledgeSkills(JsonObject knowledge) {
        jsonObject = knowledge;
        for (JsonObject.Member skill: knowledge) {
            skillList.put(skill.getName(), new KnowledgeSkillContainer(skill.getValue().asObject()));
        }
    }

    public HashMap<String, KnowledgeSkillContainer> getSkillList() {
        return skillList;
    }

    public void setSkillList(HashMap<String, KnowledgeSkillContainer> skillList) {
        this.skillList = skillList;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getAllSkills() {
        System.out.println("Getting all Knowledge Skills!");
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, KnowledgeSkillContainer> skill: skillList.entrySet()) {
            StringBuilder tmpString = new StringBuilder(String.format("%s: %d", skill.getKey(), skill.getValue().level));
            if (skill.getValue().specializationList.size() > 0) {
                for (Map.Entry<String, SkillSpecializationContainer> spec: skill.getValue().specializationList.entrySet()) {
                    tmpString.append(String.format(", %s: %d", spec.getKey(), spec.getValue().level));
                }
            }
            output.append(tmpString.append("\n"));
        }
        return String.valueOf(output);
    }

    public int getSkillByName(String searchTerm) {
        if (skillList.containsKey(searchTerm)) {
            return skillList.get(searchTerm).level;
        }

        //Assuming the initial search failed - perform a more detailed search
        System.out.println(String.format("KnowledgeSkills.java: Search of: %s failed! - Searching Manually!", searchTerm));
        for (Map.Entry<String, KnowledgeSkillContainer> skill : skillList.entrySet()) {
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return skill.getValue().getLevel();
            }
        }

        //If both failed return Error and 0
        System.out.println(String.format("ERROR UNABLE TO FIND ATTRIBUTE %s", searchTerm));
        return 0;
    }

    public Object getSkill(String searchTerm) {
        //Look at general Skill
        for (Map.Entry<String, KnowledgeSkillContainer> skill : skillList.entrySet()) {
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return new KnowledgeSkillMember(skill.getKey(), skill.getValue());
            }
        }
        return null;
    }

    public boolean containsSkill(String searchTerm) {
        for (Map.Entry<String, KnowledgeSkillContainer> skill : skillList.entrySet()) {
            if (skill.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
