# ViaNeoForgePlus Bedrock 1.0.0 test report

Date: **September 8, 2026**, **Asia/Shanghai (UTC+08:00)**. Local tests only; no remote CI result.

## Tested build

- Minecraft **26.2**, NeoForge **26.2.0.82**, Java **25.0.3**, Windows x64.
- Core **ViaNeoForgePlus 5.0.1** and addon **1.0.0**.
- ViaBedrock `0.0.29-20260720.172239-5`, exposing **Bedrock 1.26.30 (1001)**.
- Core resolved nontransitively from MavenLocal; shared Minecraft/core/Netty libraries excluded from the addon.
- MinecraftAuth 5.0.2, RakNet/NetherNet 1.7.0, WebRTC 1.0.3; remaining mutable mcstructs registry snapshot fixed to `5-3.2.2-20260324.005346-17`.

SHA-256, identical between the final build outputs and the official-client copies used in the test:

~~~text
54F9926C164DF80AC19E910A2D2EC855E70A92AE2518E2663611F49D7A3F5C05  vianeoforgeplus-bedrock-1.0.0.jar
DC5374AA4623D9DDA9DC466B180550D7EC2F0734DD6BF4042801C1CA3360673C  ViaNeoForgePlus-5.0.1.jar
~~~

`.\gradlew.bat build` passed after the matching core was built/published locally.
`verifyDistribution` checked **1,891 classes / 48 Mixin entries**, NeoForge metadata,
the lifecycle service provider, all five WebRTC native resources, no duplicate ZIP entries,
no bundled Minecraft/Gson/Netty classes, and no split packages with the core.
The addon has no JUnit test sources. Native-resource presence is not proof of native execution on all five platforms.

## Runtime results

Test root: `D:/project2/vianeoforgeplus-test-26.2-26.2.0.82`. No old client accounts, settings or worlds were copied.

| Profile | Result |
| --- | --- |
| `bedrock` (ModDev DEV) | Two lifecycle providers, Bedrock protocol/resource initialization, Java 26.1.2 world/inventory/movement and server-confirmed placement/breaking; normal exit 18:51:48 |
| `standalone/instances/bedrock` (official PROD) | Correct core + addon mod list, two providers, Bedrock 1.26.30 registration and resource HTTP server on loopback; Java 26.1.2 gameplay checks passed; Bedrock settings page present; normal exit 19:14:28 and process code 0 |

The official log explicitly says `Starting FancyModLoader version 11.0.16 (CLIENT in PROD)`.
At 19:01:20 it records `Started resource pack HTTP server on http://127.0.0.1:3371/`.
The protocol page showed Bedrock 1.26.30; settings included Bedrock auto-port/experimental options and an account-not-logged-in control.
Realms stayed unavailable without an account. No account login or diagnostic upload was initiated.

The offline test player joined **127.0.0.1:25582**, a new local Java **26.1.2** server, at **19:01:55**.
World rendering, creative inventory and movement were checked interactively. F3 showed translation from **26.2 (776)** to **26.1–26.1.2 (775)**.
Conditional server block checks emitted:

- **19:07:57**: `VNF_501_PROD_ADDON_PLACE_PASS_5_m60_10`.
- **19:08:28**: `VNF_501_PROD_ADDON_BREAK_PASS_5_m60_10`.

Evidence remains in `standalone/instances/bedrock/logs/latest.log` and `server-26.1.2/logs/latest.log`.
The core-only official profile independently passed the same Java-server interaction checks without loading Bedrock.
The server saved its new world and stopped normally at **19:25:09**.

## Limitations and warnings

**No actual Bedrock/RakNet connection or gameplay, Microsoft/Xbox authentication, or Realms was tested.**
Registration, resource initialization and Java-server compatibility do not prove these flows work.
No compatible 1.26.30 test server/account was available. The official Bedrock download service returned **1.26.45.1** on the test date;
it was not assumed compatible with the bundled protocol and was not downloaded or launched.

Offline token 0 caused expected account/certificate HTTP 401 and Realms authorization errors.
Other observed non-fatal host/engine warnings included OSHI/Perflib counters, shader samplers,
the Windows Netty epoll capability probe/log rendering, `vv:mod_details` and LAN multicast discovery.
These logs are not claimed to be error-free. No Windows security settings were changed.

The initial DEV world-entry crash from the core's invalid NeoForge item-use injection was fixed before the passing runs.
Earlier crash reports remain under the DEV directory. All official tests used the corrected core artifact above.
Other mods, legacy protocol matrices, public authenticated servers, other operating systems and remote CI remain untested.

## Repeat

Build/publish the core, then build this addon. Use the local test root's `start-client.ps1 -Profile bedrock`,
or the core repository's `scripts/launch-standalone-test.ps1 -Profile bedrock -JavaHome <Java25>`.
These launch the already officially installed clean client with an offline identity and do not read launcher account files.
For authenticated public servers, use a normal launcher and the user's own account.

The more detailed combined report is `docs/TESTING-5.0.1.md` in the sibling ViaNeoForgePlus repository.
