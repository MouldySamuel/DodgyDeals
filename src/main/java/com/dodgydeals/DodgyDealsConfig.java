package com.dodgydeals;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("dodgydeals")
public interface DodgyDealsConfig extends Config
{
    @ConfigItem(
            keyName = "showTooltip",
            name = "Show Tooltip",
            description = "Toggle the visibility of the tooltip",
            position = 1
    )
    default boolean showTooltip()
    {
        return true;
    }

    @ConfigItem(
            keyName = "tooltipX",
            name = "Tooltip X Position",
            description = "The X coordinate for the tooltip's position",
            position = 2
    )
    default int tooltipX()
    {
        return 0; // Default to centered horizontally
    }

    @ConfigItem(
            keyName = "tooltipY",
            name = "Tooltip Y Position",
            description = "The Y coordinate for the tooltip's position",
            position = 3
    )
    default int tooltipY()
    {
        return 40; // Default to near the top of the screen
    }

    @ConfigItem(
            keyName = "tileColor",
            name = "Tile Color",
            description = "The color of the tiles highlighted around the player",
            position = 4
    )
    default Color tileColor() {
        return new Color(255, 146, 49, 210); // Default color
    }

    @ConfigItem(
            keyName = "tileOpacity",
            name = "Tile Opacity",
            description = "The opacity of the tiles highlighted around the player (0-255)",
            position = 5
    )
    default int tileOpacity() {
        return 0; // Default is 50% opacity
    }

    @ConfigItem(
            keyName = "borderOpacity",
            name = "Tile Border Opacity",
            description = "The opacity of the tile borders (0-255)",
            position = 6
    )
    default int borderOpacity() {
        return 255; // Default is fully opaque
    }

    @ConfigItem(
            keyName = "tileBorderWidth",
            name = "Tile Border Width",
            description = "The width of the tile borders",
            position = 7
    )
    default int tileBorderWidth() {
        return 1; // Default is 2 pixels
    }

    @ConfigItem(
            keyName = "npcHighlightColor",
            name = "NPC Highlight Color",
            description = "The color used to highlight pickpocketable NPCs",
            position = 8
    )
    default Color npcHighlightColor() {
        return Color.GREEN; // Default is green
    }

    @ConfigItem(
            keyName = "npcHighlightOpacity",
            name = "NPC Highlight Opacity",
            description = "The opacity of the NPC highlight (0-255)",
            position = 9
    )
    default int npcHighlightOpacity() {
        return 128; // Default is 50% opacity
    }

    @ConfigItem(
            keyName = "pluginHideTime",
            name = "Hide Overlay After",
            description = "How many seconds after thieving to wait before hiding the overlay (3+)",
            position = 10
    )
    default int pluginHideTime() {
        return 3; // Defaults to 3 seconds so that it doesn't disable between animations
    }
}