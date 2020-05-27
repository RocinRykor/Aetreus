package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Armor {
    private final CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, ArmorContainer> armorList;

    public Armor(JsonObject armor, CharacterContainer characterContainer) {
        parent = characterContainer;
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

    public CharacterContainer getParent() {
        return parent;
    }

    public String getAllArmor() {
        String output = "";
        for (Map.Entry<String, ArmorContainer> armor : armorList.entrySet()) {
            ArmorContainer value = armor.getValue();

            String tmpString = String.format("%s, Ballistic Rating: %d | Impact Rating: %d",
                    armor.getKey(), value.getBallistic_rating(), value.getImpact_rating());
            if (value.modifications.size() > 0) {
                for (Map.Entry<String, ModificationContainer> mod: value.modifications.entrySet()) {
                    tmpString += String.format(", %s: %s", mod.getKey(), mod.getValue().effect);
                }
            }
            output += tmpString + "\n";
        }

        return output;
    }
}
