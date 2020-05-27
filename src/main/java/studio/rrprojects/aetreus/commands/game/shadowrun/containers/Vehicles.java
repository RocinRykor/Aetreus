package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;

public class Vehicles {
    private final CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, VehicleContainer> vehicleList;

    public Vehicles(JsonObject vehicles, CharacterContainer characterContainer) {
        parent = characterContainer;
        jsonObject = vehicles;
        vehicleList = new HashMap<>();

        for (JsonObject.Member vehicle: vehicles) {
            vehicleList.put(vehicle.getName(), new VehicleContainer(vehicle.getValue().asObject()));
        }
    }

    public CharacterContainer getParent() {
        return parent;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, VehicleContainer> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(HashMap<String, VehicleContainer> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
