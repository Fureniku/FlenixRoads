package com.fureniku.roads.data;

import com.fureniku.roads.FurenikusRoads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.awt.*;

public class PaintType {

    public static final PaintType WHITE_PAINT = new PaintType("white", new Color(255, 255, 255));
    public static final PaintType YELLOW_PAINT = new PaintType("yellow", new Color(255, 255, 0));
    public static final PaintType RED_PAINT = new PaintType("red", new Color(255, 0, 0));

    private String _name;
    private Color _rgbCol;
    private int _rCost;
    private int _gCost;
    private int _bCost;
    private ResourceLocation _textureLocation = new ResourceLocation(FurenikusRoads.MODID, "paint_white");
    private boolean _recolourWhite = false;

    /**
     * Construct a paint type, providing the name, colour, and location of the texture.
     * This will use the built-in white texture and recolour according to your provided colour.
     * @param name The unlocalized name of your paint
     * @param col A Java.AWT colour for your paint. Used to calculate costs in this case.
     */
    public PaintType(String name, Color col) {
        _name = name;
        _rgbCol = col;
        parseColourToCost(col);
        _recolourWhite = true;
    }

    /**
     * Construct a paint type, providing the name, colour, and location of the texture.
     * @param name The unlocalized name of your paint
     * @param col A Java.AWT colour for your paint. Used to calculate costs in this case.
     * @param textureLocation A resource location for the texture. Include your modID!
     */
    public PaintType(String name, Color col, ResourceLocation textureLocation) {
        _name = name;
        _rgbCol = col;
        parseColourToCost(col);
        _textureLocation = textureLocation;
    }

    /**
     * Construct a paint type, providing the name, colour, and costs.
     * This will use the built-in white texture and recolour according to your provided colour.
     * the three ints should add up to between 8 and 10 - the total cost of paint units in the paint gun.
     * @param name The unlocalized name of your paint
     * @param col A Java.AWT colour for your paint. Used to calculate costs in this case.
     * @param rCost The unit cost for red paint
     * @param gCost The unit cost for green paint
     * @param bCost The unit cost for white paint
     */
    public PaintType(String name, Color col, int rCost, int gCost, int bCost) {
        _name = name;
        _rgbCol = col;
        _rCost = rCost;
        _gCost = gCost;
        _bCost = bCost;
        _recolourWhite = true;
    }

    /**
     * Construct a paint type, providing the name, colour, and location of the texture.
     * @param name The unlocalized name of your paint
     * @param col A Java.AWT colour for your paint. Used to calculate costs in this case.
     * @param textureLocation A resource location for the texture. Include your modID!
     */
    public PaintType(String name, Color col, int rCost, int gCost, int bCost, ResourceLocation textureLocation) {
        _name = name;
        _rgbCol = col;
        _rCost = rCost;
        _gCost = gCost;
        _bCost = bCost;
    }

    public String getUnlocalizedName() {
        return _name;
    }

    public String getLocalizedName() {
        return "[" + _name + "]"; //TODO
    }

    public ResourceLocation getTexture() {
        return _textureLocation;
    }

    private void parseColourToCost(Color colIn) {
        int sum = colIn.getRed() + colIn.getGreen() + colIn.getBlue();

        double percentR = (double)colIn.getRed() / sum * 90;
        double percentG = (double)colIn.getGreen() / sum * 90;
        double percentB = (double)colIn.getBlue() / sum * 90;

        _rCost = (int)Math.round(percentR);
        _gCost = (int)Math.round(percentG);
        _bCost = (int)Math.round(percentB);
    }
}
