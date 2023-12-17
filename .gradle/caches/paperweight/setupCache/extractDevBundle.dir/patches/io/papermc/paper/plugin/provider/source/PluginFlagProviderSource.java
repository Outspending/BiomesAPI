package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Registers providers at the provided files in the add-plugin argument.
 */
public class PluginFlagProviderSource implements ProviderSource<List<File>> {

    public static final PluginFlagProviderSource INSTANCE = new PluginFlagProviderSource();
    private static final Logger LOGGER = LogUtils.getClassLogger();
    private final FileProviderSource providerSource = new FileProviderSource("File '%s' specified through 'add-plugin' argument"::formatted);

    @Override
    public void registerProviders(EntrypointHandler entrypointHandler, List<File> context) {
        for (File file : context) {
            try {
                this.providerSource.registerProviders(entrypointHandler, file.toPath());
            } catch (Exception e) {
                LOGGER.error("Error loading plugin: " + e.getMessage(), e);
            }
        }
    }
}
