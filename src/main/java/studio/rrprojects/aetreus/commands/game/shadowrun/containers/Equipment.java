package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Equipment {
    CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, ItemContainer> equipmentList;

    public Equipment(JsonObject equipment, CharacterContainer characterContainer) {
        parent = characterContainer;
        jsonObject = equipment;
        equipmentList = new HashMap<>();

        for (JsonObject.Member item :equipment) {
            equipmentList.put(item.getName(), new ItemContainer(item.getValue().asObject()));
        }
    }

    public CharacterContainer getParent() {
        return parent;
    }

    public void setParent(CharacterContainer parent) {
        this.parent = parent;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, ItemContainer> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(HashMap<String, ItemContainer> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public String getAllEquipment() {
        String output = "";
        for (Map.Entry<String, ItemContainer> item : equipmentList.entrySet()) {
            String tmpString = String.format("%s (Rating: %d) x%d", item.getKey(), item.getValue().rating, item.getValue().getQuantity());
            output += tmpString + "\n";
        }

        return output;
    }
}
