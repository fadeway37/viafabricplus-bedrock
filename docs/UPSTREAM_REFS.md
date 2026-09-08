# Upstream refs — ViaNeoForgePlus Bedrock 1.0.0

Local snapshot: **September 8, 2026**.

- Independent repository: `D:/project2/vianeoforgeplus-bedrock`, with its own `.git`.
- Upstream remote: https://github.com/florianreuth/viafabricplus-bedrock.git
- Upstream release 1.0.0: `b7ed71bf63485d51cbc099097256158d59622a7e`.
- Port parent/source checkout: `a3c0498d6f5228395e13d884fce30934438f1ba6`. Its only difference from that release is the upstream README update.
- Local branch **main** and local tag **1.0.0** point to the NeoForge port. These names match upstream's one branch and one tag.
- The unmodified Fabric tag is retained at `refs/upstream/tags/1.0.0`; upstream fetching uses `tagopt=--no-tags`.
- The addon tracks its own upstream version **1.0.0**, not the main mod's **5.0.1**.

No NeoForge addon remote repository or `origin` was created, and no commit/tag was pushed.
The sibling core is a local Maven build dependency, not a Git submodule or nested repository.
The workflow will need the NeoForge core's `5.0.1` tag published before it can build on a remote runner.

~~~sh
git log -1 --format='%H %P' 1.0.0
git show refs/upstream/tags/1.0.0
git for-each-ref --format='%(refname)' refs/heads refs/tags
git ls-remote --heads --tags --refs upstream
~~~

See [the test report](TESTING-1.0.0.md) for what was actually validated.
