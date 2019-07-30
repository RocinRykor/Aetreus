package studio.rrprojects.aetreusbot.shadowrun;

import java.util.ArrayList;

import studio.rrprojects.aetreusbot.shadowrun.Shadowrun.RollContainer;

public class FlagHandler {
	static ArrayList<FlagContainer> flags = new ArrayList<>();	
	
	public static RollContainer ProcessFlags(RollContainer rollContainer, String[] secondaryArg ) {
		ResetAllFlags();
		
		if (secondaryArg != null) {
			for (int i = 0; i < secondaryArg.length; i++) {
				for (FlagContainer flag : flags) {
					if (flag.longName.equalsIgnoreCase(secondaryArg[i]) || flag.shortName.equalsIgnoreCase(secondaryArg[i])) {
						flag.isActive = true;
						if (flag.hasModifier == true) {
							flag.modValue = ProcessModifier(secondaryArg, i);
						}
					}
					
				}
			}
		}
		
		return rollContainer;
	}

	private static int ProcessModifier(String[] secondaryArg, int i) {
		String trimmedString = secondaryArg[i].replaceAll("[\\D]", "");
		
		try {
			return Integer.parseInt(trimmedString);
		} catch (Exception e) {
			if (i < secondaryArg.length) {
				
				try {
					return Integer.parseInt(secondaryArg[i+1]);
				} catch (Exception e2) {
					return 0;
				}
				
			} else {
				return 0;
			}
		}
	}

	private static void ResetAllFlags() {
		for (FlagContainer flag : flags) {
			flag.isActive = false;
			flag.modValue = 0;
		}
	}

	public static void InitFlags() {
		flags.add(new FlagContainer("Verbose", "v", "Show individual dice rolls", false, false, 0));
		flags.add(new FlagContainer("Initiative", "i", "Combat initiative", false, true, 0));
		flags.add(new FlagContainer("Extended", "x", "Multiple dice rolls over an extended period", false, true, 0));
		flags.add(new FlagContainer("Threshold", "t", "Automatic test against the threshold", false, true, 0));
		flags.add(new FlagContainer("Prime", "p", "Prime Runner Quality (4s are hits)", false, false, 0));
		flags.add(new FlagContainer("Edge", "e", "Spend Edge to add edge rating to roll", false, false, 0));
		flags.add(new FlagContainer("Gremlins", "g", "Gremlins Quality, reduces dice pool for glitch calculation", false, true, 0));
	}
	
	public static class FlagContainer {
		public String longName;
		public String shortName;
		public String basicDescription;
		public boolean isActive;
		public boolean hasModifier;
		public int modValue;
		
		public FlagContainer(String longName, String shortName, String basicDescription, boolean isActive, boolean hasModifier, int modValue) {
			this.longName = longName;
			this.shortName = shortName;
			this.basicDescription = basicDescription;
			this.isActive = isActive;
			this.hasModifier = hasModifier;
			this.modValue = modValue;
		}
	}

	public static boolean CheckFlag(String searchTerm) {
		for (FlagContainer flag : flags) {
			if (flag.longName.equalsIgnoreCase(searchTerm)) {
				if (flag.isActive) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static int GetModifier(String searchTerm) {
		for (FlagContainer flag : flags) {
			if (flag.longName.equalsIgnoreCase(searchTerm)) {
				return flag.modValue;
			}
		}
		
		return 0;
	}

}
