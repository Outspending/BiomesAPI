package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.MushroomCow.Variant;

public class CraftMushroomCow extends CraftCow implements MushroomCow, io.papermc.paper.entity.PaperShearable { // Paper
    public CraftMushroomCow(CraftServer server, net.minecraft.world.entity.animal.MushroomCow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.MushroomCow getHandle() {
        return (net.minecraft.world.entity.animal.MushroomCow) this.entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.MushroomCow.MushroomType.values()[variant.ordinal()]);
    }

    // Paper start
    @Override
    public java.util.List<io.papermc.paper.potion.SuspiciousEffectEntry> getStewEffects() {
        if (this.getHandle().stewEffects == null) {
            return java.util.List.of();
        }

        java.util.List<io.papermc.paper.potion.SuspiciousEffectEntry> nmsPairs = new java.util.ArrayList<>(this.getHandle().stewEffects.size());
        for (final net.minecraft.world.level.block.SuspiciousEffectHolder.EffectEntry effect : this.getHandle().stewEffects) {
            nmsPairs.add(io.papermc.paper.potion.SuspiciousEffectEntry.create(
                org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType.minecraftToBukkit(effect.effect()),
                effect.duration()
            ));
        }

        return java.util.Collections.unmodifiableList(nmsPairs);
    }

    @Override
    public void setStewEffects(final java.util.List<io.papermc.paper.potion.SuspiciousEffectEntry> effects) {
        if (effects.isEmpty()) {
            this.getHandle().stewEffects = null;
            return;
        }

        java.util.List<net.minecraft.world.level.block.SuspiciousEffectHolder.EffectEntry> nmsPairs = new java.util.ArrayList<>(effects.size());
        for (final io.papermc.paper.potion.SuspiciousEffectEntry effect : effects) {
            nmsPairs.add(new net.minecraft.world.level.block.SuspiciousEffectHolder.EffectEntry(
                org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType.bukkitToMinecraft(effect.effect()),
                effect.duration()
            ));
        }

        this.getHandle().stewEffects = nmsPairs;
    }
    // Paper end

    @Override
    public String toString() {
        return "CraftMushroomCow";
    }
}
