package com.destroystokyo.paper.loottable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public interface PaperLootableBlockInventory extends LootableBlockInventory, PaperLootableInventory {

    RandomizableContainerBlockEntity getTileEntity();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    @Override
    default Level getNMSWorld() {
        return this.getTileEntity().getLevel();
    }

    default Block getBlock() {
        final BlockPos position = this.getTileEntity().getBlockPos();
        final Chunk bukkitChunk = this.getBukkitWorld().getChunkAt(org.bukkit.craftbukkit.v1_20_R2.block.CraftBlock.at(this.getNMSWorld(), position));
        return bukkitChunk.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    default PaperLootableInventoryData getLootableData() {
        return this.getTileEntity().lootableData;
    }
}
