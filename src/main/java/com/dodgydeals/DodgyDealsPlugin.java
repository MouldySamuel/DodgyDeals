package com.dodgydeals;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
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
	private static final int[] THIEVING_ANIMATIONS = {881, 882, 883}; // Example animation IDs
	private long THIEVING_TIMEOUT;
	private long lastThievingTime;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private DodgyDealsOverlay overlay;

	@Inject
	private DodgyDealsConfig config;

	@Provides
	DodgyDealsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DodgyDealsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		THIEVING_TIMEOUT = Math.max(config.pluginHideTime() * 1000L, 3000L);
		overlayManager.add(overlay);
		lastThievingTime = System.currentTimeMillis();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event) {
		if (!(event.getActor() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getActor();
		if (player.equals(client.getLocalPlayer())) {
			int animationId = player.getAnimation();
			if (isThievingAnimation(animationId)) {
				lastThievingTime = System.currentTimeMillis();
			}
		}
	}

	private boolean isThievingAnimation(int animationId) {
		for (int id : THIEVING_ANIMATIONS) {
			if (id == animationId) {
				return true;
			}
		}
		return false;
	}

	public boolean shouldShowOverlay() {
		// Update timeout dynamically in case config changes while running
		THIEVING_TIMEOUT = Math.max(config.pluginHideTime() * 1000L, 3000L);
		return System.currentTimeMillis() - lastThievingTime <= THIEVING_TIMEOUT;
	}
}