package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Career {
    JsonObject jsonObject;
    int karma;
    int nuyen;

    public Career(JsonObject career) {
        jsonObject = career;
        karma = career.getInt("karma", 0);
        nuyen = career.getInt("nuyen", 0);
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public int getNuyen() {
        return nuyen;
    }

    public void setNuyen(int nuyen) {
        this.nuyen = nuyen;
    }
}
