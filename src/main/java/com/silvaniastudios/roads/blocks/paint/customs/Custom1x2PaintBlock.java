package com.silvaniastudios.roads.blocks.paint.customs;

import com.silvaniastudios.roads.FurenikusRoads;
import com.silvaniastudios.roads.blocks.PaintColour;

import com.silvaniastudios.roads.blocks.paint.properties.PaintGrid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Custom1x2PaintBlock extends CustomPaintBlock {
	
	public static final PropertyEnum<Custom1x2PaintBlock.Enum1x2Block> CONNECT = PropertyEnum.create("position_rotation", Custom1x2PaintBlock.Enum1x2Block.class);

	public Custom1x2PaintBlock(String name, String localName, PaintGrid[] grids, String category, PaintColour colour) {
		super(name, localName, EnumPaintType.MULTI_2x1, grids, category, new int[] {0}, colour);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECT, Custom1x2PaintBlock.Enum1x2Block.n1));
	}

	public Custom1x2PaintBlock(String name, PaintGrid[] grids, String category, PaintColour colour) {
		super(name, EnumPaintType.MULTI_2x1, grids, category, new int[] {0}, colour, FurenikusRoads.tab_paint_icons);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECT, Custom1x2PaintBlock.Enum1x2Block.n1));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing placerFacing = placer.getHorizontalFacing();
		int returnMeta = meta;
		
		if (placerFacing.equals(EnumFacing.EAST))  { returnMeta = meta + 1; }
		if (placerFacing.equals(EnumFacing.SOUTH)) { returnMeta = meta + 2; }
		if (placerFacing.equals(EnumFacing.WEST))  { returnMeta = meta + 3; }

		IBlockState northBlock = world.getBlockState(pos.offset(EnumFacing.NORTH));
		IBlockState eastBlock =  world.getBlockState(pos.offset(EnumFacing.EAST));
		IBlockState southBlock = world.getBlockState(pos.offset(EnumFacing.SOUTH));
		IBlockState westBlock =  world.getBlockState(pos.offset(EnumFacing.WEST));
		
		if (placerFacing.equals(EnumFacing.NORTH)) {
			if (southBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(southBlock) == 0) { returnMeta = 4; }
			} else if (northBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(northBlock) == 4) { returnMeta = 0; }
			}
		}
		
		if (placerFacing.equals(EnumFacing.EAST)) {
			if (westBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(westBlock) == 1) { returnMeta = 5; }
			} else if (eastBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(eastBlock) == 5) { returnMeta = 1; }
			}
		}
		
		if (placerFacing.equals(EnumFacing.SOUTH)) {
			if (northBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(northBlock) == 2) { returnMeta = 6; }
			} else if (southBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(southBlock) == 6) { returnMeta = 2; }
			}
		}
		
		if (placerFacing.equals(EnumFacing.WEST)) {
			if (eastBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(eastBlock) == 3) { returnMeta = 7; }
			} else if (westBlock.getBlock() instanceof Custom1x2PaintBlock) {
				if (getMetaFromState(westBlock) == 7) { returnMeta = 3; }
			}
		}
		return this.getDefaultState().withProperty(CONNECT, Custom1x2PaintBlock.Enum1x2Block.byMetadata(returnMeta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Custom1x2PaintBlock.Enum1x2Block)state.getValue(CONNECT)).getMetadata();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(CONNECT, Custom1x2PaintBlock.Enum1x2Block.byMetadata(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {CONNECT});
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	public static enum Enum1x2Block implements IStringSerializable {
		n1(0, "n1"),
		e1(1, "e1"),
		s1(2, "s1"),
		w1(3, "w1"),
		n2(4, "n2"),
		e2(5, "e2"),
		s2(6, "s2"),
		w2(7, "w2");
		
		private static final Enum1x2Block[] META_LOOKUP = new Enum1x2Block[values().length];
		private final int meta;
		private final String name;
		
		private Enum1x2Block(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
		
		public int getMetadata() {
	        return this.meta;
	    }
		
		public static Enum1x2Block byMetadata(int meta) {
	        if (meta < 0 || meta >= META_LOOKUP.length) {
	            meta = 0;
	        }
	        
	        return META_LOOKUP[meta];
	    }
		
		static {
	        for (Enum1x2Block type: values()) {
	            META_LOOKUP[type.getMetadata()] = type;
	        }
	    }
	}
}
