package org.bukkit.craftbukkit.v1_20_R2.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBlock;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

public class CraftBlockInventoryHolder implements BlockInventoryHolder {

    private final Block block;
    private final Inventory inventory;

    public CraftBlockInventoryHolder(LevelAccessor world, BlockPos pos, Container inv) {
        this.block = CraftBlock.at(world, pos);
        this.inventory = new CraftInventory(inv);
    }
    // Paper start
    public CraftBlockInventoryHolder(net.minecraft.world.inventory.ContainerLevelAccess levelAccess, Inventory inventory) {
        com.google.common.base.Preconditions.checkArgument(levelAccess.isBlock());
        this.block = CraftBlock.at(levelAccess.getWorld(), levelAccess.getPosition());
        this.inventory = inventory;
    }
    // Paper end

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
