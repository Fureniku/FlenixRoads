package com.fureniku.roads.blocks.entityblock;

import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import com.fureniku.metropolis.blocks.decorative.MetroEntityBlockDecorative;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.menus.MetroMenu;
import com.fureniku.roads.blockentities.FabricatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public abstract class FabricatorEntityBlock extends MetroEntityBlockDecorative {

    public static RegistryObject<BlockEntityType<MetroBlockEntity>> ENTITY;
    public static RegistryObject<MetroMenu> MENU;

    public FabricatorEntityBlock(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures) {
        super(props, shape, modelDir, modelName, tag, dynamicShape, textures);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FabricatorBlockEntity(pos, state);
    }
}
