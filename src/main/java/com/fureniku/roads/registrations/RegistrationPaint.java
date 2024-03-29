package com.fureniku.roads.registrations;

import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.RegistrationGroup;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.OffsetHelper;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.roads.data.PaintType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;

public class RegistrationPaint extends RegistrationGroup {

    /* Notes for custom paint 2.0 because the top of a random class is a sensible place to put it and i'll totally remember later.

    Version 1.0 worked but had flaws:
        - Every pixel of the paint model was individually drawn, meaning potentially 256 calls in the bakery event for a 16x16 paint. Most paints were 32x32, which is 1024.
        Sides were also drawn on the edges of paints.
        THATS BAD.
        - The system didn't support connecting styles, only static icons
        THATS BORING.
        - The system only worked with single colours
        THATS... expected but also boring.

    CUE VERSION 2.0!
        - Paint files are now going to be made of structure entries. For example a straight line might have just one entry
        Entries are Vec4's, indicating a start and end position. The straight line entry might be 7,0,9,16 (from X7 Y0 to X9 Y16)
        Each entry is drawn as a single call now. Each entry is effectively one scaled model. It's basically how the vanilla system would work.
        ... could I just use the vanilla system? That might actually be much better... My editor would generate vanilla models.

        - We can add support for connecting systems. Connecting paints had two types:
            - Connect in a direction indiscriminately (center lines)
            - Connect in a direction but varied depending on the orientation of the target to connect to (side lines)
        - First is very easy. Editor has a mirrored image, you edit one and the other auto-updates to show how two side-by-side textures might connect. Enable/disable the NESW as preference.
        - Editor exports the north connection and we rotate as normal.
        - Second is a bit more complicated. I can do it in code but I'm not sure how to explain in editor to make it idiot proof enough. Might just mark as advanced.

        - Paints only having one colour is boring, but not a limitation.
        Now that we can redesign from the ground up with support for alternate paint styles in mind, we can also design paints which contain multiple styles.
        The new element system on the paint gun allows for this. Elements can be flagged using a colour enum. This defaults to "all", but setting anything else will mark that paint as a specific colour.
        Mutlicolour paints will have their own section of the paint gun, and no longer auto-adapt to every colour. If people want mutliple colours
        (e.g. this paint can be white/red/yellow but has a blue accent part), they need to create mutliple specific paints.
        Mutlicolour paints will only load if all listed colours are installed. If it requires a plugin, it won't load and an error in the console (and maybe on login in-game)
        Cost-wise, the paint will require all listed colours to be available in the paint gun. The costing ratio will be split evenly regardless of the actual distribution.


    While we're here... custom paint colours
        - Paint gun will now support all paint colours. Every colour can implement one specific dye colour, as well as an RGB value.
        - Paint gun tanks can hold more variants now. The UI buttons change to a dropdown, and you can select any dye that you have available
        - Every paint colour will have a cost associated for both options. For example, yellow might say 2mb of yellow dye, or 1mb each of green and blue.
     */

    private ArrayList<String> _paintLineNames = new ArrayList<>();
    private CreativeTabSet _paintLineTab;

    private BlockBehaviour.Properties _props = BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.STONE);

    public RegistrationPaint(RegistrationBase registrationBase) {
        super(registrationBase);
    }

    public MetroBlockDecorativeBuilder paintBlock(String modelName) {
        return new MetroBlockDecorativeBuilder(_props)
                .setModelDirectory("blocks/paint/")
                .setModelName(modelName)
                .setWidth(16)
                .setHeight(0.5f)
                .addHelper(new OffsetHelper(BlockOffsetDirection.DOWN));
    }

    @Override
    public void init(IEventBus iEventBus) {
        registerPaintSet(PaintType.WHITE_PAINT);
        registerPaintSet(PaintType.YELLOW_PAINT);
        registerPaintSet(PaintType.RED_PAINT);
    }

    @Override
    public void generateCreativeTabs() {
        for (int i = 0; i < _paintLineNames.size(); i++) {
            _paintLineTab.addItem(getItem(_paintLineNames.get(i)).get().getDefaultInstance());
        }
    }

    @Override
    public ArrayList<CreativeTabSet> getCreativeTabs() {
        ArrayList<CreativeTabSet> tabList = new ArrayList<>();
        tabList.add(_paintLineTab);
        return tabList;
    }

    private void registerPaintSet(PaintType paintType) {
        registerLines(paintType.getUnlocalizedName(), paintType.getTexture());
    }

    private void registerLines(String paintName, ResourceLocation texture) {
        _paintLineNames.add(registerBlockSet(paintName + PaintStrings.PAINT_LINE_SINGLE_CENTER, () -> paintBlock("bollard_concrete_pillar").setTextures(texture).build()));
    }
}
