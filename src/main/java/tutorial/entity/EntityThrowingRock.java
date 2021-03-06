package tutorial.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tutorial.TutorialMain;

public class EntityThrowingRock extends EntityThrowable
{
	public EntityThrowingRock(World world) {
		super(world);
	}

	public EntityThrowingRock(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public EntityThrowingRock(World world, double par2, double par4, double par6) {
		super(world, par2, par4, par6);
	}
	
	@Override
	protected float getGravityVelocity() {
        return inGround ? 0.0F : super.getGravityVelocity();
    }

	@Override
	protected void onImpact(MovingObjectPosition mop)
	{
		if (!inGround) {
			for (int l = 0; l < 4; ++l) {
				worldObj.spawnParticle("crit", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
		
		if (mop.entityHit != null && !inGround) {
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 2.0F);
		} else {
			this.motionX = (double)((float)(mop.hitVec.xCoord - this.posX));
			this.motionY = (double)((float)(mop.hitVec.yCoord - this.posY));
			this.motionZ = (double)((float)(mop.hitVec.zCoord - this.posZ));
			float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
			this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
			this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
			inGround = true;
		}
		
		if (!worldObj.isRemote) {
			//setDead();
		}
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if (inGround && !worldObj.isRemote) {
			System.out.println("[ROCK] Picking up rock.");
			player.inventory.addItemStackToInventory(new ItemStack(TutorialMain.throwingRock));
			setDead();
		}
	}
}
