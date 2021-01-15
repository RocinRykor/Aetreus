package studio.rrprojects.aetreus.gui;

import net.dv8tion.jda.api.JDA;

public class GUIController {
    JDA jda;

    public GUIController(JDA jda) {
        this.jda = jda;
    }

    public void Initialize() {
        System.out.println("GUI Controller Initialization");
        MainWindow window = new MainWindow("Aetreus Text Channel Integration v1", jda);
    }
}
