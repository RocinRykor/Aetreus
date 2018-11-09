package com.rocinrykor.aetreusbot.baxter;

import com.rocinrykor.aetreusbot.discord.BotInfo;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

public class BaxterController extends Thread {
	private static JDA baxterJDA;
	static boolean baxterOnline;
	static String presenceDefault = "Type \"&Baxter Help\" to begin";
	
	static int sleepTime = 5;
	
	static int debugNum = 0;
	public static void StartBaxter() { 
		if (baxterOnline) {
			return;
		}
		
		if (BotInfo.getBOT_TOKEN().equalsIgnoreCase("DEFUALT_TOKEN_PLEASE_CHANGE")) {
			System.out.println("ERROR: A VALID TOKEN HAS NOT BEEN DEFINED \n"
					+ "PLEASE EDIT THE CONFIG FILE AND RESTART THE BOT");
			return;
		}
		
		try {
			baxterJDA = new JDABuilder(AccountType.BOT).setToken(BotInfo.getBAXTER_TOKEN()).build();
			SetPresence(presenceDefault);
			baxterOnline = true;
			
			UpdateTimer();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private static void UpdateTimer() {
		new Thread() {
			public void run() {
				while (baxterOnline) {
					try {
						Thread.sleep(sleepTime * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Update();
				}
			}
		}.start();
	}

	protected static void Update() {
		if (baxterOnline) {
		}
	}

	public static void StopBaxter() {
		if (baxterOnline) {
			baxterOnline = false;
			baxterJDA.shutdown();
		}
	}
	
	public JDA getJDA() {
		return baxterJDA;
	}
	
	public static void SetPresence(String presence) {
		baxterJDA.getPresence().setGame(Game.playing(presence));
	}
	
	public static void ResetPresence() {
		SetPresence(presenceDefault);
	}

}
