package dev.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**
 * Standalone client-side performance helper.
 * It deliberately avoids mixins and does not require other optimization mods.
 */
public final class FpsboostClient implements ClientModInitializer {
    private static final int RENDER_DISTANCE = 6;
    private static final int SIMULATION_DISTANCE = 5;
    private static final double ENTITY_DISTANCE = 0.5;
    private static final int FPS_LIMIT = 120;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("fpsboost")
                .then(literal("apply").executes(context -> {
                    applyMobileProfile();
                    context.getSource().sendFeedback(Text.literal("§a[Fpsboost] Perfil móvil aplicado."));
                    return 1;
                }))
                .then(literal("info").executes(context -> {
                    context.getSource().sendFeedback(Text.literal("§b[Fpsboost] Independiente · Render 6 · Simulación 5 · Entidades 50% · FPS 120"));
                    return 1;
                }))
                .then(literal("conflicts").executes(context -> {
                    reportOtherOptimizationMods(context.getSource());
                    return 1;
                }))
            )
        );
    }

    private static void applyMobileProfile() {
        MinecraftClient client = MinecraftClient.getInstance();
        var options = client.options;
        // Only vanilla client options are changed, and only after the player runs the command.
        options.getViewDistance().setValue(RENDER_DISTANCE);
        options.getSimulationDistance().setValue(SIMULATION_DISTANCE);
        options.getEntityDistanceScaling().setValue(ENTITY_DISTANCE);
        options.getMaxFps().setValue(FPS_LIMIT);
        options.getEntityShadows().setValue(false);
        options.getAo().setValue(false);
        options.getBiomeBlendRadius().setValue(0);
        options.getMipmapLevels().setValue(0);
        options.write();
    }

    private static void reportOtherOptimizationMods(net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
        var loader = FabricLoader.getInstance();
        String[] known = {"sodium", "lithium", "ferritecore", "immediatelyfast", "entityculling", "c2me", "vulkanmod"};
        boolean found = false;
        for (String id : known) {
            if (loader.isModLoaded(id)) {
                source.sendFeedback(Text.literal("§e[Fpsboost] Detectado: " + id + " (no se modifica ni se desactiva)."));
                found = true;
            }
        }
        if (!found) source.sendFeedback(Text.literal("§a[Fpsboost] No se detectaron los optimizadores conocidos."));
        source.sendFeedback(Text.literal("§7Fpsboost no depende de ellos y no altera sus archivos ni ajustes."));
    }
}
