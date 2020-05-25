package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class WeaponModifications {
    JsonObject jsonObject;
    HashMap<String, WeaponModificationContainer> modificationList;
    public WeaponModifications(JsonObject modifications) {
        jsonObject = modifications;
        modificationList = new HashMap<>();
        for (JsonObject.Member modification : modifications) {
            modificationList.put(modification.getName(), new WeaponModificationContainer(modification.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, WeaponModificationContainer> getModificationList() {
        return modificationList;
    }

    public void setModificationList(HashMap<String, WeaponModificationContainer> modificationList) {
        this.modificationList = modificationList;
    }
}
