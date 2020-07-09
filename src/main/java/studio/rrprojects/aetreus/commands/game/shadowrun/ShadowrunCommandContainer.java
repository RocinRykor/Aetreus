package studio.rrprojects.aetreus.commands.game.shadowrun;

import net.dv8tion.jda.api.entities.MessageChannel;
import java.util.HashMap;

public class ShadowrunCommandContainer {
    MessageChannel DESTINATION;
    String primaryFunction = null;
    String secondaryFunction = null;
    Boolean hasNull = false;
    HashMap<String, String> ModList;

    public ShadowrunCommandContainer(String raw, MessageChannel DESTINATION) {
        this.DESTINATION = DESTINATION;
        System.out.println(raw);
        Parse(raw);
    }

    private void Parse(String raw) {
        String[] breakdown = raw.split(" ");

        if (breakdown.length >= 3) {
            primaryFunction = breakdown[1];
            secondaryFunction = breakdown[2];
            hasNull = false;
        } else {
            if (breakdown.length == 2) {
                primaryFunction = breakdown[1];
            } else {
                primaryFunction = null;
            }
            secondaryFunction = null;
            hasNull = true;
        }
    }
}
