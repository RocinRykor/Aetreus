package studio.rrprojects.aetreusbot.dungeonsanddragons;

public class DNDHandler {
	
	public static void InitTables() {
		System.out.println("Initializing Tables!");
		
		Attributes.initTable();
		Proficiency.initTable();
		Skills.initTable();
	}
}