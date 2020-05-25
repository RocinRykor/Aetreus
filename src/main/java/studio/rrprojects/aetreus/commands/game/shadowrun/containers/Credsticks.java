package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Credsticks {
    JsonObject jsonObject;
    HashMap<String, CredstickContainer> credstickList;

    public Credsticks(JsonObject credsticks) {
        jsonObject = credsticks;
        credstickList = new HashMap<>();
        for (JsonObject.Member credstick :credsticks) {
            credstickList.put(credstick.getName(), new CredstickContainer(credstick.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, CredstickContainer> getCredstickList() {
        return credstickList;
    }

    public void setCredstickList(HashMap<String, CredstickContainer> credstickList) {
        this.credstickList = credstickList;
    }

    public String getAllCredsticks() {
        String output = "";
        for (Map.Entry<String, CredstickContainer> credstick : credstickList.entrySet()) {
            String tmpString = String.format("Name: %s (Rating: %d) - Amount: %d¥", credstick.getKey(), credstick.getValue().rating, credstick.getValue().nuyen);
            if(credstick.getValue().is_fake) {
                tmpString += " (FAKE)";
            }
            output += tmpString + "\n";
        }

        return output;
    }
}
