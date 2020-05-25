package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Inventory {
    JsonObject jsonObject;

    Weapons weapons;
    //Armor armor;
    //Vehicles vehicles;
    Equipment equipment;
    Credsticks credsticks;
    //Cyberware cyberware;

    public Inventory(JsonObject inventory) {
        jsonObject = inventory;

        weapons = new Weapons(inventory.get("weapons").asObject());
        //armor = new Armor(inventory.get("armor").asObject());
        //vehicles = new Vehicles(inventory.get("vehicles").asObject());
        equipment = new Equipment(inventory.get("equipment").asObject());
        credsticks = new Credsticks(inventory.get("credsticks").asObject());
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

    public void setCredsticks(Credsticks credsticks) {
        this.credsticks = credsticks;
    }
}
