package com.dodgydeals;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("dodgydeals")
public interface DodgyDealsConfig extends Config
{
    @ConfigItem(
            keyName = "tileColor",
            name = "Tile Color",
            description = "The color of the tile overlay",
            position = 0
    )
    @Alpha
    default Color tileColor()
    {
        return new Color(255, 146, 49, 210); // Default: #FF9231D2
    }

    @ConfigItem(
            keyName = "tileOpacity",
            name = "Tile Opacity",
            description = "Opacity of the tile overlay (0-255)",
            position = 1
    )
    default int tileOpacity()
    {
        return 210; // Default opacity for the tile
    }

    @ConfigItem(
            keyName = "borderOpacity",
            name = "Border Opacity",
            description = "Opacity of the border overlay (0-255)",
            position = 2
    )
    default int borderOpacity()
    {
        return 150; // Default opacity for the border
    }

    @ConfigItem(
            keyName = "tileBorderWidth",
            name = "Tile Border Width",
            description = "Width of the tile border",
            position = 3
    )
    default int tileBorderWidth()
    {
        return 2; // Default border width
    }

    // Grouping NPC settings together
    @ConfigItem(
            keyName = "npcHighlightColor",
            name = "NPC Tile Highlight Color",
            description = "The color of the tile that an NPC is on",
            position = 4
    )
    @Alpha
    default Color npcHighlightColor()
    {
        return new Color(0, 255, 0, 100); // Default: Light Green with opacity
    }

    @ConfigItem(
            keyName = "npcHighlightOpacity",
            name = "NPC Tile Opacity",
            description = "Opacity of the NPC's tile highlight (0-255)",
            position = 5
    )
    default int npcHighlightOpacity()
    {
        return 100; // Default opacity for the NPC tile highlight
    }
}
