package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

public class ItemContainer {
    JsonObject jsonObject;
    int rating;
    int quantity;
    String notes;
    public ItemContainer(JsonObject item) {
        jsonObject = item;
        rating = item.getInt("rating", 1);
        quantity = item.getInt("quantity", 1);
        notes = item.getString("notes", "Unknown");
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
