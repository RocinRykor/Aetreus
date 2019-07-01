package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.io.File;
import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import studio.rrprojects.aetreusbot.Controller;

public class CharacterFileLoader {

	public static void LoadCharacters() {
		/*
		 * See if CharacterPref File is loaded
		 * Load or Create as Needed
		 * Load/Create Files from CharacterPref file
		 * */
		
		String startingDir = Controller.getMainDir() + File.separator + "Character Files";
		List<Member> members = Controller.getJda().getGuilds().get(0).getMembersWithRoles(Controller.getJda().getRolesByName("Tabletop RPG", true).get(0));
		
		
		
	}

}
