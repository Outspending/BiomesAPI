package org.bukkit.craftbukkit.v1_20_R2.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    public CraftSaddledInventory(Container inventory) {
        super(inventory);
    }

}
