package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_20_R2.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    private static final Set<Material> SKULL_MATERIALS = Sets.newHashSet(
            Material.CREEPER_HEAD,
            Material.CREEPER_WALL_HEAD,
            Material.DRAGON_HEAD,
            Material.DRAGON_WALL_HEAD,
            Material.PIGLIN_HEAD,
            Material.PIGLIN_WALL_HEAD,
            Material.PLAYER_HEAD,
            Material.PLAYER_WALL_HEAD,
            Material.SKELETON_SKULL,
            Material.SKELETON_WALL_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL,
            Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD
    );

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    static final ItemMetaKey NOTE_BLOCK_SOUND = new ItemMetaKey("note_block_sound");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;
    private CompoundTag serializedProfile;
    private ResourceLocation noteBlockSound;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.setProfile(skullMeta.profile);
        this.noteBlockSound = skullMeta.noteBlockSound;
    }

    CraftMetaSkull(CompoundTag tag) {
        super(tag);

        try { // Paper - Ignore invalid game profiles
        if (tag.contains(CraftMetaSkull.SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.setProfile(NbtUtils.readGameProfile(tag.getCompound(CraftMetaSkull.SKULL_OWNER.NBT)));
        } else if (tag.contains(CraftMetaSkull.SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_STRING) && !tag.getString(CraftMetaSkull.SKULL_OWNER.NBT).isEmpty()) {
            this.setProfile(new GameProfile(Util.NIL_UUID, tag.getString(CraftMetaSkull.SKULL_OWNER.NBT)));
        }
        } catch (Exception ignored) {} // Paper

        if (tag.contains(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            CompoundTag nbtTagCompound = tag.getCompound(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT).copy();
            if (nbtTagCompound.contains(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT, 8)) {
                this.noteBlockSound = ResourceLocation.tryParse(nbtTagCompound.getString(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT));
            }
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (this.profile == null) {
            Object object = map.get(CraftMetaSkull.SKULL_OWNER.BUKKIT);
            if (object instanceof PlayerProfile) {
                this.setOwnerProfile((PlayerProfile) object);
            } else {
                this.setOwner(SerializableMeta.getString(map, CraftMetaSkull.SKULL_OWNER.BUKKIT, true));
            }
        }

        if (this.noteBlockSound == null) {
            Object object = map.get(CraftMetaSkull.NOTE_BLOCK_SOUND.BUKKIT);
            if (object != null) {
                this.setNoteBlockSound(NamespacedKey.fromString(object.toString()));
            }
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaSkull.SKULL_PROFILE.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            CompoundTag skullTag = tag.getCompound(CraftMetaSkull.SKULL_PROFILE.NBT);
            // convert type of stored Id from String to UUID for backwards compatibility
            if (skullTag.contains("Id", CraftMagicNumbers.NBT.TAG_STRING)) {
                UUID uuid = UUID.fromString(skullTag.getString("Id"));
                skullTag.putUUID("Id", uuid);
            }

            this.setProfile(NbtUtils.readGameProfile(skullTag));
        }

        if (tag.contains(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            CompoundTag nbtTagCompound = tag.getCompound(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT).copy();
            if (nbtTagCompound.contains(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT, 8)) {
                this.noteBlockSound = ResourceLocation.tryParse(nbtTagCompound.getString(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT));
            }
        }
    }

    private void setProfile(GameProfile profile) {
        this.profile = profile;
        this.serializedProfile = (profile == null) ? null : NbtUtils.writeGameProfile(new CompoundTag(), profile);
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (this.profile != null) {
            this.checkForInconsistency();

            // SPIGOT-6558: Set initial textures
            tag.put(CraftMetaSkull.SKULL_OWNER.NBT, this.serializedProfile);
            // Fill in textures
            PlayerProfile ownerProfile = new CraftPlayerProfile(this.profile); // getOwnerProfile may return null
            if (ownerProfile.getTextures().isEmpty()) {
                ownerProfile.update().thenAccept((filledProfile) -> {
                    this.setOwnerProfile(filledProfile);
                    tag.put(CraftMetaSkull.SKULL_OWNER.NBT, this.serializedProfile);
                });
            }
        }

        if (this.noteBlockSound != null) {
            CompoundTag nbtTagCompound = new CompoundTag();
            nbtTagCompound.putString(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT, this.noteBlockSound.toString());
            tag.put(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT, nbtTagCompound);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return this.profile == null && this.noteBlockSound == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return CraftMetaSkull.SKULL_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    @Override
    public boolean hasOwner() {
        return this.profile != null && !this.profile.getName().isEmpty();
    }

    @Override
    public String getOwner() {
        return this.hasOwner() ? this.profile.getName() : null;
    }

    // Paper start
    @Override
    public void setPlayerProfile(@org.jetbrains.annotations.Nullable com.destroystokyo.paper.profile.PlayerProfile profile) {
        setProfile((profile == null) ? null : com.destroystokyo.paper.profile.CraftPlayerProfile.asAuthlibCopy(profile));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return profile != null ? com.destroystokyo.paper.profile.CraftPlayerProfile.asBukkitCopy(profile) : null;
    }
    // Paper end

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (this.hasOwner()) {
            if (!this.profile.getId().equals(Util.NIL_UUID)) {
                return Bukkit.getOfflinePlayer(this.profile.getId());
            }

            if (!this.profile.getName().isEmpty()) {
                return Bukkit.getOfflinePlayer(this.profile.getName());
            }
        }

        return null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name != null && name.length() > CraftMetaSkull.MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            this.setProfile(null);
        } else {
            // Paper start - Use Online Players Skull
            GameProfile newProfile = null;
            net.minecraft.server.level.ServerPlayer player = net.minecraft.server.MinecraftServer.getServer().getPlayerList().getPlayerByName(name);
            if (player != null) newProfile = player.getGameProfile();
            if (newProfile == null) newProfile = new GameProfile(Util.NIL_UUID, name);
            this.setProfile(newProfile);
            // Paper end
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            this.setProfile(null);
        } else if (owner instanceof CraftPlayer) {
            this.setProfile(((CraftPlayer) owner).getProfile());
        } else {
            this.setProfile(new GameProfile(owner.getUniqueId(), owner.getName()));
        }

        return true;
    }

    @Override
    @Deprecated // Paper
    public PlayerProfile getOwnerProfile() {
        if (!this.hasOwner()) {
            return null;
        }

        return new CraftPlayerProfile(this.profile);
    }

    @Override
    @Deprecated // Paper
    public void setOwnerProfile(PlayerProfile profile) {
        if (profile == null) {
            this.setProfile(null);
        } else {
            this.setProfile(CraftPlayerProfile.validateSkullProfile(((com.destroystokyo.paper.profile.SharedPlayerProfile) profile).buildGameProfile())); // Paper
        }
    }

    @Override
    public void setNoteBlockSound(NamespacedKey noteBlockSound) {
        if (noteBlockSound == null) {
            this.noteBlockSound = null;
        } else {
            this.noteBlockSound = CraftNamespacedKey.toMinecraft(noteBlockSound);
        }
    }

    @Override
    public NamespacedKey getNoteBlockSound() {
        return (this.noteBlockSound == null) ? null : CraftNamespacedKey.fromMinecraft(this.noteBlockSound);
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasOwner()) {
            hash = 61 * hash + this.profile.hashCode();
        }
        if (this.noteBlockSound != null) {
            hash = 61 * hash + this.noteBlockSound.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            this.checkForInconsistency();
            // SPIGOT-5403: equals does not check properties
            return (this.profile != null ? that.profile != null && this.serializedProfile.equals(that.serializedProfile) : that.profile == null) && Objects.equals(this.noteBlockSound, that.noteBlockSound);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || this.isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.profile != null) {
            return builder.put(CraftMetaSkull.SKULL_OWNER.BUKKIT, new com.destroystokyo.paper.profile.CraftPlayerProfile(this.profile)); // Paper
        }
        NamespacedKey namespacedKeyNB = this.getNoteBlockSound();
        if (namespacedKeyNB != null) {
            return builder.put(CraftMetaSkull.NOTE_BLOCK_SOUND.BUKKIT, namespacedKeyNB.toString());
        }
        return builder;
    }

    private void checkForInconsistency() {
        if (this.profile != null && this.serializedProfile == null) {
            // SPIGOT-7510: Fix broken reflection usage from plugins
            Bukkit.getLogger().warning("""
                    Found inconsistent skull meta, this should normally not happen and is not a Bukkit / Spigot issue, but one from a plugin you are using.
                    Bukkit will attempt to fix it this time for you, but may not be able to do this every time.
                    If you see this message after typing a command from a plugin, please report this to the plugin developer, they should use the api instead of relying on reflection (and doing it the wrong way).""");
            this.serializedProfile = NbtUtils.writeGameProfile(new CompoundTag(), this.profile);
        }
    }
}
