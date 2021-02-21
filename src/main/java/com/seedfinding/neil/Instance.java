package com.seedfinding.neil;

import net.fabricmc.api.ModInitializer;

public class Instance implements ModInitializer {
	public static final GenController genController=new GenController();
	@Override
	public void onInitialize() {
		System.out.println("Vizard has been loaded");
	}
}
