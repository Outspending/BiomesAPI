package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import org.slf4j.Logger;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads all plugin providers in the given directory.
 */
public class DirectoryProviderSource extends FileProviderSource {

    public static final DirectoryProviderSource INSTANCE = new DirectoryProviderSource();
    private static final Logger LOGGER = LogUtils.getClassLogger();

    public DirectoryProviderSource() {
        super("Directory '%s'"::formatted);
    }

    @Override
    public void registerProviders(EntrypointHandler entrypointHandler, Path context) throws Exception {
        // Sym link happy, create file if missing.
        if (!Files.isDirectory(context)) {
            Files.createDirectories(context);
        }

        Files.walk(context, 1, FileVisitOption.FOLLOW_LINKS)
            .filter(this::isValidFile)
            .forEach((path) -> {
                try {
                    super.registerProviders(entrypointHandler, path);
                } catch (IllegalArgumentException ignored) {
                    // Ignore initial argument exceptions
                } catch (Exception e) {
                    LOGGER.error("Error loading plugin: " + e.getMessage(), e);
                }
            });
    }

    public boolean isValidFile(Path path) {
        // Avoid loading plugins that start with a dot
        return Files.isRegularFile(path) && !path.startsWith(".");
    }
}
