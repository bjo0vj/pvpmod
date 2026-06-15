/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
// import meteordevelopment.meteorclient.systems.modules.world.Ambience; // AUTO-REMOVED
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.world.level.dimension.DimensionType;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void updateRenderState(ClientLevel level, float partialTicks, Camera camera, SkyRenderState state, CallbackInfo ci) {
//         Ambience ambience = Modules.get().get(Ambience.class); // AUTO-REMOVED
//         if (!ambience.isActive()) return; // AUTO-REMOVED

//         if (ambience.endSky.get()) state.skybox = DimensionType.Skybox.END; // AUTO-REMOVED
//         if (ambience.customSkyColor.get()) state.skyColor = ambience.skyColor().getPacked(); // AUTO-REMOVED
    }

    @ModifyArg(method = "renderEndSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
    private Vector4fc modifyEndSkyColor(Vector4fc original) {
//         Ambience ambience = Modules.get().get(Ambience.class); // AUTO-REMOVED

//         if (ambience.isActive() && ambience.endSky.get() && ambience.customSkyColor.get()) // AUTO-REMOVED
//             return ambience.skyColor().getVec4f(); // AUTO-REMOVED
        else return original;
    }
}
