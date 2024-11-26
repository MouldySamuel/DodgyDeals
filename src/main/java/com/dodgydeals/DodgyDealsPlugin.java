package com.dodgydeals;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Dodgy Deals",
		description = "Displays an 11x11 radius around the player",
		tags = {"radius", "overlay", "player"}
)
public class DodgyDealsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private DodgyDealsOverlay overlay;

	@Provides
	DodgyDealsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DodgyDealsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}
}