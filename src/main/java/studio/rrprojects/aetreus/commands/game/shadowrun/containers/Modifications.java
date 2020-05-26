package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class Modifications {
    JsonObject jsonObject;
    HashMap<String, ModificationContainer> modificationList;

    public Modifications(JsonObject modifications) {
        jsonObject = modifications;
        modificationList = new HashMap<>();
        for (JsonObject.Member modification : modifications) {
            modificationList.put(modification.getName(), new ModificationContainer(modification.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, ModificationContainer> getModificationList() {
        return modificationList;
    }

    public void setModificationList(HashMap<String, ModificationContainer> modificationList) {
        this.modificationList = modificationList;
    }
}
