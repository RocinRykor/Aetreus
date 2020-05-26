package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class ModificationContainer {
    JsonObject jsonObject;
    String effect;
    public ModificationContainer(JsonObject modification) {
        jsonObject = modification;
        effect = modification.getString("effect", "unknown");
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
