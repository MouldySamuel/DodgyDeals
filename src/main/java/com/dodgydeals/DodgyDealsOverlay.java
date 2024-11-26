package com.dodgydeals;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.api.Perspective;
import net.runelite.api.NPCComposition;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class DodgyDealsOverlay extends Overlay
{
    private static final int RADIUS = 5; // Half of 11 (center included)

    private final Client client;
    private final DodgyDealsConfig config;

    private Set<NPC> pickpocketableNPCs = new HashSet<>();

    @Inject
    public DodgyDealsOverlay(Client client, DodgyDealsConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player player = client.getLocalPlayer();
        if (player == null)
        {
            return null;
        }

        WorldPoint playerLocation = player.getWorldLocation();

        // Clear the previous set of pickpocketable NPCs for this frame
        pickpocketableNPCs.clear();

        // Render the player's 11x11 radius
        for (int dx = -RADIUS; dx <= RADIUS; dx++)
        {
            for (int dy = -RADIUS; dy <= RADIUS; dy++)
            {
                WorldPoint point = new WorldPoint(
                        playerLocation.getX() + dx,
                        playerLocation.getY() + dy,
                        playerLocation.getPlane()
                );

                renderTile(graphics, point);
            }
        }

        // Render NPC highlights within the 11x11 radius, only if they have Pickpocket option
        for (NPC npc : client.getNpcs())
        {
            WorldPoint npcLocation = npc.getWorldLocation();
            renderNpcTile(graphics, npcLocation, npc); // Pass the NPC to the renderNpcTile function
        }

        // Render the tooltip with the number of pickpocketable NPCs
        renderTooltip(graphics);

        return null;
    }

    private void renderTile(Graphics2D graphics, WorldPoint point)
    {
        LocalPoint localPoint = LocalPoint.fromWorld(client, point);
        if (localPoint == null)
        {
            return;
        }

        Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);
        if (tilePoly != null)
        {
            // Get color for the tile and border
            Color tileColor = config.tileColor();
            int tileOpacity = Math.min(255, Math.max(0, config.tileOpacity()));
            int borderOpacity = Math.min(255, Math.max(0, config.borderOpacity()));
            int borderWidth = Math.max(1, config.tileBorderWidth());

            // Apply tile opacity
            Color transparentTileColor = new Color(
                    tileColor.getRed(),
                    tileColor.getGreen(),
                    tileColor.getBlue(),
                    tileOpacity
            );
            graphics.setColor(transparentTileColor);
            graphics.fill(tilePoly); // Fill the tile

            // Apply border opacity
            Color transparentBorderColor = new Color(
                    tileColor.getRed(),
                    tileColor.getGreen(),
                    tileColor.getBlue(),
                    borderOpacity
            );
            graphics.setColor(transparentBorderColor);

            // Handle border drawing with configurable width
            if (borderWidth > 0)
            {
                graphics.setStroke(new BasicStroke(borderWidth));
                graphics.draw(tilePoly); // Draw the border with the specified stroke
            }
        }
    }

    private void renderNpcTile(Graphics2D graphics, WorldPoint npcLocation, NPC npc)
    {
        Player player = client.getLocalPlayer();
        if (player == null) return;

        WorldPoint playerLocation = player.getWorldLocation();

        // Check if NPC is within the 11x11 square around the player
        if (Math.abs(npcLocation.getX() - playerLocation.getX()) <= RADIUS &&
                Math.abs(npcLocation.getY() - playerLocation.getY()) <= RADIUS)
        {
            // Check if the NPC has "Pickpocket" option in its interactions
            if (!hasPickpocketOption(npc)) {
                return; // Skip this NPC if no Pickpocket option
            }

            // Add the NPC to the pickpocketable list
            pickpocketableNPCs.add(npc);

            LocalPoint localPoint = LocalPoint.fromWorld(client, npcLocation);
            if (localPoint == null)
            {
                return;
            }

            Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);
            if (tilePoly != null)
            {
                // Get the NPC highlight color and opacity
                Color npcHighlightColor = config.npcHighlightColor();
                int npcOpacity = Math.min(255, Math.max(0, config.npcHighlightOpacity()));

                // Apply NPC tile highlight opacity
                Color transparentNpcHighlightColor = new Color(
                        npcHighlightColor.getRed(),
                        npcHighlightColor.getGreen(),
                        npcHighlightColor.getBlue(),
                        npcOpacity
                );
                graphics.setColor(transparentNpcHighlightColor);
                graphics.fill(tilePoly); // Fill the NPC's tile
            }
        }
    }

    private boolean hasPickpocketOption(NPC npc)
    {
        // Get the NPCComposition to access the actions
        NPCComposition npcComposition = npc.getComposition();
        if (npcComposition == null)
        {
            return false; // If no composition, return false
        }

        // Get the actions from the NPCComposition
        String[] actions = npcComposition.getActions();
        if (actions == null)
        {
            return false; // If no actions, return false
        }

        // Iterate over NPC interaction options to see if "Pickpocket" is one of them
        for (String option : actions)
        {
            if (option != null && option.equalsIgnoreCase("Pickpocket"))
            {
                return true; // Return true if Pickpocket is found
            }
        }

        return false; // Return false if Pickpocket is not found
    }

    private void renderTooltip(Graphics2D graphics)
    {
        // Enable anti-aliasing for better text rendering
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Define tooltip text
        String tooltipText = "Pickpocketable NPCs: " + pickpocketableNPCs.size();

        // Set font for the tooltip
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));

        // Measure the size of the text to properly position and size the box
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(tooltipText);
        int textHeight = fontMetrics.getHeight();

        // Define the box dimensions (with padding)
        int padding = 6; // Padding around the text inside the box
        int boxWidth = textWidth + 2 * padding;
        int boxHeight = textHeight + 2 * padding;

        // Get the screen width (this will allow us to center the tooltip)
        int screenWidth = client.getCanvasWidth();

        // Calculate the x position to center the tooltip (horizontal centering)
        int xPosition = (screenWidth - boxWidth) / 2;

        // Set the semi-transparent color for the box (50% opacity)
        Color boxColor = new Color(0, 0, 0, 128); // Semi-transparent black (50% opacity)
        graphics.setColor(boxColor);

        // Draw the box behind the text
        graphics.fillRoundRect(xPosition, 10 - padding, boxWidth, boxHeight, 8, 8);

        // Set the color for the text (White)
        graphics.setColor(Color.WHITE);

        // Draw the tooltip text centered inside the box
        graphics.drawString(tooltipText, xPosition + padding, 10 + fontMetrics.getAscent());
    }
}