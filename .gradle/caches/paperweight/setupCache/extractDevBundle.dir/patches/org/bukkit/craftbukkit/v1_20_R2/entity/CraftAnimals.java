package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.world.entity.animal.Animal;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Animals;
import org.bukkit.inventory.ItemStack;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, Animal entity) {
        super(server, entity);
    }

    @Override
    public Animal getHandle() {
        return (Animal) this.entity;
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }

    @Override
    public UUID getBreedCause() {
        return this.getHandle().loveCause;
    }

    @Override
    public void setBreedCause(UUID uuid) {
        this.getHandle().loveCause = uuid;
    }

    @Override
    public boolean isLoveMode() {
        return this.getHandle().isInLove();
    }

    @Override
    public void setLoveModeTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Love mode ticks must be positive or 0");
        this.getHandle().setInLoveTime(ticks);
    }

    @Override
    public int getLoveModeTicks() {
        return this.getHandle().inLove;
    }

    @Override
    public boolean isBreedItem(ItemStack itemStack) {
        return this.getHandle().isFood(CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public boolean isBreedItem(Material material) {
        return this.isBreedItem(new ItemStack(material));
    }
}
