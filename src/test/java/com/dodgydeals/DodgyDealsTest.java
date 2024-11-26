package com.dodgydeals;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DodgyDealsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DodgyDealsPlugin.class);
		RuneLite.main(args);
	}
}