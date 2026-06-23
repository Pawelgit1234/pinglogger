package com.spichka.pinglogger.mixin;

import com.spichka.pinglogger.PingLogger;
import com.google.gson.Gson;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.server.network.ServerQueryNetworkHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.util.Map;

@Mixin(ServerQueryNetworkHandler.class)
public class ServerQueryHandlerMixin {

    private static final Gson gson = new Gson();

    @Shadow
    @Final
    private ClientConnection connection;

    @Inject(method = "onRequest", at = @At("HEAD"))
    private void onPing(QueryRequestC2SPacket packet, CallbackInfo ci) {

        String json = gson.toJson(Map.of(
            "timestamp", Instant.now().toString(),
            "ip", connection.getAddress().toString()
        ));

        PingLogger.executor.execute(() -> {
            try {
                synchronized (PingLogger.class) {
                    PingLogger.writer.write(json);
                    PingLogger.writer.newLine();
                    PingLogger.writer.flush();
                    PingLogger.LOGGER.info(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}