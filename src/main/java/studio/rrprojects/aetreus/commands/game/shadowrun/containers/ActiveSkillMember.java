package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

public class ActiveSkillMember {
    String name;
    ActiveSkillContainer container;

    public ActiveSkillMember(String key, ActiveSkillContainer value) {
        name = key;
        container = value;
    }

    public String getName() {
        return name;
    }

    public ActiveSkillContainer getContainer() {
        return container;
    }
}
