package com.rocinrykor.aetreusbot.baxter;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;

public class BaxterController extends Thread {
	
	/* 
	 * Baxter Controller
	 * Starts and maintains separate JDA for Baxter so that he can use his own "Playing Status"
	 * While active, has a constantly running timer to send an update to Baxter on a regular basis
	 * */
	
	private static JDA baxterJDA;
	private static boolean baxterOnline;
	static String presenceDefault = "Type \"&Baxter Help\" to begin";
	
	static int sleepTime = 5;
	
	static int debugNum = 0;
	
	public static void StartBaxter() { 
		if (isBaxterOnline()) {
			return;
		}
				
		try {
			//baxterJDA = new JDABuilder(AccountType.BOT).setToken(BotInfo.getBAXTER_TOKEN()).build();
		} catch (Exception e) {
			System.out.println("BAXTER JDA ERROR");
			return;
		}
		
		SetPresence(presenceDefault);
		setBaxterOnline(true);
		
		UpdateTimer();
	}

	private static void UpdateTimer() {
		new Thread() {
			public void run() {
				while (isBaxterOnline()) {
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
		if (isBaxterOnline()) {
			Meter.Update();
		}
	}

	public static void StopBaxter() {
		if (isBaxterOnline()) {
			setBaxterOnline(false);
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

	public static boolean isBaxterOnline() {
		return baxterOnline;
	}

	public static void setBaxterOnline(boolean baxterOnline) {
		BaxterController.baxterOnline = baxterOnline;
	}

}
