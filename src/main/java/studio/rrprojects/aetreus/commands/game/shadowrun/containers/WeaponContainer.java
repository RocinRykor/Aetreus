package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class WeaponContainer {

    JsonObject jsonObject;
    String weapon_type;
    String type;
    Range range;
    int conceal;
    String ammo;
    String mode;
    Damage damage;
    float weight;
    String availability;
    int cost;
    float streetIndex;
    String legal;
    int recoil_compensation;
    WeaponModifications modifications;

    public WeaponContainer(JsonObject weapon) {
        jsonObject = weapon;
        weapon_type = weapon.getString("weapon_type", "unknown");
        type = weapon.getString("type", "unknown");
        range = new Range(weapon.get("range").asObject());
        conceal = weapon.getInt("conceal", 0);
        ammo = weapon.getString("ammo", "unknown");
        mode = weapon.getString("mode", "unknown");
        damage = new Damage(weapon.get("damage").asObject());
        weight = weapon.getFloat("weight", 0);
        availability = weapon.getString("availability", "unknown");
        cost = weapon.getInt("cost", 0);
        streetIndex = weapon.getFloat("streetIndex", 0);
        legal = weapon.getString("legal", "unknown");
        recoil_compensation = weapon.getInt("recoil_compensation", 0);
        modifications = new WeaponModifications(weapon.get("modifications").asObject());
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getWeapon_type() {
        return weapon_type;
    }

    public void setWeapon_type(String weapon_type) {
        this.weapon_type = weapon_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public int getConceal() {
        return conceal;
    }

    public void setConceal(int conceal) {
        this.conceal = conceal;
    }

    public String getAmmo() {
        return ammo;
    }

    public void setAmmo(String ammo) {
        this.ammo = ammo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getStreetIndex() {
        return streetIndex;
    }

    public void setStreetIndex(float streetIndex) {
        this.streetIndex = streetIndex;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public int getRecoil_compensation() {
        return recoil_compensation;
    }

    public void setRecoil_compensation(int recoil_compensation) {
        this.recoil_compensation = recoil_compensation;
    }

    public WeaponModifications getModifications() {
        return modifications;
    }

    public void setModifications(WeaponModifications modifications) {
        this.modifications = modifications;
    }
}
