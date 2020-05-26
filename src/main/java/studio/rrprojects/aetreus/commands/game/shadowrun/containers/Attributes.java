package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
    CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, AttributeContainer> attributeList;

    public Attributes(JsonObject attributes, CharacterContainer characterContainer) {
        parent = characterContainer;
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

    public CharacterContainer getParent() {
        return parent;
    }

    public void setParent(CharacterContainer parent) {
        this.parent = parent;
    }

    public int getAttribute(String searchTerm) {
        if (attributeList.containsKey(searchTerm)) {
            return attributeList.get(searchTerm).getTotal();
        }

        //Assuming the initial search failed - perform a more detailed search
        System.out.println(String.format("Search of: %s failed! - Searching Manually!", searchTerm));
        for (Map.Entry<String, AttributeContainer> attribute : attributeList.entrySet()) {
            if (attribute.getKey().toLowerCase().contains(searchTerm.toLowerCase())) {
                return attribute.getValue().getTotal();
            }
        }

        //If both failed return Error and 0
        System.out.println(String.format("ERROR UNABLE TO FIND ATTRIBUTE %s", searchTerm));
        return 0;
    }
}
