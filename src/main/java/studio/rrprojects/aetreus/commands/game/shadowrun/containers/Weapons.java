package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Weapons {
    JsonObject jsonObject;
    HashMap<String, WeaponContainer> weaponList;

    public Weapons(JsonObject weapons) {
        jsonObject = weapons;
        weaponList = new HashMap<>();
        for (JsonObject.Member weapon: weapons) {
            weaponList.put(weapon.getName(), new WeaponContainer(weapon.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, WeaponContainer> getWeaponList() {
        return weaponList;
    }

    public void setWeaponList(HashMap<String, WeaponContainer> weaponList) {
        this.weaponList = weaponList;
    }

    public String getAllWeapons() {
        String output = "";
        for (Map.Entry<String, WeaponContainer> weapon : weaponList.entrySet()) {
            WeaponContainer value = weapon.getValue();
            Damage damage = value.damage;

            String tmpString = String.format("%s, %s - Damage: %s%s (%s) ",
                    weapon.getKey(), value.getWeapon_type(), damage.threshold, damage.amount, damage.type);
            if (value.modifications.modificationList.size() > 0) {
                for (Map.Entry<String, ModificationContainer> mod: value.modifications.modificationList.entrySet()) {
                    tmpString += String.format(", %s: %s", mod.getKey(), mod.getValue().effect);
                }
            }
            output += tmpString + "\n";
        }

        return output;
    }
}
