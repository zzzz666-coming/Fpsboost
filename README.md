# Fpsboost

Standalone Fabric client-side performance mod for Minecraft Java 1.21.11, targeting Android launchers such as PojavLauncher.

## Design goals
- No hard dependency on Sodium, Lithium, FerriteCore, or other optimization mods.
- No invasive mixins in the initial implementation.
- Explicit opt-in settings; never silently overwrite unrelated mod settings.
- Compatible with other mods by using Fabric API and vanilla client options only.

## Build
Requires JDK 21 and Gradle. Run `gradle build`; the mod jar is in `build/libs/`.

## Current scope
The first implementation provides a lightweight client-side performance profile. It does not promise a fixed FPS increase; results depend on device, renderer, thermal state, and modpack.
