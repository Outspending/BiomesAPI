package org.bukkit.craftbukkit.v1_20_R2.entity;

import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Drowned;

public class CraftDrowned extends CraftZombie implements Drowned, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.Drowned> { // Paper

    public CraftDrowned(CraftServer server, net.minecraft.world.entity.monster.Drowned entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Drowned getHandle() {
        return (net.minecraft.world.entity.monster.Drowned) this.entity;
    }

    @Override
    public String toString() {
        return "CraftDrowned";
    }
}
