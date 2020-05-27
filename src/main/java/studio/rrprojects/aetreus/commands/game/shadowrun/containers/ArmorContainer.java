package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class ArmorContainer {
    JsonObject jsonObject;
    String availability;
    String legal;
    int conceal;
    int ballistic_rating;
    int impact_rating;
    int weight;
    int cost;
    float street_index;
    HashMap<String, ModificationContainer> modifications;

    public ArmorContainer(JsonObject armor) {
        jsonObject = armor;
        conceal = armor.getInt("conceal", 0);
        ballistic_rating = armor.getInt("ballistic_rating", 0);
        impact_rating = armor.getInt("impact_rating", 0);
        weight = armor.getInt("weight", 0);
        availability = armor.getString("availability", "unknown");
        cost = armor.getInt("cost", 0);
        street_index = armor.getFloat("street_index", 0);
        legal = armor.getString("legal", "unknown");
        modifications = new HashMap<>();
        JsonObject modificationList = armor.get("modifications").asObject();

        for (JsonObject.Member modification: modificationList) {
            modifications.put(modification.getName(), new ModificationContainer(modification.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public int getConceal() {
        return conceal;
    }

    public void setConceal(int conceal) {
        this.conceal = conceal;
    }

    public int getBallistic_rating() {
        return ballistic_rating;
    }

    public void setBallistic_rating(int ballistic_rating) {
        this.ballistic_rating = ballistic_rating;
    }

    public int getImpact_rating() {
        return impact_rating;
    }

    public void setImpact_rating(int impact_rating) {
        this.impact_rating = impact_rating;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getStreet_index() {
        return street_index;
    }

    public void setStreet_index(float street_index) {
        this.street_index = street_index;
    }

    public HashMap<String, ModificationContainer> getModifications() {
        return modifications;
    }

    public void setModifications(HashMap<String, ModificationContainer> modifications) {
        this.modifications = modifications;
    }
}
