package org.bukkit.craftbukkit.v1_20_R2.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftSaddledInventory implements HorseInventory {

    public CraftInventoryHorse(Container inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getArmor() {
       return this.getItem(1);
    }

    @Override
    public void setArmor(ItemStack stack) {
        this.setItem(1, stack);
    }
}
