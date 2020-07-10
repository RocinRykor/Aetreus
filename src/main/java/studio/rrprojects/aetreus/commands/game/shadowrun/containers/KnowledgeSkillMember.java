package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

public class KnowledgeSkillMember {
    String name;
    KnowledgeSkillContainer container;

    public KnowledgeSkillMember(String key, KnowledgeSkillContainer value) {
        name = key;
        container = value;
    }

    public String getName() {
        return name;
    }

    public KnowledgeSkillContainer getContainer() {
        return container;
    }
}
