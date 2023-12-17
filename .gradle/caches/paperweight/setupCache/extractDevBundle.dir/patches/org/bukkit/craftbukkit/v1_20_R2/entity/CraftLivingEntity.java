package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.CraftSound;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.memory.CraftMemoryKey;
import org.bukkit.craftbukkit.v1_20_R2.entity.memory.CraftMemoryMapper;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionUtil;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final net.minecraft.world.entity.LivingEntity entity) {
        super(server, entity);

        if (entity instanceof Mob || entity instanceof ArmorStand) {
            this.equipment = new CraftEntityEquipment(this);
        }
    }

    @Override
    public double getHealth() {
        return Math.min(Math.max(0, this.getHandle().getHealth()), this.getMaxHealth());
    }

    @Override
    public void setHealth(double health) {
        health = (float) health;
        // Paper start - Be more informative
        Preconditions.checkArgument(health >= 0 && health <= this.getMaxHealth(),
            "Health value (%s) must be between 0 and %s. (attribute base value: %s%s)",
            health, this.getMaxHealth(), this.getHandle().getAttribute(Attributes.MAX_HEALTH).getBaseValue(), this instanceof CraftPlayer ? ", player: " + this.getName() : ""
        );
        // Paper end

        // during world generation, we don't want to run logic for dropping items and xp
        if (this.getHandle().generation && health == 0) {
            this.getHandle().discard();
            return;
        }

        this.getHandle().setHealth((float) health);

        if (health == 0) {
            this.getHandle().die(this.getHandle().damageSources().generic());
        }
    }

    @Override
    public double getAbsorptionAmount() {
        return this.getHandle().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        Preconditions.checkArgument(amount >= 0 && Double.isFinite(amount), "amount < 0 or non-finite");

        this.getHandle().setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return this.getHandle().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double amount) {
        Preconditions.checkArgument(amount > 0, "Max health amount (%s) must be greater than 0", amount);

        this.getHandle().getAttribute(Attributes.MAX_HEALTH).setBaseValue(amount);

        if (this.getHealth() > amount) {
            this.setHealth(amount);
        }
    }

    @Override
    public void resetMaxHealth() {
        this.setMaxHealth(this.getHandle().getAttribute(Attributes.MAX_HEALTH).getAttribute().getDefaultValue());
    }

    @Override
    public double getEyeHeight() {
        return this.getHandle().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return this.getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot get line of sight during world generation");

        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (!transparent.contains(material)) {
                break;
            }
        }
        return blocks;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = this.getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    // Paper start
    @Override
    public Block getTargetBlock(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        return this.getTargetBlockExact(maxDistance, fluidMode.bukkit);
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        return this.getTargetBlockFace(maxDistance, fluidMode.bukkit);
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, org.bukkit.FluidCollisionMode fluidMode) {
        RayTraceResult result = this.rayTraceBlocks(maxDistance, fluidMode);
        return result != null ? result.getHitBlockFace() : null;
    }

    @Override
    public com.destroystokyo.paper.block.TargetBlockInfo getTargetBlockInfo(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        RayTraceResult result = this.rayTraceBlocks(maxDistance, fluidMode.bukkit);
        if (result != null && result.getHitBlock() != null && result.getHitBlockFace() != null) {
            return new com.destroystokyo.paper.block.TargetBlockInfo(result.getHitBlock(), result.getHitBlockFace());
        }
        return null;
    }

    public Entity getTargetEntity(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : rayTrace.getEntity().getBukkitEntity();
    }

    public TargetEntityInfo getTargetEntityInfo(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : new TargetEntityInfo(rayTrace.getEntity().getBukkitEntity(), new org.bukkit.util.Vector(rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z));
    }

    @Override
    public RayTraceResult rayTraceEntities(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = this.rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : new org.bukkit.util.RayTraceResult(org.bukkit.craftbukkit.v1_20_R2.util.CraftVector.toBukkit(rayTrace.getLocation()), rayTrace.getEntity().getBukkitEntity());
    }

    public net.minecraft.world.phys.EntityHitResult rayTraceEntity(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = getHandle().getTargetEntity(maxDistance);
        if (rayTrace == null) {
            return null;
        }
        if (!ignoreBlocks) {
            net.minecraft.world.phys.HitResult rayTraceBlocks = getHandle().getRayTrace(maxDistance, net.minecraft.world.level.ClipContext.Fluid.NONE);
            if (rayTraceBlocks != null) {
                net.minecraft.world.phys.Vec3 eye = getHandle().getEyePosition(1.0F);
                if (eye.distanceToSqr(rayTraceBlocks.getLocation()) <= eye.distanceToSqr(rayTrace.getLocation())) {
                    return null;
                }
            }
        }
        return rayTrace;
    }
    // Paper end

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return this.getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        RayTraceResult hitResult = this.rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (hitResult != null ? hitResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return this.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot ray tray blocks during world generation");

        Location eyeLocation = this.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        return this.getWorld().rayTraceBlocks(eyeLocation, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return this.getHandle().getAirSupply();
    }

    @Override
    public void setRemainingAir(int ticks) {
        this.getHandle().setAirSupply(ticks);
    }

    @Override
    public int getMaximumAir() {
        return this.getHandle().maxAirTicks;
    }

    @Override
    public void setMaximumAir(int ticks) {
        this.getHandle().maxAirTicks = ticks;
    }

    @Override
    public int getArrowCooldown() {
        return this.getHandle().removeArrowTime;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        this.getHandle().removeArrowTime = ticks;
    }

    @Override
    public int getArrowsInBody() {
        return this.getHandle().getArrowCount();
    }

    @Override
    public void setArrowsInBody(final int count, final boolean fireEvent) { // Paper
        Preconditions.checkArgument(count >= 0, "New arrow amount must be >= 0");
        if (!fireEvent) { // Paper
        this.getHandle().getEntityData().set(net.minecraft.world.entity.LivingEntity.DATA_ARROW_COUNT_ID, count);
        // Paper start
        } else {
            this.getHandle().setArrowCount(count);
        }
        // Paper end
    }
    // Paper Start - Bee Stinger API
    @Override
    public int getBeeStingerCooldown() {
        return getHandle().removeStingerTime;
    }

    @Override
    public void setBeeStingerCooldown(int ticks) {
        getHandle().removeStingerTime = ticks;
    }

    @Override
    public int getBeeStingersInBody() {
        return getHandle().getStingerCount();
    }

    @Override
    public void setBeeStingersInBody(int count) {
        Preconditions.checkArgument(count >= 0, "New bee stinger amount must be >= 0");
        getHandle().setStingerCount(count);
    }
    // Paper End - Bee Stinger API
    @Override
    public void damage(double amount) {
        this.damage(amount, null);
    }

    @Override
    public void damage(double amount, org.bukkit.entity.Entity source) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot damage entity during world generation");

        DamageSource reason = this.getHandle().damageSources().generic();

        if (source instanceof HumanEntity) {
            reason = this.getHandle().damageSources().playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = this.getHandle().damageSources().mobAttack(((CraftLivingEntity) source).getHandle());
        }

        this.entity.hurt(reason, (float) amount);
    }

    @Override
    public Location getEyeLocation() {
        Location loc = this.getLocation();
        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return this.getHandle().invulnerableDuration;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        this.getHandle().invulnerableDuration = ticks;
    }

    @Override
    public double getLastDamage() {
        return this.getHandle().lastHurt;
    }

    @Override
    public void setLastDamage(double damage) {
        this.getHandle().lastHurt = (float) damage;
    }

    @Override
    public int getNoDamageTicks() {
        return this.getHandle().invulnerableTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        this.getHandle().invulnerableTime = ticks;
    }

    @Override
    public int getNoActionTicks() {
        return this.getHandle().getNoActionTime();
    }

    @Override
    public void setNoActionTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        this.getHandle().setNoActionTime(ticks);
    }

    @Override
    public net.minecraft.world.entity.LivingEntity getHandle() {
        return (net.minecraft.world.entity.LivingEntity) this.entity;
    }

    public void setHandle(final net.minecraft.world.entity.LivingEntity entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + this.getEntityId() + '}';
    }

    @Override
    public Player getKiller() {
        return this.getHandle().lastHurtByPlayer == null ? null : (Player) this.getHandle().lastHurtByPlayer.getBukkitEntity();
    }

    // Paper start
    @Override
    public void setKiller(Player killer) {
        net.minecraft.server.level.ServerPlayer entityPlayer = killer == null ? null : ((CraftPlayer) killer).getHandle();
        getHandle().lastHurtByPlayer = entityPlayer;
        getHandle().lastHurtByMob = entityPlayer;
        getHandle().lastHurtByPlayerTime = entityPlayer == null ? 0 : 100; // 100 value taken from EntityLiving#damageEntity
    }
    // Paper end

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        org.spigotmc.AsyncCatcher.catchOp("effect add"); // Paper
        this.getHandle().addEffect(CraftPotionUtil.fromBukkit(effect), EntityPotionEffectEvent.Cause.PLUGIN); // Paper - Don't ignore icon
        return true;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= this.addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return this.getHandle().hasEffect(CraftPotionEffectType.bukkitToMinecraft(type));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        MobEffectInstance handle = this.getHandle().getEffect(CraftPotionEffectType.bukkitToMinecraft(type));
        return (handle == null) ? null : CraftPotionUtil.toBukkit(handle); // Paper
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        this.getHandle().removeEffect(CraftPotionEffectType.bukkitToMinecraft(type), EntityPotionEffectEvent.Cause.PLUGIN);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (MobEffectInstance handle : this.getHandle().activeEffects.values()) {
            effects.add(CraftPotionUtil.toBukkit(handle)); // Paper
        }
        return effects;
    }

    // Paper start - LivingEntity#clearActivePotionEffects();
    @Override
    public boolean clearActivePotionEffects() {
        return this.getHandle().removeAllEffects(EntityPotionEffectEvent.Cause.PLUGIN);
    }
    // Paper end

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        // Paper start - launchProjectile consumer
        return this.launchProjectile(projectile, velocity, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity, java.util.function.Consumer<? super T> function) {
        // Paper end - launchProjectile consumer
        Preconditions.checkState(!this.getHandle().generation, "Cannot launch projectile during world generation");

        net.minecraft.world.level.Level world = ((CraftWorld) this.getWorld()).getHandle();
        net.minecraft.world.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.world.entity.projectile.Snowball(world, this.getHandle());
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemSnowball
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new ThrownEgg(world, this.getHandle());
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEgg
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new ThrownEnderpearl(world, this.getHandle());
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEnderPearl
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.Arrow(world, this.getHandle());
                ((Arrow) launch.getBukkitEntity()).setBasePotionData(new PotionData(PotionType.WATER, false, false));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.SpectralArrow(world, this.getHandle());
            } else if (Trident.class.isAssignableFrom(projectile)) {
                launch = new ThrownTrident(world, this.getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.TRIDENT));
            } else {
                launch = new net.minecraft.world.entity.projectile.Arrow(world, this.getHandle());
            }
            ((net.minecraft.world.entity.projectile.AbstractArrow) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 3.0F, 1.0F); // ItemBow
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.ThrownPotion(world, this.getHandle());
                ((net.minecraft.world.entity.projectile.ThrownPotion) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new net.minecraft.world.entity.projectile.ThrownPotion(world, this.getHandle());
                ((net.minecraft.world.entity.projectile.ThrownPotion) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), -20.0F, 0.5F, 1.0F); // ItemSplashPotion
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ThrownExperienceBottle(world, this.getHandle());
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), -20.0F, 0.7F, 1.0F); // ItemExpBottle
        } else if (FishHook.class.isAssignableFrom(projectile) && this.getHandle() instanceof net.minecraft.world.entity.player.Player) {
            launch = new FishingHook((net.minecraft.world.entity.player.Player) this.getHandle(), world, 0, 0);
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.SmallFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.WitherSkull(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.DragonFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new LargeFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ(), 1);
            }

            ((AbstractHurtingProjectile) launch).projectileSource = this;
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection();

            launch = EntityType.LLAMA_SPIT.create(world);

            ((net.minecraft.world.entity.projectile.LlamaSpit) launch).setOwner(this.getHandle());
            ((net.minecraft.world.entity.projectile.LlamaSpit) launch).shoot(direction.getX(), direction.getY(), direction.getZ(), 1.5F, 10.0F); // EntityLlama
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();

            launch = new net.minecraft.world.entity.projectile.ShulkerBullet(world, this.getHandle(), null, null);
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (Firework.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();

            launch = new FireworkRocketEntity(world, net.minecraft.world.item.ItemStack.EMPTY, this.getHandle());
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Preconditions.checkArgument(launch != null, "Projectile (%s) not supported", projectile.getName());

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }
        // Paper start - launchProjectile consumer
        if (function != null) {
            function.accept((T) launch.getBukkitEntity());
        }
        // Paper end - launchProjectile consumer

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot check line of sight during world generation");

        return this.getHandle().hasLineOfSight(((CraftEntity) other).getHandle());
    }

    // Paper start
    @Override
    public boolean hasLineOfSight(Location loc) {
        if (this.getHandle().level() != ((CraftWorld) loc.getWorld()).getHandle()) {
            return false;
        }

        net.minecraft.world.phys.Vec3 start = new net.minecraft.world.phys.Vec3(this.getHandle().getX(), this.getHandle().getEyeY(), this.getHandle().getZ());
        net.minecraft.world.phys.Vec3 end = new net.minecraft.world.phys.Vec3(loc.getX(), loc.getY(), loc.getZ());
        if (end.distanceToSqr(start) > 128D * 128D) {
            return false; // Return early if the distance is greater than 128 blocks
        }

        return this.getHandle().level().clipDirect(start, end, net.minecraft.world.phys.shapes.CollisionContext.of(this.getHandle())) == net.minecraft.world.phys.HitResult.Type.MISS;
    }
    // Paper end

    @Override
    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof Mob && !((Mob) this.getHandle()).isPersistenceRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setPersistenceRequired(!remove);
        }
    }

    @Override
    public EntityEquipment getEquipment() {
        return this.equipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setCanPickUpLoot(pickup);
        } else {
            this.getHandle().bukkitPickUpLoot = pickup;
        }
    }

    @Override
    public boolean getCanPickupItems() {
        if (this.getHandle() instanceof Mob) {
            return ((Mob) this.getHandle()).canPickUpLoot();
        } else {
            return this.getHandle().bukkitPickUpLoot;
        }
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (this.getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    @Override
    public boolean isLeashed() {
        if (!(this.getHandle() instanceof Mob)) {
            return false;
        }
        return ((Mob) this.getHandle()).getLeashHolder() != null;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        Preconditions.checkState(this.isLeashed(), "Entity not leashed");
        return ((Mob) this.getHandle()).getLeashHolder().getBukkitEntity();
    }

    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        }
        ((Mob) this.getHandle()).dropLeash(true, false);
        return true;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        if (this.getHandle().generation || (this.getHandle() instanceof WitherBoss) || !(this.getHandle() instanceof Mob)) {
            return false;
        }

        if (holder == null) {
            return this.unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        this.unleash();
        ((Mob) this.getHandle()).setLeashedTo(((CraftEntity) holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return this.getHandle().getSharedFlag(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        this.getHandle().setSharedFlag(7, gliding);
    }

    @Override
    public boolean isSwimming() {
        return this.getHandle().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.getHandle().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return this.getHandle().isAutoSpinAttack();
    }

    @Override
    public boolean isSleeping() {
        return this.getHandle().isSleeping();
    }

    @Override
    public boolean isClimbing() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot check if climbing during world generation");

        return this.getHandle().onClimbable();
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return this.getHandle().craftAttributes.getAttribute(attribute);
    }

    // Paper start
    @Override
    public void registerAttribute(Attribute attribute) {
        getHandle().craftAttributes.registerAttribute(attribute);
    }
    // Paper end

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setNoAi(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (this.getHandle() instanceof Mob) ? !((Mob) this.getHandle()).isNoAi() : false;
    }

    @Override
    public void attack(Entity target) {
        Preconditions.checkArgument(target != null, "target == null");
        Preconditions.checkState(!this.getHandle().generation, "Cannot attack during world generation");

        if (this.getHandle() instanceof net.minecraft.world.entity.player.Player) {
            ((net.minecraft.world.entity.player.Player) this.getHandle()).attack(((CraftEntity) target).getHandle());
        } else {
            this.getHandle().doHurtTarget(((CraftEntity) target).getHandle());
        }
    }

    @Override
    public void swingMainHand() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot swing hand during world generation");

        this.getHandle().swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    public void swingOffHand() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot swing hand during world generation");

        this.getHandle().swing(InteractionHand.OFF_HAND, true);
    }

    @Override
    public void playHurtAnimation(float yaw) {
        if (this.getHandle().level() instanceof ServerLevel world) {
            /*
             * Vanilla degrees state that 0 = left, 90 = front, 180 = right, and 270 = behind.
             * This makes no sense. We'll add 90 to it so that 0 = front, clockwise from there.
             */
            float actualYaw = yaw + 90;
            ClientboundHurtAnimationPacket packet = new ClientboundHurtAnimationPacket(this.getEntityId(), actualYaw);

            world.getChunkSource().broadcastAndSend(this.getHandle(), packet);
        }
    }

    @Override
    public void setCollidable(boolean collidable) {
        this.getHandle().collides = collidable;
    }

    @Override
    public boolean isCollidable() {
        return this.getHandle().collides;
    }

    @Override
    public Set<UUID> getCollidableExemptions() {
        return this.getHandle().collidableExemptions;
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return (T) this.getHandle().getBrain().getMemory(CraftMemoryKey.bukkitToMinecraft(memoryKey)).map(CraftMemoryMapper::fromNms).orElse(null);
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        this.getHandle().getBrain().setMemory(CraftMemoryKey.bukkitToMinecraft(memoryKey), CraftMemoryMapper.toNms(t));
    }

    @Override
    public Sound getHurtSound() {
        SoundEvent sound = this.getHandle().getHurtSound0(this.getHandle().damageSources().generic());
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public Sound getDeathSound() {
        SoundEvent sound = this.getHandle().getDeathSound0();
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public Sound getFallDamageSound(int fallHeight) {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallDamageSound0(fallHeight));
    }

    @Override
    public Sound getFallDamageSoundSmall() {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallSounds().small());
    }

    @Override
    public Sound getFallDamageSoundBig() {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallSounds().big());
    }

    @Override
    public Sound getDrinkingSound(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack must not be null");
        return CraftSound.minecraftToBukkit(this.getHandle().getDrinkingSound0(CraftItemStack.asNMSCopy(itemStack)));
    }

    @Override
    public Sound getEatingSound(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack must not be null");
        return CraftSound.minecraftToBukkit(this.getHandle().getEatingSound0(CraftItemStack.asNMSCopy(itemStack)));
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.getHandle().canBreatheUnderwater();
    }

    @Override
    public EntityCategory getCategory() {
        MobType type = this.getHandle().getMobType(); // Not actually an enum?

        if (type == MobType.UNDEFINED) {
            return EntityCategory.NONE;
        } else if (type == MobType.UNDEAD) {
            return EntityCategory.UNDEAD;
        } else if (type == MobType.ARTHROPOD) {
            return EntityCategory.ARTHROPOD;
        } else if (type == MobType.ILLAGER) {
            return EntityCategory.ILLAGER;
        } else if (type == MobType.WATER) {
            return EntityCategory.WATER;
        }

        throw new UnsupportedOperationException("Unsupported monster type: " + type + ". This is a bug, report this to Spigot.");
    }

    @Override
    public boolean isInvisible() {
        return this.getHandle().isInvisible();
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.getHandle().persistentInvisibility = invisible;
        this.getHandle().setSharedFlag(5, invisible);
    }
    // Paper start
    @Override
    public float getSidewaysMovement() {
        return this.getHandle().xxa;
    }

    @Override
    public float getForwardsMovement() {
        return this.getHandle().zza;
    }

    @Override
    public float getUpwardsMovement() {
        return this.getHandle().yya;
    }
    // Paper end

    // Paper start
    @Override
    public int getArrowsStuck() {
        return this.getHandle().getArrowCount();
    }

    @Override
    public void setArrowsStuck(final int arrows) {
        this.getHandle().setArrowCount(arrows);
    }

    @Override
    public int getShieldBlockingDelay() {
        return getHandle().getShieldBlockingDelay();
    }

    @Override
    public void setShieldBlockingDelay(int delay) {
        getHandle().setShieldBlockingDelay(delay);
    }

    @Override
    public ItemStack getActiveItem() {
        return getHandle().getUseItem().asBukkitMirror();
    }

    // Paper start
    @Override
    public void clearActiveItem() {
        getHandle().stopUsingItem();
    }
    // Paper end

    @Override
    public int getItemUseRemainingTime() {
        return getHandle().getUseItemRemainingTicks();
    }

    @Override
    public int getHandRaisedTime() {
        return getHandle().getTicksUsingItem();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().isUsingItem();
    }

    @Override
    public org.bukkit.inventory.EquipmentSlot getHandRaised() {
        return getHandle().getUsedItemHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? org.bukkit.inventory.EquipmentSlot.HAND : org.bukkit.inventory.EquipmentSlot.OFF_HAND;
    }

    @Override
    public boolean isJumping() {
        return getHandle().jumping;
    }

    @Override
    public void setJumping(boolean jumping) {
        getHandle().setJumping(jumping);
        if (jumping && getHandle() instanceof Mob) {
            // this is needed to actually make a mob jump
            ((Mob) getHandle()).getJumpControl().jump();
        }
    }

    @Override
    public void playPickupItemAnimation(org.bukkit.entity.Item item, int quantity) {
        getHandle().take(((CraftItem) item).getHandle(), quantity);
    }

    @Override
    public float getHurtDirection() {
        return this.getHandle().getHurtDir();
    }

    @Override
    public void setHurtDirection(float hurtDirection) {
        throw new UnsupportedOperationException("Cannot set the hurt direction on a non player");
    }

    public static MobType fromBukkitEntityCategory(EntityCategory entityCategory) {
        switch (entityCategory) {
            case NONE:
                return MobType.UNDEFINED;
            case UNDEAD:
                return MobType.UNDEAD;
            case ARTHROPOD:
                return MobType.ARTHROPOD;
            case ILLAGER:
                return MobType.ILLAGER;
            case WATER:
                return MobType.WATER;
        }
        throw new IllegalArgumentException(entityCategory + " is an unrecognized entity category");
    }

    @Override
    public void broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot slot) {
        this.getHandle().broadcastBreakEvent(org.bukkit.craftbukkit.v1_20_R2.CraftEquipmentSlot.getNMS(slot));
    }

    @Override
    public void broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot slot, Collection<org.bukkit.entity.Player> players) {
        if (players.isEmpty()) {
            return;
        }
        final net.minecraft.network.protocol.game.ClientboundEntityEventPacket packet = new net.minecraft.network.protocol.game.ClientboundEntityEventPacket(
            this.getHandle(),
            net.minecraft.world.entity.LivingEntity.entityEventForEquipmentBreak(org.bukkit.craftbukkit.v1_20_R2.CraftEquipmentSlot.getNMS(slot))
        );
        players.forEach(player -> ((CraftPlayer) player).getHandle().connection.send(packet));
    }

    @Override
    public ItemStack damageItemStack(ItemStack stack, int amount) {
        final net.minecraft.world.item.ItemStack nmsStack;
        if (stack instanceof CraftItemStack craftItemStack) {
            if (craftItemStack.handle == null || craftItemStack.handle.isEmpty()) {
                return stack;
            }
            nmsStack = craftItemStack.handle;
        } else {
            nmsStack = CraftItemStack.asNMSCopy(stack);
            stack = CraftItemStack.asCraftMirror(nmsStack); // mirror to capture changes in hurt logic & events
        }
        this.damageItemStack0(nmsStack, amount, null);
        return stack;
    }

    @Override
    public void damageItemStack(org.bukkit.inventory.EquipmentSlot slot, int amount) {
        final net.minecraft.world.entity.EquipmentSlot nmsSlot = org.bukkit.craftbukkit.v1_20_R2.CraftEquipmentSlot.getNMS(slot);
        this.damageItemStack0(this.getHandle().getItemBySlot(nmsSlot), amount, nmsSlot);
    }

    @Override
    public float getBodyYaw() {
        return this.getHandle().getVisualRotationYInDegrees();
    }

    @Override
    public void setBodyYaw(float bodyYaw) {
        this.getHandle().setYBodyRot(bodyYaw);
    }

    private void damageItemStack0(net.minecraft.world.item.ItemStack nmsStack, int amount, net.minecraft.world.entity.EquipmentSlot slot) {
        nmsStack.hurtAndBreak(amount, this.getHandle(), livingEntity -> {
            if (slot != null) {
                livingEntity.broadcastBreakEvent(slot);
            }
        });
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public net.kyori.adventure.util.TriState getFrictionState() {
        return this.getHandle().frictionState;
    }

    @Override
    public void setFrictionState(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState state) {
        java.util.Objects.requireNonNull(state, "state may not be null");
        this.getHandle().frictionState = state;
    }

    @Override
    public void knockback(double strength, double directionX, double directionZ) {
        Preconditions.checkArgument(strength > 0, "Knockback strength must be > 0");
        getHandle().knockback(strength, directionX, directionZ);
    };
    // Paper end
}
