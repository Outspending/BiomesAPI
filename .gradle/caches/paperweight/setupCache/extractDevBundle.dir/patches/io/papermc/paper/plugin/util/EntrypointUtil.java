package io.papermc.paper.plugin.util;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler;
import io.papermc.paper.plugin.provider.source.ProviderSource;
import org.slf4j.Logger;

public class EntrypointUtil {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    public static <C> void registerProvidersFromSource(ProviderSource<C> source, C context) {
        try {
            source.registerProviders(LaunchEntryPointHandler.INSTANCE, context);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
