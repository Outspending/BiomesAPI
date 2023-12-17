package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R2.CraftParticle;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

    public CraftAreaEffectCloud(CraftServer server, net.minecraft.world.entity.AreaEffectCloud entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.AreaEffectCloud getHandle() {
        return (net.minecraft.world.entity.AreaEffectCloud) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAreaEffectCloud";
    }

    @Override
    public int getDuration() {
        return this.getHandle().getDuration();
    }

    @Override
    public void setDuration(int duration) {
        this.getHandle().setDuration(duration);
    }

    @Override
    public int getWaitTime() {
        return this.getHandle().waitTime;
    }

    @Override
    public void setWaitTime(int waitTime) {
        this.getHandle().setWaitTime(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return this.getHandle().reapplicationDelay;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        this.getHandle().reapplicationDelay = delay;
    }

    @Override
    public int getDurationOnUse() {
        return this.getHandle().durationOnUse;
    }

    @Override
    public void setDurationOnUse(int duration) {
        this.getHandle().durationOnUse = duration;
    }

    @Override
    public float getRadius() {
        return this.getHandle().getRadius();
    }

    @Override
    public void setRadius(float radius) {
        this.getHandle().setRadius(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return this.getHandle().radiusOnUse;
    }

    @Override
    public void setRadiusOnUse(float radius) {
        this.getHandle().setRadiusOnUse(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return this.getHandle().radiusPerTick;
    }

    @Override
    public void setRadiusPerTick(float radius) {
        this.getHandle().setRadiusPerTick(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.minecraftToBukkit(this.getHandle().getParticle().getType());
    }

    @Override
    public void setParticle(Particle particle) {
        this.setParticle(particle, null);
    }

    @Override
    public <T> void setParticle(Particle particle, T data) {
        particle = CraftParticle.convertLegacy(particle);
        data = CraftParticle.convertLegacy(data);
        if (data != null) {
            Preconditions.checkArgument(particle.getDataType().isInstance(data), "data (%s) should be %s", data.getClass(), particle.getDataType());
        }
        this.getHandle().setParticle(CraftParticle.createParticleParam(particle, data));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.getHandle().getColor());
    }

    @Override
    public void setColor(Color color) {
        this.getHandle().setFixedColor(color.asRGB());
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

        this.getHandle().setPotion(CraftPotionType.bukkitToMinecraft(CraftPotionUtil.fromBukkit(data)));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(CraftPotionType.minecraftToBukkit(this.getHandle().getPotion()));
    }

    @Override
    public void setBasePotionType(@NotNull PotionType potionType) {
        // TODO: 10/6/23 Change PotionType.UNCRAFTABLE to PotionType.EMPTY in error message
        Preconditions.checkArgument(potionType != null, "PotionType cannot be null use PotionType.UNCRAFTABLE to represent no effect instead.");

        this.getHandle().setPotion(CraftPotionType.bukkitToMinecraft(potionType));
    }

    @NotNull
    @Override
    public PotionType getBasePotionType() {
        return CraftPotionType.minecraftToBukkit(this.getHandle().getPotion());
    }

    @Override
    public ProjectileSource getSource() {
        net.minecraft.world.entity.LivingEntity source = this.getHandle().getOwner();
        return (source == null) ? null : (LivingEntity) source.getBukkitEntity();
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity craftLivingEntity) {
            this.getHandle().setOwner(craftLivingEntity.getHandle());
        } else {
            this.getHandle().setOwner(null);
        }
    }

    // Paper start - owner API
    @Override
    public java.util.UUID getOwnerUniqueId() {
        return this.getHandle().ownerUUID;
    }

    @Override
    public void setOwnerUniqueId(final java.util.UUID ownerUuid) {
        this.getHandle().setOwner(null);
        this.getHandle().ownerUUID = ownerUuid;
    }
    // Paper end
}
