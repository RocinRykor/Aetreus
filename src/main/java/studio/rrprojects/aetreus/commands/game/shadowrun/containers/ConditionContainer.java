package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class ConditionContainer {
    CharacterContainer parent;
    JsonObject jsonObject;
    int stun;
    int physical;
    int overflow;
    public ConditionContainer(JsonObject condition, CharacterContainer characterContainer) {
        parent = characterContainer;
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
            int maxCount = parent.getAttributes().getAttribute("BOD");
            output += String.format("\nOVER: %s (%d/%d)", counterToEmoji(overflow, emojiOverflow, maxCount), overflow, maxCount);
        }

        return output;
    }

    private Object counterToEmoji(int count, String emojiCode, int maxCount) {
        int countBlank = maxCount-count;
        String emojiBlank = ":black_circle:";

        StringBuilder string= new StringBuilder();
        for (int i = 0; i < count; i++) {
            string.append(emojiCode).append(" ");
        }
        for (int i = 0; i < countBlank; i++) {
            string.append(emojiBlank).append(" ");
        }

        return string.toString();

        //TODO - Max overflow at character's body
    }

    private Object counterToEmoji(int count, String emojiCode) {
       return counterToEmoji(count, emojiCode, 10);
    }
}
