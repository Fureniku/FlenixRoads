package com.silvaniastudios.roads.client.model.diagonal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.silvaniastudios.roads.FurenikusRoads;
import com.silvaniastudios.roads.blocks.diagonal.HalfBlock;
import com.silvaniastudios.roads.blocks.diagonal.RoadBlockDiagonal;
import com.silvaniastudios.roads.blocks.diagonal.ShapeLibrary;
import com.silvaniastudios.roads.client.render.Quad;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class DiagonalBakedModelBase implements IBakedModel {

	protected VertexFormat format;
	Minecraft mc;

	public DiagonalBakedModelBase(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.format = format;
		mc = Minecraft.getMinecraft();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

		if (side != null) {
			return Collections.emptyList();
		}

		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		EnumFacing facing = extendedState.getValue(RoadBlockDiagonal.FACING);
		
		HalfBlock blockLeft = new HalfBlock(extendedState, HalfBlock.HalfBlockSide.LEFT);
		HalfBlock blockRight = new HalfBlock(extendedState, HalfBlock.HalfBlockSide.RIGHT);
		
		blockLeft.setPartnerBlock(blockRight);
		blockRight.setPartnerBlock(blockLeft);

		return packQuads(facing, blockLeft, blockRight);
	}

	//Overriden in subclasses
	protected List<BakedQuad> packQuads(EnumFacing facing, HalfBlock blockLeft, HalfBlock blockRight) {
		List<BakedQuad> quads = new ArrayList<>();
		return quads;
	}

	@Override public ItemOverrideList getOverrides() { return null; }
	@Override public boolean isAmbientOcclusion() { return true; }
	@Override public boolean isGui3d() { return false; }
	@Override public boolean isBuiltInRenderer() { return false; }
	@Override public TextureAtlasSprite getParticleTexture() { return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(FurenikusRoads.MODID + ":blocks/road_block_standard"); }
	@Override public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }


	/*
	 *    ###   #   #    #    ####          ####     #    #   #  #####  ####   #   #
	 *   #   #  #   #   # #   #   #         #   #   # #   #  #   #      #   #  #   #
	 *   #   #  #   #  #   #  #   #         #   #  #   #  # #    #      #   #  #   #
	 *   #   #  #   #  #####  #   #         ####   #####  ##     ####   ####    # #
	 *   # # #  #   #  #   #  #   #         #   #  #   #  # #    #      # #      # 
	 *   #  ##  #   #  #   #  #   #         #   #  #   #  #  #   #      #  #     #
	 *    ####   ###   #   #  ####          ####   #   #  #   #  #####  #   #    # 
	 */   

	//Creation of a shape and converting it into Minecraft quads.
	//Create the shape first (shapeTrapeziumLeft in this case)
	//Apply rotation to all quads. Rotation can be any int 1-360 (or more I guess but why?). Pretty rotated blocks!
	//Can rotate on X/Y/Z axis by calling Quad.rotateQuad[Axis]

	//After rotation, any post processing. If you want the shape to sit flush, the bottom face needs V flipping and both top and bottom need UVs updating.
	//THIS ONLY WORKS ON MINECRAFT LOCKED ROTATIONS (0 90 180 270). If we're doing weird rotations we can't do this. F in chat.
	//Finally convert to a baked quad and add to the list to return (Can do this in the first loop if we're not doing UV things)

	protected List<BakedQuad> createTriangle(List<BakedQuad> quads, boolean left, HalfBlock block, double width) {
		List<Quad> rawQuads = new ArrayList<>();

		if (left) {
			rawQuads = ShapeLibrary.shapeTriangleLeft(block, width, format);
		} else {
			rawQuads = ShapeLibrary.shapeTriangleRight(block, width, format);
		}

		return shapeBuilder(rawQuads, quads, block);
	}


	protected List<BakedQuad> createTrapezium(List<BakedQuad> quads, boolean left, HalfBlock block, double widthN, double widthW) {
		List<Quad> rawQuads = new ArrayList<>();

		if (left) {
			rawQuads = ShapeLibrary.shapeTrapeziumLeft(block, widthN, widthW, format);
		} else {
			rawQuads = ShapeLibrary.shapeTrapeziumRight(block, widthN, widthW, format);
		}

		return shapeBuilder(rawQuads, quads, block);
	}

	//Works for most default shapes that assume 90 degree rotations.
	protected List<BakedQuad> shapeBuilder(List<Quad> rawQuads, List<BakedQuad> quads, HalfBlock block) {
		for (int i = 0; i < rawQuads.size(); i++) {
			if (rawQuads.get(i) != null) {
				rawQuads.set(i, Quad.rotateQuadY(rawQuads.get(i), block.getRotation()));
			}
		}

		if (rawQuads.get(0) != null) {
			rawQuads.get(0).updateUVs(); //Prevent UV rotation on top face
		}
		
		if (rawQuads.get(1) != null) {
			rawQuads.get(1).setFlipV(true); //Flip UVs for bottom face
			rawQuads.get(1).updateUVs(); //Prevent UV rotation on bottom face
		}

		for (int i = 0; i < rawQuads.size(); i++) {
			if (rawQuads.get(i) != null) {
				BakedQuad baked = rawQuads.get(i).createQuad(block.getColour());
				
				if (i == 1 && block.isFluid()) {
					baked = rawQuads.get(i).createUnnormalizedQuad(block.getColour()); //Unnormalized? abnormal? idk I'm not good at 3d rendering math stuff 
				}
				
				quads.add(baked);
			}
		}

		return quads;
	}
}
