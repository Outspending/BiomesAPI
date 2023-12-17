package org.bukkit.craftbukkit.v1_20_R2.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.ChestBoat;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

public class CraftChestBoat extends CraftBoat implements org.bukkit.entity.ChestBoat, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper
    private final Inventory inventory;

    public CraftChestBoat(CraftServer server, ChestBoat entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public ChestBoat getHandle() {
        return (ChestBoat) this.entity;
    }

    @Override
    public String toString() {
        return "CraftChestBoat";
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void setLootTable(LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public LootTable getLootTable() {
        ResourceLocation nmsTable = this.getHandle().getLootTable();
        if (nmsTable == null) {
            return null; // return empty loot table?
        }

        NamespacedKey key = CraftNamespacedKey.fromMinecraft(nmsTable);
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return this.getHandle().getLootTableSeed();
    }

    public void setLootTable(LootTable table, long seed) { // Paper - change visibility since it overrides a public method
        ResourceLocation newKey = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
        this.getHandle().setLootTable(newKey);
        this.getHandle().setLootTableSeed(seed);
    }
}
