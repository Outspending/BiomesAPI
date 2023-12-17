package org.bukkit.craftbukkit.v1_20_R2.inventory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTrimRecipe;

public class CraftSmithingTrimRecipe extends SmithingTrimRecipe implements CraftRecipe {

    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        super(key, template, base, addition);
    }
    // Paper start
    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition, boolean copyNbt) {
        super(key, template, base, addition, copyNbt);
    }
    // Paper end

    public static CraftSmithingTrimRecipe fromBukkitRecipe(SmithingTrimRecipe recipe) {
        if (recipe instanceof CraftSmithingTrimRecipe) {
            return (CraftSmithingTrimRecipe) recipe;
        }
        CraftSmithingTrimRecipe ret = new CraftSmithingTrimRecipe(recipe.getKey(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition(), recipe.willCopyNbt()); // Paper
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toMinecraft(this.getKey()), new net.minecraft.world.item.crafting.SmithingTrimRecipe(this.toNMS(this.getTemplate(), true), this.toNMS(this.getBase(), true), this.toNMS(this.getAddition(), true), this.willCopyNbt()))); // Paper
    }
}
