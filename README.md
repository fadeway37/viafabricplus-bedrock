# ViaNeoForgePlus Bedrock

Addon for [ViaNeoForgePlus](https://github.com/fadeway37/ViaNeoForgePlus) that adds Minecraft: Bedrock Edition support back
to the mod.

## Usage

1. Install [ViaNeoForgePlus](https://github.com/fadeway37/ViaNeoForgePlus) and this addon.
2. Open the ViaNeoForgePlus screen and select the Bedrock version.
3. For online mode servers and Realms, open the settings, switch to the `Bedrock` tab and click
   `Account for Bedrock Edition`. Your browser opens for the Microsoft login, and the entry then shows the name of the
   account that is signed in.

Logging in is only needed once and is independent of Realms — the same account is used for every online mode Bedrock
server. `Bedrock Realms` in the ViaNeoForgePlus screen stays disabled until an account is set.

The remaining `Bedrock` settings control whether the default Bedrock port is filled in automatically and whether
ViaBedrock's experimental features are enabled.

## Gradle

The mod can be published to Maven Local as `com.viaversion:vianeoforgeplus-bedrock`.
Run `./gradlew publishToMavenLocal` in the ViaNeoForgePlus repository first, then in this repository.

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    // Replace each version with the corresponding release
    runtimeOnly("com.viaversion:vianeoforgeplus:x.x.x") {
        isTransitive = false
    }
    runtimeOnly("com.viaversion:vianeoforgeplus-bedrock:x.x.x") {
        isTransitive = false
    }
}
```

## Links

- [ViaNeoForgePlus](https://github.com/fadeway37/ViaNeoForgePlus)
- [ViaFabricPlus Bedrock (upstream)](https://github.com/florianreuth/viafabricplus-bedrock)
- [ViaBedrock](https://github.com/RaphiMC/ViaBedrock)

## Contact

- Issues: [Issue tracker](https://github.com/fadeway37/viafabricplus-bedrock/issues)
