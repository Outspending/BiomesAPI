package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;
import com.destroystokyo.paper.loottable.PaperLootableBlockInventory; // Paper

public class CraftChest extends CraftLootable<ChestBlockEntity> implements Chest, PaperLootableBlockInventory { // Paper

    public CraftChest(World world, ChestBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftChest(CraftChest state) {
        super(state);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!this.isPlaced() || this.isWorldGeneration()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock blockChest = (ChestBlock) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        MenuProvider nms = blockChest.getMenuProvider(this.data, world.getHandle(), this.getPosition(), true);

        if (nms instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public void open() {
        this.requirePlaced();
        if (!this.getTileEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getTileEntity().getBlockState();
            int openCount = this.getTileEntity().openersCounter.getOpenerCount();

            this.getTileEntity().openersCounter.onAPIOpen((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, openCount + 1);
        }
        this.getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        this.requirePlaced();
        if (this.getTileEntity().openersCounter.opened && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = this.getTileEntity().getBlockState();
            int openCount = this.getTileEntity().openersCounter.getOpenerCount();

            this.getTileEntity().openersCounter.onAPIClose((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block);
            this.getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) this.getWorldHandle(), this.getPosition(), block, openCount, 0);
        }
        this.getTileEntity().openersCounter.opened = false;
    }

    @Override
    public CraftChest copy() {
        return new CraftChest(this);
    }

    // Paper start - More Lidded Block API
    @Override
    public boolean isOpen() {
        return getTileEntity().openersCounter.opened;
    }
    // Paper end - More Lidded Block API
}
