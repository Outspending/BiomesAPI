package me.outspending.biomesapi;

import me.outspending.biomesapi.annotations.AsOf;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a particle renderer with a specific ambient particle and probability.
 * This is a record class introduced in Java 14 as a preview feature and made final in Java 16.
 * Records provide a compact syntax for declaring classes which are transparent holders for shallowly immutable data.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public record ParticleRenderer(@NotNull AmbientParticle ambientParticle, float probability) {

    /**
     * Provides a default setting for the ParticleRenderer.
     * The default ambient particle is set to CLOUD and the default probability is set to 0.008F.
     *
     * @return a new instance of ParticleRenderer with default settings
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static ParticleRenderer defaultSettings() {
        return new ParticleRenderer(AmbientParticle.CLOUD, 0.008F);
    }

}