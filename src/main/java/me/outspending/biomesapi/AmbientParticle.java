package me.outspending.biomesapi;

import lombok.Getter;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Represents the different types of ambient particles that can be used in the game.
 * This is an enumeration of all the possible ambient particles, each associated with a specific SimpleParticleType.
 * The @Getter annotation from the Lombok library is used to automatically generate a getter method for the 'particle' field.
 *
 * @version 0.0.1
 */
@Getter
@AsOf("0.0.1")
public enum AmbientParticle {
    ASH(ParticleTypes.ASH),
    BUBBLE(ParticleTypes.BUBBLE),
    BUBBLE_COLUMN_UP(ParticleTypes.BUBBLE_COLUMN_UP),
    BUBBLE_POP(ParticleTypes.BUBBLE_POP),
    CAMPFIRE_COSY_SMOKE(ParticleTypes.CAMPFIRE_COSY_SMOKE),
    CAMPFIRE_SIGNAL_SMOKE(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE),
    CLOUD(ParticleTypes.CLOUD),
    COMPOSTER(ParticleTypes.COMPOSTER),
    CRIMSON_SPORE(ParticleTypes.CRIMSON_SPORE),
    CRIT(ParticleTypes.CRIT),
    CURRENT_DOWN(ParticleTypes.CURRENT_DOWN),
    DAMAGE_INDICATOR(ParticleTypes.DAMAGE_INDICATOR),
    DOLPHIN(ParticleTypes.DOLPHIN),
    DRAGON_BREATH(ParticleTypes.DRAGON_BREATH),
    DRIPPING_DRIPSTONE_LAVA(ParticleTypes.DRIPPING_DRIPSTONE_LAVA),
    DRIPPING_DRIPSTONE_WATER(ParticleTypes.DRIPPING_DRIPSTONE_WATER),
    DRIPPING_HONEY(ParticleTypes.DRIPPING_HONEY),
    DRIPPING_OBSIDIAN_TEAR(ParticleTypes.DRIPPING_OBSIDIAN_TEAR),
    DRIPPING_WATER(ParticleTypes.DRIPPING_WATER),
    EFFECT(ParticleTypes.EFFECT),
    ELDER_GUARDIAN(ParticleTypes.ELDER_GUARDIAN),
    ELECTRIC_SPARK(ParticleTypes.ELECTRIC_SPARK),
    ENCHANT(ParticleTypes.ENCHANT),
    ENCHANTED_HIT(ParticleTypes.ENCHANTED_HIT),
    END_ROD(ParticleTypes.END_ROD),
    ENTITY_EFFECT(ParticleTypes.ENTITY_EFFECT),
    EXPLOSION(ParticleTypes.EXPLOSION),
    EXPLOSION_EMITTER(ParticleTypes.EXPLOSION_EMITTER),
    FALLING_DRIPSTONE_LAVA(ParticleTypes.FALLING_DRIPSTONE_LAVA),
    FALLING_DRIPSTONE_WATER(ParticleTypes.FALLING_DRIPSTONE_WATER),
    FALLING_HONEY(ParticleTypes.FALLING_HONEY),
    FALLING_NECTAR(ParticleTypes.FALLING_NECTAR),
    FALLING_OBSIDIAN_TEAR(ParticleTypes.FALLING_OBSIDIAN_TEAR),
    FALLING_SPORE_BLOSSOM(ParticleTypes.FALLING_SPORE_BLOSSOM),
    FALLING_WATER(ParticleTypes.FALLING_WATER),
    FIREWORK(ParticleTypes.FIREWORK),
    FISHING(ParticleTypes.FISHING),
    FLAME(ParticleTypes.FLAME),
    FLASH(ParticleTypes.FLASH),
    GLOW(ParticleTypes.GLOW),
    GLOW_SQUID_INK(ParticleTypes.GLOW_SQUID_INK),
    HAPPY_VILLAGER(ParticleTypes.HAPPY_VILLAGER),
    HEART(ParticleTypes.HEART),
    INSTANT_EFFECT(ParticleTypes.INSTANT_EFFECT),
    ITEM_SLIME(ParticleTypes.ITEM_SLIME),
    ITEM_SNOWBALL(ParticleTypes.ITEM_SNOWBALL),
    LANDING_HONEY(ParticleTypes.LANDING_HONEY),
    LANDING_OBSIDIAN_TEAR(ParticleTypes.LANDING_OBSIDIAN_TEAR),
    LARGE_SMOKE(ParticleTypes.LARGE_SMOKE),
    LAVA(ParticleTypes.LAVA),
    MYCELIUM(ParticleTypes.MYCELIUM),
    NAUTILUS(ParticleTypes.NAUTILUS),
    NOTE(ParticleTypes.NOTE),
    POOF(ParticleTypes.POOF),
    PORTAL(ParticleTypes.PORTAL),
    RAIN(ParticleTypes.RAIN),
    REVERSE_PORTAL(ParticleTypes.REVERSE_PORTAL),
    SCRAPE(ParticleTypes.SCRAPE),
    SMOKE(ParticleTypes.SMOKE),
    SNEEZE(ParticleTypes.SNEEZE),
    SNOWFLAKE(ParticleTypes.SNOWFLAKE),
    SOUL(ParticleTypes.SOUL),
    SOUL_FIRE_FLAME(ParticleTypes.SOUL_FIRE_FLAME),
    SPIT(ParticleTypes.SPIT),
    SPORE_BLOSSOM_AIR(ParticleTypes.SPORE_BLOSSOM_AIR),
    SQUID_INK(ParticleTypes.SQUID_INK),
    SWEEP_ATTACK(ParticleTypes.SWEEP_ATTACK),
    TOTEM_OF_UNDYING(ParticleTypes.TOTEM_OF_UNDYING),
    UNDERWATER(ParticleTypes.UNDERWATER),
    WARPED_SPORE(ParticleTypes.WARPED_SPORE),
    WAX_ON(ParticleTypes.WAX_ON),
    WAX_OFF(ParticleTypes.WAX_OFF),
    WHITE_ASH(ParticleTypes.WHITE_ASH),
    WITCH(ParticleTypes.WITCH);

    private final SimpleParticleType particle;

    /**
     * Constructor for the AmbientParticle enum.
     * This constructor is used to associate a specific SimpleParticleType with each ambient particle.
     * The @AsOf annotation indicates the version when this constructor was introduced.
     *
     * @param particle the SimpleParticleType associated with the ambient particle
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    AmbientParticle(SimpleParticleType particle) {
        this.particle = particle;
    }

}
