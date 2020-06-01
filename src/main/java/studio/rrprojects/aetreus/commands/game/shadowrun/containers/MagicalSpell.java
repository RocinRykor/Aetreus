package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class MagicalSpell {
    JsonObject jsonObject;
    int force;
    String category;
    String type;
    String target;
    String duration;
    String drain;
    String drain_damage;
    String description;
    String source;

    public MagicalSpell(JsonObject spell) {
        jsonObject = spell;
        force = spell.getInt("force", 1);
        category = spell.getString("category", "unknown");
        type = spell.getString("type", "unknown");
        target = spell.getString("target", "unknown");
        duration = spell.getString("duration", "unknown");
        drain = spell.getString("drain", "unknown");
        drain_damage = spell.getString("drain_damage", "unknown");
        description = spell.getString("description", "unknown");
        source = spell.getString("source", "unknown");
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDrain() {
        return drain;
    }

    public void setDrain(String drain) {
        this.drain = drain;
    }

    public String getDrain_damage() {
        return drain_damage;
    }

    public void setDrain_damage(String drain_damage) {
        this.drain_damage = drain_damage;
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
}
