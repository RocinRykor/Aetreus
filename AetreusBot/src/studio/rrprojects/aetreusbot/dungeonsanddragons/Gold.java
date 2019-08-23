package studio.rrprojects.aetreusbot.dungeonsanddragons;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import studio.rrprojects.aetreusbot.command.Command;
import studio.rrprojects.aetreusbot.command.CommandParser.CommandContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterLoader;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerTools;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.CharacterParser.CharacterContainer;
import studio.rrprojects.aetreusbot.dungeonsanddragons.tools.ContainerToJsonWriter;
import studio.rrprojects.aetreusbot.utils.MessageBuilder;
import studio.rrprojects.aetreusbot.utils.NewMessage;

public class Gold extends Command{

	@Override
	public String getName() {
		return "Gold";
	}

	@Override
	public String getAlias() {
		return "Money";
	}

	@Override
	public String getHelpDescription() {
		return "D&D - Manages the player's gold";
	}

	@Override
	public String getHomeChannel() {
		return "BotTesting";
	}

	@Override
	public boolean isChannelRestricted() {
		return false;
	}

	@Override
	public boolean isAdminOnly() {
		return false;
	}

	@Override
	public boolean isAdultRestricted() {
		return false;
	}

	@Override
	public boolean deleteCallMessage() {
		return false;
	}
	
	HashMap<String, String> moneyMap;
	
	public Gold() {
		 moneyMap = new HashMap<>();
		 
		 InitMoneyMap();
	}
	
	public void executeMain(CommandContainer cmd) {
		if (cmd.AUTHOR.isFake()) {
			return;
		}
		
		CharacterContainer character = CharacterLoader.GetPlayerData(cmd.AUTHOR.getName());
		
		HashMap<String, Runnable> subCommands = new HashMap<>();
		subCommands.put("list", () -> ListMoney(cmd, character));
		subCommands.put("add", () -> AddMoney(cmd, character));
		subCommands.put("remove", () -> RemoveMoney(cmd, character));
		subCommands.put("set", () -> SetMoney(cmd, character));
		subCommands.put("exchange", () -> ExchangeMoney(cmd, character));
		subCommands.put("help", () -> HelpCommand(cmd, subCommands));

		if (subCommands.containsKey(cmd.MAIN_ARG.toLowerCase())) {
			subCommands.get(cmd.MAIN_ARG.toLowerCase()).run();
			return;
		}
		
		ListMoney(cmd, character);
		
		
		//SendMessage(finalMessage, cmd.DESTINATION);
	}
	
	private void AddMoney(CommandContainer cmd, CharacterContainer character) {
		String[] secondaryArg = cmd.SECONDARY_ARG;
		
		if (!(secondaryArg.length > 0)) {
			return;
		}
		
		String moneyAmount, moneyType;
		
		for (int i = 0; i < secondaryArg.length; i++) {
			moneyAmount = secondaryArg[i].replaceAll("[^0-9]", "");
			moneyType = secondaryArg[i].replaceAll("[0-9]", "");
			
			System.out.println(moneyType);
			
			if (moneyMap.containsKey(moneyType)) {
				String searchTerm = moneyMap.get(moneyType);
				
				System.out.println(searchTerm);
				
				int newValue = character.characterInventory.money.get(searchTerm) + Integer.parseInt(moneyAmount);
				
				character.characterInventory.money.replace(searchTerm, newValue);
			}
			
		}
		
		ListMoney(cmd, character);
	}

	private void RemoveMoney(CommandContainer cmd, CharacterContainer character) {
	}

	private void SetMoney(CommandContainer cmd, CharacterContainer character) {

	}

	private void ExchangeMoney(CommandContainer cmd, CharacterContainer character) {
	}
	
	private void HelpCommand(CommandContainer cmd, HashMap<String, Runnable> subCommands) {
		String message = "Money Command: Can be used to manage the player's money. \n"
				+ "Valid Arguments: \n";
		
		for (Map.Entry<String, Runnable> command: subCommands.entrySet()) {
			message += command.getKey() + "\n";
		}
		
		SendMessage(message, cmd.DESTINATION);
	}

	private void ListMoney(CommandContainer cmd, CharacterContainer character) {
		String title = "Character Name: " + character.characterInfo.characterName;
		
		String header = "Character's Gold:";
		
		String message = "";
		
		HashMap<String, Integer> money = character.characterInventory.money;
		
		for (HashMap.Entry<String, Integer> entry : money.entrySet()) {
			message += entry.getKey() + ": " + entry.getValue() + "\n"; 
		}
		
		EmbedBuilder finalMessage = MessageBuilder.BuildMessage(title, header, message, Color.BLUE);
		
		SendMessage(finalMessage, cmd.DESTINATION);
	}

	private void SendMessage(EmbedBuilder message, Channel DESDESTINATION) {
		NewMessage.send(message, DESDESTINATION);
	}
	
	private void SendMessage(String message, Channel DESTINATION) {
		NewMessage.send(message, DESTINATION);
	}
	
	private void InitMoneyMap() {
		// TODO Auto-generated method stub
		
		//Copper
		moneyMap.put("c", "copper");
		moneyMap.put("cp", "copper");
		moneyMap.put("cop", "copper");
		moneyMap.put("copper", "copper");
		
		//Silver
		moneyMap.put("s", "silver");
		moneyMap.put("sp", "silver");
		moneyMap.put("sil", "silver");
		moneyMap.put("silver", "silver");
		
		//Electrum
		moneyMap.put("e", "electrum");
		moneyMap.put("ep", "electrum");
		moneyMap.put("elec", "electrum");
		moneyMap.put("electrum", "electrum");
		
		//Gold
		moneyMap.put("g", "gold");
		moneyMap.put("gp", "gold");
		moneyMap.put("gold", "gold");
		
		//Platinum
		moneyMap.put("p", "platinum");
		moneyMap.put("pp", "platinum");
		moneyMap.put("plat", "platinum");
		moneyMap.put("platinum", "platinum");
	}
}
