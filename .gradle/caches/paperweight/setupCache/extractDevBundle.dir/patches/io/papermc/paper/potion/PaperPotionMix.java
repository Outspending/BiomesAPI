package io.papermc.paper.potion;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftRecipe;
import org.bukkit.inventory.RecipeChoice;

public record PaperPotionMix(ItemStack result, Predicate<ItemStack> input, Predicate<ItemStack> ingredient) {

    public PaperPotionMix(PotionMix potionMix) {
        this(CraftItemStack.asNMSCopy(potionMix.getResult()), convert(potionMix.getInput()), convert(potionMix.getIngredient()));
    }

    static Predicate<ItemStack> convert(final RecipeChoice choice) {
        if (choice instanceof PredicateRecipeChoice predicateRecipeChoice) {
            return stack -> predicateRecipeChoice.test(CraftItemStack.asBukkitCopy(stack));
        }
        return CraftRecipe.toIngredient(choice, true);
    }
}
