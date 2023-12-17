package com.destroystokyo.paper.network;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import io.papermc.paper.adventure.AdventureComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftIconCache;

public final class StandardPaperServerListPingEventImpl extends PaperServerListPingEventImpl {

    // private static final GameProfile[] EMPTY_PROFILES = new GameProfile[0];
    private static final UUID FAKE_UUID = new UUID(0, 0);

    private List<GameProfile> originalSample;

    private StandardPaperServerListPingEventImpl(MinecraftServer server, Connection networkManager, ServerStatus ping) {
        super(server, new PaperStatusClient(networkManager), ping.version().map(ServerStatus.Version::protocol).orElse(-1), server.server.getServerIcon());
        this.originalSample = ping.players().map(ServerStatus.Players::sample).orElse(null); // GH-1473 - pre-tick race condition NPE
    }

    @Nonnull
    @Override
    public List<PlayerProfile> getPlayerSample() {
        List<PlayerProfile> sample = super.getPlayerSample();

        if (this.originalSample != null) {
            for (GameProfile profile : this.originalSample) {
                sample.add(CraftPlayerProfile.asBukkitCopy(profile));
            }
            this.originalSample = null;
        }

        return sample;
    }

    private List<GameProfile> getPlayerSampleHandle() {
        if (this.originalSample != null) {
            return this.originalSample;
        }

        List<PlayerProfile> entries = super.getPlayerSample();
        if (entries.isEmpty()) {
            return Collections.emptyList();
        }

        final List<GameProfile> profiles = new ArrayList<>();
        for (PlayerProfile profile : entries) {
            /*
             * Avoid null UUIDs/names since that will make the response invalid
             * on the client.
             * Instead, fall back to a fake/empty UUID and an empty string as name.
             * This can be used to create custom lines in the player list that do not
             * refer to a specific player.
             */
            if (profile.getId() != null && profile.getName() != null) {
                profiles.add(CraftPlayerProfile.asAuthlib(profile));
            } else {
                profiles.add(new GameProfile(MoreObjects.firstNonNull(profile.getId(), FAKE_UUID), Strings.nullToEmpty(profile.getName())));
            }
        }
        return profiles;
    }

    public static void processRequest(MinecraftServer server, Connection networkManager) {
        StandardPaperServerListPingEventImpl event = new StandardPaperServerListPingEventImpl(server, networkManager, server.getStatus());
        server.server.getPluginManager().callEvent(event);

        // Close connection immediately if event is cancelled
        if (event.isCancelled()) {
            networkManager.disconnect(null);
            return;
        }

        // Setup response

        // Description
        final Component description = new AdventureComponent(event.motd());

        // Players
        final Optional<ServerStatus.Players> players;
        if (!event.shouldHidePlayers()) {
            players = Optional.of(new ServerStatus.Players(event.getMaxPlayers(), event.getNumPlayers(), event.getPlayerSampleHandle()));
        } else {
            players = Optional.empty();
        }

        // Version
        final ServerStatus.Version version = new ServerStatus.Version(event.getVersion(), event.getProtocolVersion());

        // Favicon
        final Optional<ServerStatus.Favicon> favicon;
        if (event.getServerIcon() != null) {
            favicon = Optional.of(new ServerStatus.Favicon(((CraftIconCache) event.getServerIcon()).value));
        } else {
            favicon = Optional.empty();
        }
        final ServerStatus ping = new ServerStatus(description, players, Optional.of(version), favicon, server.enforceSecureProfile());

        // Send response
        networkManager.send(new ClientboundStatusResponsePacket(ping));
    }

}
