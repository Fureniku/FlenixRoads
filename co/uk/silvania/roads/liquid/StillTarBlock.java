package co.uk.silvania.roads.liquid;

import co.uk.silvania.roads.Roads;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class StillTarBlock extends BlockStationary implements IFluidBlock {
	
	public StillTarBlock(int id) {
		super(id, Material.water);
		this.setHardness(100.0F);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
	}
	
	@Override
	public int getRenderType() {
		return 4;
	}

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        entity.motionX *= 0.5D;
    	entity.motionY *= 0.1D;
        entity.motionZ *= 0.5D;
    }
	
	public void registerIcons(IconRegister iconRegister) {
		this.theIcon = new Icon[] {iconRegister.registerIcon("Roads:TarStill"), iconRegister.registerIcon("Roads:TarFlowing")};
	}

	@Override
	public Fluid getFluid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}
}