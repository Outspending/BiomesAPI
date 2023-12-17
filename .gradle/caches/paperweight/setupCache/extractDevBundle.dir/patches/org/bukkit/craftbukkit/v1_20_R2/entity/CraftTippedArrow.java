package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class CraftTippedArrow extends CraftArrow implements Arrow {

    public CraftTippedArrow(CraftServer server, net.minecraft.world.entity.projectile.Arrow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.Arrow getHandle() {
        return (net.minecraft.world.entity.projectile.Arrow) this.entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        MobEffect minecraft = CraftPotionEffectType.bukkitToMinecraft(effect.getType());
        MobEffectInstance existing = null;
        for (MobEffectInstance mobEffect : this.getHandle().effects) {
            if (mobEffect.getEffect() == minecraft) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            this.getHandle().effects.remove(existing);
        }
        this.getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        this.getHandle().updateColor();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        this.getHandle().effects.clear();
        this.getHandle().updateColor();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffectInstance effect : this.getHandle().effects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (MobEffectInstance effect : this.getHandle().effects) {
            if (CraftPotionUtil.equals(effect.getEffect(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !this.getHandle().effects.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        MobEffect minecraft = CraftPotionEffectType.bukkitToMinecraft(effect);
        MobEffectInstance existing = null;
        for (MobEffectInstance mobEffect : this.getHandle().effects) {
            if (mobEffect.getEffect() == minecraft) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        this.getHandle().effects.remove(existing);
        this.getHandle().updateColor();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Preconditions.checkArgument(data != null, "PotionData cannot be null");
        this.getHandle().potion = CraftPotionType.bukkitToMinecraft(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(CraftPotionType.minecraftToBukkit(this.getHandle().potion));
    }

    @Override
    public void setBasePotionType(@NotNull PotionType potionType) {
        // TODO: 10/6/23 Change PotionType.UNCRAFTABLE to PotionType.EMPTY in error message
        Preconditions.checkArgument(potionType != null, "PotionType cannot be null use PotionType.UNCRAFTABLE to represent no effect instead.");

        this.getHandle().potion = CraftPotionType.bukkitToMinecraft(potionType);
    }

    @NotNull
    @Override
    public PotionType getBasePotionType() {
        return CraftPotionType.minecraftToBukkit(this.getHandle().potion);
    }

    @Override
    public void setColor(Color color) {
        int colorRGB = (color == null) ? -1 : color.asRGB();
        this.getHandle().setFixedColor(colorRGB);
    }

    @Override
    public Color getColor() {
        if (this.getHandle().getColor() <= -1) {
            return null;
        }
        return Color.fromRGB(this.getHandle().getColor());
    }
}
