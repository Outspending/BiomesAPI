package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements AbstractArrow {

    public CraftArrow(CraftServer server, net.minecraft.world.entity.projectile.AbstractArrow entity) {
        super(server, entity);
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        Preconditions.checkArgument(knockbackStrength >= 0, "Knockback value (%s) cannot be negative", knockbackStrength);
        this.getHandle().setKnockback(knockbackStrength);
    }

    @Override
    public int getKnockbackStrength() {
        return this.getHandle().knockback;
    }

    @Override
    public double getDamage() {
        return this.getHandle().getBaseDamage();
    }

    @Override
    public void setDamage(double damage) {
        Preconditions.checkArgument(damage >= 0, "Damage value (%s) must be positive", damage);
        this.getHandle().setBaseDamage(damage);
    }

    @Override
    public int getPierceLevel() {
        return this.getHandle().getPierceLevel();
    }

    @Override
    public void setPierceLevel(int pierceLevel) {
        Preconditions.checkArgument(0 <= pierceLevel && pierceLevel <= Byte.MAX_VALUE, "Pierce level (%s) out of range, expected 0 < level < 127", pierceLevel);

        this.getHandle().setPierceLevel((byte) pierceLevel);
    }

    @Override
    public boolean isCritical() {
        return this.getHandle().isCritArrow();
    }

    @Override
    public void setCritical(boolean critical) {
        this.getHandle().setCritArrow(critical);
    }

    // Paper - moved to AbstractProjectile

    @Override
    public boolean isInBlock() {
        return this.getHandle().inGround;
    }

    @Override
    public Block getAttachedBlock() {
        if (!this.isInBlock()) {
            return null;
        }

        BlockPos pos = this.getHandle().blockPosition();
        return this.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[this.getHandle().pickup.ordinal()];
    }

    @Override
    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkArgument(status != null, "PickupStatus cannot be null");
        this.getHandle().pickup = net.minecraft.world.entity.projectile.AbstractArrow.Pickup.byOrdinal(status.ordinal());
    }

    // Paper start
    @Override
    public org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack getItemStack() {
        return org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack.asCraftMirror(getHandle().getPickupItem());
    }

    @Override
    public void setLifetimeTicks(int ticks) {
        this.getHandle().life = ticks;
    }

    @Override
    public int getLifetimeTicks() {
        return this.getHandle().life;
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public org.bukkit.Sound getHitSound() {
        return org.bukkit.craftbukkit.v1_20_R2.CraftSound.minecraftToBukkit(this.getHandle().soundEvent);
    }

    @Override
    public void setHitSound(@org.jetbrains.annotations.NotNull org.bukkit.Sound sound) {
        this.getHandle().setSoundEvent(org.bukkit.craftbukkit.v1_20_R2.CraftSound.bukkitToMinecraft(sound));
    }

    @Override
    public void setNoPhysics(boolean noPhysics) {
        this.getHandle().setNoPhysics(noPhysics);
    }

    @Override
    public boolean hasNoPhysics() {
        return this.getHandle().isNoPhysics();
    }
    // Paper end

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityArrow
        this.getHandle().life = value;
    }

    @Override
    public boolean isShotFromCrossbow() {
        return this.getHandle().shotFromCrossbow();
    }

    @Override
    public void setShotFromCrossbow(boolean shotFromCrossbow) {
        this.getHandle().setShotFromCrossbow(shotFromCrossbow);
    }

    @Override
    public net.minecraft.world.entity.projectile.AbstractArrow getHandle() {
        return (net.minecraft.world.entity.projectile.AbstractArrow) this.entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }
}
