package com.spichka.pinglogger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingLogger implements ModInitializer {
    public static final String MOD_ID = "pinglogger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // MUST be public for mixin access
    public static BufferedWriter writer;

    // ADD THIS (you forgot it)
    public static final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    public void onInitialize() {
        try {
            Path file = Path.of("logs.jsonl");

            writer = Files.newBufferedWriter(
                    file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}