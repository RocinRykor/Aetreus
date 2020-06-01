package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Spells {
    CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, MagicalSpell> magicalSpells;
    HashMap<String, AdeptPower> adeptPowers;

    public Spells(JsonObject spells, CharacterContainer characterContainer) {
        parent = characterContainer;
        jsonObject = spells;
        magicalSpells = new HashMap<>();
        adeptPowers = new HashMap<>();

        for (JsonObject.Member spell : spells.get("magical_spells").asObject()) {
            magicalSpells.put(spell.getName(), new MagicalSpell(spell.getValue().asObject()));
        }

        for (JsonObject.Member power : spells.get("adept_powers").asObject()) {
            adeptPowers.put(power.getName(), new AdeptPower(power.getValue().asObject()));
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

    public HashMap<String, MagicalSpell> getMagicalSpells() {
        return magicalSpells;
    }

    public void setMagicalSpells(HashMap<String, MagicalSpell> magicalSpells) {
        this.magicalSpells = magicalSpells;
    }

    public HashMap<String, AdeptPower> getAdeptPowers() {
        return adeptPowers;
    }

    public void setAdeptPowers(HashMap<String, AdeptPower> adeptPowers) {
        this.adeptPowers = adeptPowers;
    }

    public String getAllAdeptPowers() {
        String output = "";
        for (Map.Entry<String, AdeptPower> power : adeptPowers.entrySet()) {
            AdeptPower value = power.getValue();

            String tmpString = String.format("%s, level %d",
                    power.getKey(), value.getLevel());
            output += tmpString + "\n";
        }

        return output;
    }
}
