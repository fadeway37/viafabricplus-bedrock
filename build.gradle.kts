import org.gradle.api.artifacts.component.ModuleComponentIdentifier


plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.146"
}

group = property("project_group") as String
version = property("project_version") as String
base { archivesName = property("project_name") as String }
java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
}
repositories {
    mavenLocal { content { includeModule("com.viaversion", "vianeoforgeplus") } }
    mavenCentral()
    maven("https://repo.viaversion.com")
    maven("https://maven.lenni0451.net/everything")
    maven("https://jitpack.io") { content { includeGroup("com.github.oryxel1") } }
}

// The core and Minecraft provide these modules. Never merge a second copy into
// the addon: NeoForge rejects split packages across its transforming modules.
val providedModules = setOf(
    "com.google.code.gson:gson", "com.google.errorprone:error_prone_annotations",
    "it.unimi.dsi:fastutil", "org.jetbrains:annotations", "org.joml:joml",
    "org.projectlombok:lombok", "net.lenni0451:Reflect", "net.lenni0451.commons:unchecked",
    "com.mojang:brigadier", "at.yawk.lz4:lz4-java"
)
val embedded = configurations.create("embedded") {
    isCanBeConsumed = false
    isCanBeResolved = true
}
configurations.compileOnly { extendsFrom(embedded) }
val coreMod = configurations.create("coreMod") {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}
configurations.compileOnly { extendsFrom(coreMod) }

// ViaBedrock pins dialog, whose POM still requests a mutable registry snapshot.
// Use the exact registry artifact validated with this release on every classpath.
configurations.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.viaversion.mcstructs" && requested.name == "registry") {
            useVersion("5-3.2.2-20260324.005346-17")
            because("Reproduce the tested ViaBedrock 1.26.30 dependency set")
        }
    }
}

val mergedLibraries = embedded.incoming.artifactView {
    componentFilter { id ->
        id !is ModuleComponentIdentifier ||
            (id.group != "io.netty" && "${id.group}:${id.module}" !in providedModules)
    }
}.files
val mergedLibraryOutput = layout.buildDirectory.dir("generated/merged-libraries")
val mergeLibraries = tasks.register<Sync>("mergeLibraries") {
    from({ mergedLibraries.map(::zipTree) })
    into(mergedLibraryOutput)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude(
        "META-INF/MANIFEST.MF", "META-INF/INDEX.LIST", "META-INF/*.SF",
        "META-INF/*.RSA", "META-INF/*.DSA", "META-INF/maven/**",
        "module-info.class", "META-INF/versions/**/module-info.class"
    )
}
sourceSets.main { output.dir(mapOf("builtBy" to mergeLibraries), mergedLibraryOutput) }

val productionRun = sourceSets.create("productionRun")
val testDirectory = layout.projectDirectory.dir(
    providers.gradleProperty("clientDirectory").getOrElse("../vianeoforgeplus-test-26.2-26.2.0.82/bedrock")
)
val prepareFreshClient = tasks.register<Copy>("prepareFreshClient") {
    dependsOn(tasks.named("jar"))
    from(coreMod)
    from(tasks.named<Jar>("jar").flatMap { it.archiveFile })
    into(testDirectory.dir("mods"))
}
neoForge {
    version = property("neoforge_version") as String
    validateAccessTransformers = true
    addModdingDependenciesTo(productionRun)
    runs {
        register("freshClient") {
            client()
            sourceSet.set(productionRun)
            loadedMods.set(emptySet())
            gameDirectory.set(testDirectory)
            taskBefore(prepareFreshClient)
            providers.gradleProperty("testServer").orNull?.let {
                programArgument("--quickPlayMultiplayer")
                programArgument(it)
            }
        }
    }
    mods { register("vianeoforgeplus_bedrock") { sourceSet(sourceSets.main.get()) } }
}

dependencies {
    add("coreMod", "com.viaversion:vianeoforgeplus:5.0.1")
    add("embedded", "net.raphimc:ViaBedrock:0.0.29-20260720.172239-5") {
        exclude(group = "com.mojang", module = "brigadier")
        exclude(group = "at.yawk.lz4", module = "lz4-java")
        exclude(group = "io.netty")
    }
    add("embedded", "net.raphimc:MinecraftAuth:5.0.2") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    add("embedded", "dev.kastle.netty:netty-transport-raknet:1.7.3") { exclude(group = "io.netty") }
    add("embedded", "dev.kastle.netty:netty-transport-nethernet:1.7.3") { exclude(group = "io.netty") }
    arrayOf("windows-x86_64", "windows-aarch64", "linux-x86_64", "linux-aarch64", "macos-aarch64").forEach {
        add("embedded", "dev.kastle.webrtc:webrtc-java:1.0.3:$it")
    }
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 25
}
tasks.processResources {
    val properties = mapOf(
        "version" to project.version,
        "description" to project.property("project_description"),
        "minecraftVersion" to project.property("minecraft_version"),
        "neoForgeVersion" to project.property("neoforge_version")
    )
    inputs.properties(properties)
    filesMatching("META-INF/neoforge.mods.toml") { expand(properties) }
}
tasks.jar {
    manifest { attributes(
        "Implementation-Title" to "ViaNeoForgePlus Bedrock",
        "Implementation-Version" to project.version,
        "Automatic-Module-Name" to "com.viaversion.vianeoforgeplus.bedrock"
    ) }
}
publishing {
    publications { create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifactId = "vianeoforgeplus-bedrock"
    } }
}

apply(from = "gradle/verify-distribution.gradle.kts")
