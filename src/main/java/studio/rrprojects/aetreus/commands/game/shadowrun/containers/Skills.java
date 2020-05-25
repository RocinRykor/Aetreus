package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Skills {
    JsonObject jsonObject;
    ActiveSkills activeSkills;
    KnowledgeSkills knowledgeSkills;

    public Skills(JsonObject skills) {
        jsonObject = skills;
        activeSkills = new ActiveSkills(skills.get("active").asObject());
        knowledgeSkills = new KnowledgeSkills(skills.get("knowledge").asObject());
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ActiveSkills getActiveSkills() {
        return activeSkills;
    }

    public void setActiveSkills(ActiveSkills activeSkills) {
        this.activeSkills = activeSkills;
    }

    public KnowledgeSkills getKnowledgeSkills() {
        return knowledgeSkills;
    }

    public void setKnowledgeSkills(KnowledgeSkills knowledgeSkills) {
        this.knowledgeSkills = knowledgeSkills;
    }
}
