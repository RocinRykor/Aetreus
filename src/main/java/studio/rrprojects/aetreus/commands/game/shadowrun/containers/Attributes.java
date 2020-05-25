package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
    JsonObject jsonObject;
    HashMap<String, AttributeContainer> attributeList;

    public Attributes(JsonObject attributes) {
        jsonObject = attributes;
        attributeList = new HashMap<>();

        for (JsonObject.Member attribute : attributes) {
           attributeList.put(attribute.getName(), new AttributeContainer(attribute.getValue().asObject()));
        }
    }
    public String getAllAttributes() {
        StringBuilder firstLine = new StringBuilder("Attr:  ");
        StringBuilder secondLine = new StringBuilder("Value: ");

        for (Map.Entry<String, AttributeContainer> attribute: attributeList.entrySet()) {
            firstLine.append(" ").append(attribute.getKey().toUpperCase().charAt(0));
            secondLine.append(" ").append(attribute.getValue().getBase() + attribute.getValue().getModifier());
        }

        return String.valueOf(firstLine.append("\n").append(secondLine));
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, AttributeContainer> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(HashMap<String, AttributeContainer> attributeList) {
        this.attributeList = attributeList;
    }

}
