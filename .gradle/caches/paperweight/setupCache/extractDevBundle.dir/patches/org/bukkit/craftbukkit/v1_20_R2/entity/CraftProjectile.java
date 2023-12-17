package org.bukkit.craftbukkit.v1_20_R2.entity;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.world.entity.projectile.Projectile entity) {
        super(server, entity);
    }

    // Paper - moved to AbstractProjectile

    @Override
    public net.minecraft.world.entity.projectile.Projectile getHandle() {
        return (net.minecraft.world.entity.projectile.Projectile) this.entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
