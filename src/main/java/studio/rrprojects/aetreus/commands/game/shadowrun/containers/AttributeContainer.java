package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class AttributeContainer {
    JsonObject jsonObject;
    int base;
    int override;
    int modifier;

    public AttributeContainer(JsonObject attribute) {
        jsonObject = attribute;
        base = attribute.getInt("base", 0);
        override = attribute.getInt("override", 0);
        modifier = attribute.getInt("modifier", 0);
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getOverride() {
        return override;
    }

    public void setOverride(int override) {
        this.override = override;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
}
