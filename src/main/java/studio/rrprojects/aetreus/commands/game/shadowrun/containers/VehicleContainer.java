package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class VehicleContainer {
    JsonObject jsonObject;
    String category;
    String handling;
    String seating;
    String availability;
    int speed;
    int acceleration;
    int body;
    int armor;
    int signature;
    int pilot;
    int sensor;
    int cargo;
    int load;
    int cost;
    float street_index;
    HashMap<String, ModificationContainer> modificationList;

    public VehicleContainer(JsonObject vehicle) {
        jsonObject = vehicle;
        category = vehicle.getString("category", "unknown");
        handling = vehicle.getString("handling", "unknown");
        speed = vehicle.getInt("speed", 0);
        acceleration = vehicle.getInt("acceleration", 0);
        body = vehicle.getInt("body", 0);
        armor = vehicle.getInt("armor", 0);
        signature = vehicle.getInt("signature", 0);
        pilot = vehicle.getInt("pilot", 0);
        sensor = vehicle.getInt("sensor", 0);
        cargo = vehicle.getInt("cargo", 0);
        load = vehicle.getInt("load", 0);
        cost = vehicle.getInt("cost", 0);
        seating = vehicle.getString("seating", "unknown");
        speed = vehicle.getInt("speed", 0);
        availability = vehicle.getString("availability", "unknown");
        street_index = vehicle.getFloat("street_index", 0);
        modificationList = new HashMap<>();
        JsonObject modifications = vehicle.get("modifications").asObject();

        for (JsonObject.Member modification: modifications) {
            modificationList.put(modification.getName(), new ModificationContainer(modification.getValue().asObject()));
        }
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getSignature() {
        return signature;
    }

    public void setSignature(int signature) {
        this.signature = signature;
    }

    public int getPilot() {
        return pilot;
    }

    public void setPilot(int pilot) {
        this.pilot = pilot;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
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

    public HashMap<String, ModificationContainer> getModificationList() {
        return modificationList;
    }

    public void setModificationList(HashMap<String, ModificationContainer> modificationList) {
        this.modificationList = modificationList;
    }
}
