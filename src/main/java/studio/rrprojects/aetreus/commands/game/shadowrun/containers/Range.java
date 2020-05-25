package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class Range {
    JsonObject jsonObject;
    String rangeShort;
    String rangeMedium;
    String rangeLong;
    String rangeExtreme;
    public Range(JsonObject range) {
        jsonObject = range;
        rangeShort = range.getString("short", "unknown");
        rangeMedium = range.getString("medium", "unknown");
        rangeLong = range.getString("long", "unknown");
        rangeExtreme = range.getString("extreme", "unknown");
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getRangeShort() {
        return rangeShort;
    }

    public void setRangeShort(String rangeShort) {
        this.rangeShort = rangeShort;
    }

    public String getRangeMedium() {
        return rangeMedium;
    }

    public void setRangeMedium(String rangeMedium) {
        this.rangeMedium = rangeMedium;
    }

    public String getRangeLong() {
        return rangeLong;
    }

    public void setRangeLong(String rangeLong) {
        this.rangeLong = rangeLong;
    }

    public String getRangeExtreme() {
        return rangeExtreme;
    }

    public void setRangeExtreme(String rangeExtreme) {
        this.rangeExtreme = rangeExtreme;
    }
}
