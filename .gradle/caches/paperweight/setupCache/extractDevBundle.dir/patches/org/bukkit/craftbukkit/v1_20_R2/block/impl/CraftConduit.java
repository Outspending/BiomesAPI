/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R2.block.impl;

public final class CraftConduit extends org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData implements org.bukkit.block.data.Waterlogged {

    public CraftConduit() {
        super();
    }

    public CraftConduit(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_20_R2.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.ConduitBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftConduit.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftConduit.WATERLOGGED, waterlogged);
    }
}
