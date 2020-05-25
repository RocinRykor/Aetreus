package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class ConditionContainer {
    JsonObject jsonObject;
    int stun;
    int physical;
    int overflow;
    public ConditionContainer(JsonObject condition) {
        jsonObject = condition;
        stun = condition.getInt("stun", 0);
        physical = condition.getInt("physical", 0);
        overflow = condition.getInt("overflow", 0);
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getStun() {
        return stun;
    }

    public void setStun(int stun) {
        this.stun = stun;
    }

    public int getPhysical() {
        return physical;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
    }

    public int getOverflow() {
        return overflow;
    }

    public void setOverflow(int overflow) {
        this.overflow = overflow;
    }

    public String Display() {
        String emojiStun = ":orange_circle:";
        String emojiPhysical = ":red_circle:";
        String emojiOverflow = ":skull_crossbones:";
        String output = "";

        output = String.format(
                "STUN: %s (%d/10)\n" +
                "PHYS: %s (%d/10)", counterToEmoji(stun, emojiStun), stun,
                counterToEmoji(physical, emojiPhysical), physical);
        if (overflow > 0) {
            output += String.format("\nOVER: %s (%d/10)", counterToEmoji(overflow, emojiOverflow), overflow);
        }

        return output;
    }

    private Object counterToEmoji(int count, String emojiCode) {
        int countBlank = 10-count;
        String emojiBlank = ":black_circle:";

        StringBuilder string= new StringBuilder();
        for (int i = 0; i < count; i++) {
            string.append(emojiCode).append(" ");
        }
        for (int i = 0; i < countBlank; i++) {
            string.append(emojiBlank).append(" ");
        }
        return string.toString();
    }
}
