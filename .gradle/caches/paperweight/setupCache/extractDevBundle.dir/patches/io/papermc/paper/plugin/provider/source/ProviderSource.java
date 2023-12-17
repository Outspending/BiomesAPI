package io.papermc.paper.plugin.provider.source;

import io.papermc.paper.plugin.entrypoint.EntrypointHandler;

/**
 * A provider source is responsible for giving PluginTypes an EntrypointHandler for
 * registering providers at.
 *
 * @param <C> context
 */
public interface ProviderSource<C> {

    void registerProviders(EntrypointHandler entrypointHandler, C context) throws Throwable;
}
