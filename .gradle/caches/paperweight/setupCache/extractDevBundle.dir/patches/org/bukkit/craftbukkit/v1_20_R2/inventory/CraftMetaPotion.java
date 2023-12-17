package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaPotion extends CraftMetaItem implements PotionMeta {

    private static final Set<Material> POTION_MATERIALS = Sets.newHashSet(
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION,
            Material.TIPPED_ARROW
    );

    static final ItemMetaKey AMPLIFIER = new ItemMetaKey("amplifier", "amplifier");
    static final ItemMetaKey AMBIENT = new ItemMetaKey("ambient", "ambient");
    static final ItemMetaKey DURATION = new ItemMetaKey("duration", "duration");
    static final ItemMetaKey SHOW_PARTICLES = new ItemMetaKey("show_particles", "has-particles");
    static final ItemMetaKey SHOW_ICON = new ItemMetaKey("show_icon", "has-icon");
    static final ItemMetaKey POTION_EFFECTS = new ItemMetaKey("custom_potion_effects", "custom-effects");
    static final ItemMetaKey POTION_COLOR = new ItemMetaKey("CustomPotionColor", "custom-color");
    static final ItemMetaKey ID = new ItemMetaKey("id", "potion-id");
    static final ItemMetaKey DEFAULT_POTION = new ItemMetaKey("Potion", "potion-type");

    // Having an initial "state" in ItemMeta seems bit dirty but the UNCRAFTABLE potion type
    // is treated as the empty form of the meta because it represents an empty potion with no effect
    private PotionType type = PotionType.UNCRAFTABLE;
    private List<PotionEffect> customEffects;
    private Color color;

    CraftMetaPotion(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaPotion potionMeta)) {
            return;
        }
        this.type = potionMeta.type;
        this.color = potionMeta.color;
        if (potionMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<>(potionMeta.customEffects);
        }
    }

    CraftMetaPotion(CompoundTag tag) {
        super(tag);
        if (tag.contains(CraftMetaPotion.DEFAULT_POTION.NBT)) {
            this.type = CraftPotionType.stringToBukkit(tag.getString(CraftMetaPotion.DEFAULT_POTION.NBT));
            if (this.type == null) {
                this.type = PotionType.UNCRAFTABLE;
            }
        }
        if (tag.contains(CraftMetaPotion.POTION_COLOR.NBT)) {
            try {
                this.color = Color.fromRGB(tag.getInt(CraftMetaPotion.POTION_COLOR.NBT));
            } catch (IllegalArgumentException ex) {
                // Invalid colour
            }
        }
        if (tag.contains(CraftMetaPotion.POTION_EFFECTS.NBT)) {
            ListTag list = tag.getList(CraftMetaPotion.POTION_EFFECTS.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
            int length = list.size();
            this.customEffects = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                CompoundTag effect = list.getCompound(i);
                PotionEffectType type = PotionEffectType.getByKey(NamespacedKey.fromString(effect.getString(CraftMetaPotion.ID.NBT)));
                // SPIGOT-4047: Vanilla just disregards these
                if (type == null) {
                    continue;
                }

                int amp = effect.getByte(CraftMetaPotion.AMPLIFIER.NBT);
                int duration = effect.getInt(CraftMetaPotion.DURATION.NBT);
                boolean ambient = effect.getBoolean(CraftMetaPotion.AMBIENT.NBT);
                boolean particles = effect.contains(CraftMetaPotion.SHOW_PARTICLES.NBT, CraftMagicNumbers.NBT.TAG_BYTE) ? effect.getBoolean(CraftMetaPotion.SHOW_PARTICLES.NBT) : true;
                boolean icon = effect.contains(CraftMetaPotion.SHOW_ICON.NBT, CraftMagicNumbers.NBT.TAG_BYTE) ? effect.getBoolean(CraftMetaPotion.SHOW_ICON.NBT) : particles;
                this.customEffects.add(new PotionEffect(type, duration, amp, ambient, particles, icon));
            }
        }
    }

    CraftMetaPotion(Map<String, Object> map) {
        super(map);
        String typeString = SerializableMeta.getString(map, CraftMetaPotion.DEFAULT_POTION.BUKKIT, true);
        if (typeString != null) {
            this.type = CraftPotionType.stringToBukkit(typeString);
        }
        if (this.type == null) {
            this.type = PotionType.UNCRAFTABLE;
        }

        Color color = SerializableMeta.getObject(Color.class, map, CraftMetaPotion.POTION_COLOR.BUKKIT, true);
        if (color != null) {
            this.setColor(color);
        }

        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, CraftMetaPotion.POTION_EFFECTS.BUKKIT, true);
        if (rawEffectList == null) {
            return;
        }

        for (Object obj : rawEffectList) {
            Preconditions.checkArgument(obj instanceof PotionEffect, "Object (%s) in effect list is not valid", obj.getClass());
            this.addCustomEffect((PotionEffect) obj, true);
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        tag.putString(CraftMetaPotion.DEFAULT_POTION.NBT, CraftPotionType.bukkitToString(this.type));

        if (this.hasColor()) {
            tag.putInt(CraftMetaPotion.POTION_COLOR.NBT, this.color.asRGB());
        }

        if (this.customEffects != null) {
            ListTag effectList = new ListTag();
            tag.put(CraftMetaPotion.POTION_EFFECTS.NBT, effectList);

            for (PotionEffect effect : this.customEffects) {
                CompoundTag effectData = new CompoundTag();
                effectData.putString(CraftMetaPotion.ID.NBT, effect.getType().getKey().toString());
                effectData.putByte(CraftMetaPotion.AMPLIFIER.NBT, (byte) effect.getAmplifier());
                effectData.putInt(CraftMetaPotion.DURATION.NBT, effect.getDuration());
                effectData.putBoolean(CraftMetaPotion.AMBIENT.NBT, effect.isAmbient());
                effectData.putBoolean(CraftMetaPotion.SHOW_PARTICLES.NBT, effect.hasParticles());
                effectData.putBoolean(CraftMetaPotion.SHOW_ICON.NBT, effect.hasIcon());
                effectList.add(effectData);
            }
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isPotionEmpty();
    }

    boolean isPotionEmpty() {
        return (this.type == PotionType.UNCRAFTABLE) && !(this.hasCustomEffects() || this.hasColor());
    }

    @Override
    boolean applicableTo(Material type) {
        return CraftMetaPotion.POTION_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaPotion clone() {
        CraftMetaPotion clone = (CraftMetaPotion) super.clone();
        clone.type = this.type;
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<>(this.customEffects);
        }
        return clone;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Preconditions.checkArgument(data != null, "PotionData cannot be null");
        this.type = CraftPotionUtil.fromBukkit(data);
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.type);
    }

    @Override
    public void setBasePotionType(@NotNull PotionType potionType) {
        // TODO: 10/6/23 Change PotionType.UNCRAFTABLE to PotionType.EMPTY in error message
        Preconditions.checkArgument(potionType != null, "PotionType cannot be null use PotionType.UNCRAFTABLE to represent no effect instead.");

        this.type = potionType;
    }

    @NotNull
    @Override
    public PotionType getBasePotionType() {
        return this.type;
    }

    @Override
    public boolean hasCustomEffects() {
        return this.customEffects != null;
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        if (this.hasCustomEffects()) {
            return ImmutableList.copyOf(this.customEffects);
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Preconditions.checkArgument(effect != null, "Potion effect cannot be null");

        int index = this.indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = this.customEffects.get(index);
                if (old.getAmplifier() == effect.getAmplifier() && old.getDuration() == effect.getDuration() && old.isAmbient() == effect.isAmbient()) {
                    return false;
                }
                this.customEffects.set(index, effect);
                return true;
            } else {
                return false;
            }
        } else {
            if (this.customEffects == null) {
                this.customEffects = new ArrayList<>();
            }
            this.customEffects.add(effect);
            return true;
        }
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");

        if (!this.hasCustomEffects()) {
            return false;
        }

        boolean changed = false;
        Iterator<PotionEffect> iterator = this.customEffects.iterator();
        while (iterator.hasNext()) {
            PotionEffect effect = iterator.next();
            if (type.equals(effect.getType())) {
                iterator.remove();
                changed = true;
            }
        }
        if (this.customEffects.isEmpty()) {
            this.customEffects = null;
        }
        return changed;
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");
        return this.indexOfEffect(type) != -1;
    }

    @Override
    public boolean setMainEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");
        int index = this.indexOfEffect(type);
        if (index == -1 || index == 0) {
            return false;
        }

        PotionEffect old = this.customEffects.get(0);
        this.customEffects.set(0, this.customEffects.get(index));
        this.customEffects.set(index, old);
        return true;
    }

    private int indexOfEffect(PotionEffectType type) {
        if (!this.hasCustomEffects()) {
            return -1;
        }

        for (int i = 0; i < this.customEffects.size(); i++) {
            if (this.customEffects.get(i).getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean clearCustomEffects() {
        boolean changed = this.hasCustomEffects();
        this.customEffects = null;
        return changed;
    }

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.type != PotionType.UNCRAFTABLE) {
            hash = 73 * hash + this.type.hashCode();
        }
        if (this.hasColor()) {
            hash = 73 * hash + this.color.hashCode();
        }
        if (this.hasCustomEffects()) {
            hash = 73 * hash + this.customEffects.hashCode();
        }
        return original != hash ? CraftMetaPotion.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaPotion) {
            CraftMetaPotion that = (CraftMetaPotion) meta;

            return this.type.equals(that.type)
                    && (this.hasCustomEffects() ? that.hasCustomEffects() && this.customEffects.equals(that.customEffects) : !that.hasCustomEffects())
                    && (this.hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaPotion || this.isPotionEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.type != PotionType.UNCRAFTABLE) {
            builder.put(CraftMetaPotion.DEFAULT_POTION.BUKKIT, CraftPotionType.bukkitToString(this.type));
        }

        if (this.hasColor()) {
            builder.put(CraftMetaPotion.POTION_COLOR.BUKKIT, this.getColor());
        }

        if (this.hasCustomEffects()) {
            builder.put(CraftMetaPotion.POTION_EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }

        return builder;
    }
}
