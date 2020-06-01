package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;

public class CredstickContainer {
    JsonObject jsonObject;
    boolean is_fake;
    int rating;
    int nuyen;
    ArrayList<String> permits;

    public CredstickContainer(JsonObject credstick) {
        jsonObject = credstick;
        is_fake = credstick.getBoolean("is_fake", true);
        rating = credstick.getInt("force", 1);
        nuyen = credstick.getInt("nuyen", 0);
        JsonObject permitList = credstick.get("permits").asObject();
        for (JsonObject.Member permit: permitList) {
            permits.add(permit.getName());
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isIs_fake() {
        return is_fake;
    }

    public void setIs_fake(boolean is_fake) {
        this.is_fake = is_fake;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNuyen() {
        return nuyen;
    }

    public void setNuyen(int nuyen) {
        this.nuyen = nuyen;
    }

    public ArrayList<String> getPermits() {
        return permits;
    }

    public void setPermits(ArrayList<String> permits) {
        this.permits = permits;
    }
}
