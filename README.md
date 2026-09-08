# ViaNeoForgePlus Bedrock

Independent NeoForge port of [florianreuth/viafabricplus-bedrock](https://github.com/florianreuth/viafabricplus-bedrock).
Adds the optional ViaBedrock integration back to ViaNeoForgePlus 5.x.

## Version and installation

- Addon **1.0.0**, matching its own upstream version (not the core's 5.0.1).
- Requires **ViaNeoForgePlus 5.0.1**, **Minecraft 26.2**, **NeoForge 26.2.0.82**, and **64-bit Java 25**.
- Put `vianeoforgeplus-bedrock-1.0.0.jar` alongside `ViaNeoForgePlus-5.0.1.jar` in a client instance's `mods` directory.
- Do not also install the Fabric addon or an older core containing Bedrock. No Fabric Loader/API or compatibility layer is required.

Open the ViaNeoForgePlus protocol screen and select **Bedrock 1.26.30 (1001)**.
The optional **Bedrock** settings include automatic port selection, experimental features and the account screen.
Online-mode Bedrock servers and Realms require the user's own Microsoft/Xbox sign-in; the addon does not obtain or copy accounts from another instance.
ViaBedrock remains experimental. Startup and Java-server tests are not evidence of Bedrock gameplay or Realms compatibility.

This is a separate Git repository and build, not a submodule or source set of the core.
No public NeoForge addon origin, Maven service or release download is assumed to exist until separately published.

## Build

First build and publish the matching NeoForge core in its repository:

~~~powershell
.\gradlew.bat build publishToMavenLocal
~~~

Then, in this repository:

~~~powershell
.\gradlew.bat build
~~~

Use Java 25 and the checked-in wrapper. Output: `build/libs/vianeoforgeplus-bedrock-1.0.0.jar`.
The local Maven coordinate is `com.viaversion:vianeoforgeplus-bedrock:1.0.0`; the required core coordinate is `com.viaversion:vianeoforgeplus:5.0.1`.
The core is resolved nontransitively from MavenLocal. The Fabric artifact at the upstream Maven repository is not a substitute.

`check` runs `verifyDistribution`: expanded NeoForge metadata, Mixin entries, lifecycle provider registration, five WebRTC native resources, duplicate classes and split packages against the core.
The addon embeds its own required protocol/auth/transport libraries and excludes libraries supplied by Minecraft, NeoForge and the core.
Both direct and remaining snapshot transitive dependencies are pinned to the tested artifacts.

The CI workflow checks out core tag `5.0.1`, builds/publishes the core locally, then builds the addon.
That core tag must be pushed before remote addon CI can succeed; a local build is not a remote CI result.

## Lifecycle and tests

NeoForge discovers the addon using mod ID `vianeoforgeplus_bedrock`.
The core discovers the integration through `META-INF/services/com.viaversion.viafabricplus.api.entrypoint.ViaFabricPlusEntrypoint`.
Do not instantiate the lifecycle provider a second time in the NeoForge mod constructor.
Upstream packages, Mixin config names and resource namespaces intentionally stay unchanged.

For a clean ModDev test run, use `.\gradlew.bat runFreshClient`; `-PclientDirectory=<path>` overrides the separate default test directory.
ModDev launches in DEV, even when it loads the packaged JARs. The core repository's `scripts/launch-standalone-test.ps1` additionally tests the official installed client in PROD with `-Profile bedrock`.
See `docs/TESTING-1.0.0.md` and `docs/UPSTREAM_REFS.md` for the recorded scope and Git provenance.

## Upstream and license

Upstream release **1.0.0**: `b7ed71bf63485d51cbc099097256158d59622a7e`.
The source checkout `a3c0498` additionally contains the upstream post-release documentation update.
The NeoForge port keeps branch `main` and tag `1.0.0`; the original upstream tag is retained in `refs/upstream/tags/1.0.0`.

Thanks to the original addon contributors, ViaFabricPlus, ViaBedrock, ViaVersion, MinecraftAuth and the transport/native library authors.
Copyright notices and dependency licenses are retained. Licensed under [GPL-3.0-or-later](LICENSE).
Report NeoForge integration issues to the port maintainer, not as Fabric-loader failures upstream.
