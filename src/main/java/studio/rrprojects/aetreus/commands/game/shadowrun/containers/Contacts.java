package studio.rrprojects.aetreus.commands.game.shadowrun.containers;

import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Contacts {
    private final CharacterContainer parent;
    JsonObject jsonObject;
    HashMap<String, ContactContainer> contactList;

    public Contacts(JsonObject contacts, CharacterContainer characterContainer) {
        parent = characterContainer;
        jsonObject = contacts;
        contactList = new HashMap<>();

        for (JsonObject.Member contact : contacts) {
            contactList.put(contact.getName(), new ContactContainer(contact.getValue().asObject()));
        }
    }

    public String getAllContacts() {
        StringBuilder contacts = new StringBuilder();
        for (Map.Entry<String, ContactContainer> contact: contactList.entrySet()) {
            contacts.append(String.format("%s: %s, level %d\n", contact.getKey(), contact.getValue().getType(), contact.getValue().getLevel()));
        }
        return contacts.toString();
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HashMap<String, ContactContainer> getContactList() {
        return contactList;
    }

    public void setContactList(HashMap<String, ContactContainer> contactList) {
        this.contactList = contactList;
    }

    public CharacterContainer getParent() {
        return parent;
    }
}
