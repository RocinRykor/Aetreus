package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class Armor {
    JsonObject jsonObject;
    HashMap<String, ArmorContainer> armorList;

    public Armor(JsonObject armor) {
        jsonObject = armor;
        armorList = new HashMap<>();

        for (JsonObject.Member armorItem: armor) {
            armorList.put(armorItem.getName(), new ArmorContainer(armorItem.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, ArmorContainer> getArmorList() {
        return armorList;
    }

    public void setArmorList(HashMap<String, ArmorContainer> armorList) {
        this.armorList = armorList;
    }
}
