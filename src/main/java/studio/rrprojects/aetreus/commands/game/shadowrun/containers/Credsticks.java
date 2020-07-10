package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Credsticks {
    private final CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, CredstickContainer> credstickList;

    public Credsticks(JsonObject credsticks, CharacterContainer characterContainer) {
        parent = characterContainer;
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

    public CharacterContainer getParent() {
        return parent;
    }

    public ArrayList<CredstickMember> getAllCredsticks() {
        ArrayList<CredstickMember> output = new ArrayList();
        for (Map.Entry<String, CredstickContainer> credstick : credstickList.entrySet()) {
            output.add(new CredstickMember(credstick.getKey(), credstick.getValue()));
        }

        return output;
    }

    public CredstickMember getPrimaryCredstick() {
        return getAllCredsticks().get(0);
    }
}
