package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Damage {
    JsonObject jsonObject;
    String threshold;
    String amount;
    String type;

    public Damage(JsonObject damage) {
        jsonObject = damage;
        threshold = damage.getString("threshold", "unknown");
        amount = damage.getString("amount", "unknown");
        type = damage.getString("type", "unknown");
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
