package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBarrel extends CraftLootable<BarrelBlockEntity> implements Barrel {

    public CraftBarrel(World world, BarrelBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBarrel(CraftBarrel state) {
        super(state);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getTileEntity().openersCounter.opened) {
            BlockState blockData = this.getTileEntity().getBlockState();
            boolean open = blockData.getValue(BarrelBlock.OPEN);

            if (!open) {
                this.getTileEntity().updateBlockState(blockData, true);
                if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                    this.getTileEntity().playSound(blockData, SoundEvents.BARREL_OPEN);
                }
            }
        }
        this.getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getTileEntity().openersCounter.opened) {
            BlockState blockData = this.getTileEntity().getBlockState();
            this.getTileEntity().updateBlockState(blockData, false);
            if (this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
                this.getTileEntity().playSound(blockData, SoundEvents.BARREL_CLOSE);
            }
        }
        this.getTileEntity().openersCounter.opened = false;
    }

    @Override
    public CraftBarrel copy() {
        return new CraftBarrel(this);
    }

    // Paper start - More Lidded Block API
    @Override
    public boolean isOpen() {
        return getTileEntity().openersCounter.opened;
    }
    // Paper end - More Lidded Block API
}
