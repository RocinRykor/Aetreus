package com.rocinrykor.aetreusbot.utils;

public class ArgCountChecker {

	public static boolean argChecker(int length, int minCount) {
		int checker = 0;
		checker += length;
		if (checker >= minCount) {
			return true;
		} else {
			return false;
		}
	}
	
}
