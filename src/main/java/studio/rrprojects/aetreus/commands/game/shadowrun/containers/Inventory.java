package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Inventory {
    private final CharacterContainer parent;
    JsonObject jsonObject;

    Weapons weapons;
    Armor armor;
    Vehicles vehicles;
    Equipment equipment;
    Credsticks credsticks;
    //Cyberware cyberware;

    public Inventory(JsonObject inventory, CharacterContainer characterContainer) {
        parent = characterContainer;
        jsonObject = inventory;

        weapons = new Weapons(inventory.get("weapons").asObject(), parent);
        armor = new Armor(inventory.get("armor").asObject(), parent);
        vehicles = new Vehicles(inventory.get("vehicles").asObject(), parent);
        equipment = new Equipment(inventory.get("equipment").asObject(), parent);
        credsticks = new Credsticks(inventory.get("credsticks").asObject(), parent);
        //cyberware = new Cyberware(inventory.get("cyberware").asObject());
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Weapons getWeapons() {
        return weapons;
    }

    public void setWeapons(Weapons weapons) {
        this.weapons = weapons;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Credsticks getCredsticks() {
        return credsticks;
    }

    public void setCredsticks(Credsticks credsticks) { this.credsticks = credsticks; }

    public Armor getArmor() {
        return armor;
    }

    public CharacterContainer getParent() {
        return parent;
    }
}
