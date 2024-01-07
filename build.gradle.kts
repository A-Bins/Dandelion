import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.nio.file.Files

/* Plugins */
plugins {
    id("idea")
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

/* Project Setting */
group = "kr.abins"
version = ""

/* Repositories */
repositories {
    mavenCentral()
    maven(url = "https://mvn.lumine.io/repository/maven-public/") // model engine - repo
    maven(url = "https://repo.caramel.moe/repository/maven-public/") // caramel - repo
    maven(url = "https://repo.codemc.io/repository/maven-snapshots/") // ?
    maven(url = "https://repo.papermc.io/repository/maven-public/") // papermc-repo
    maven(url = "https://oss.sonatype.org/content/groups/public/") //sonatype
    maven(url = "https://repo.citizensnpcs.co/") // citizens - repo
}

/* Dependencies */
dependencies {
    val graalVersion = "23.1.1"
    val gameVersion = "1.20.4-R0.1-SNAPSHOT"

    /* Daydream API */
    compileOnly("moe.caramel", "acacia-api", "1.0.0-SNAPSHOT")
    compileOnly("moe.caramel", "daydream-api", gameVersion)
    paperweight.paperDevBundle(gameVersion)

    /* TheOutpost Dependencies */
    compileOnly(files("C:/TheOutpost/plugins/TheOutpost.jar"))

    /* Graal JS Dependencies */
    compileOnly("org.graalvm.polyglot", "polyglot", graalVersion)
    compileOnly("org.graalvm.polyglot", "js", graalVersion)

    /* Kotlin Dependencies */
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
    compileOnly("org.jetbrains.kotlinx", "kotlinx-datetime", "0.4.0")
    compileOnly("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.5.0")

    /* Kotlin Coroutine Dependencies */
    compileOnly("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.7.3") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    }

    /* MC-Coroutine Dependencies */
    compileOnly("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-api", "2.13.0")
    compileOnly("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-core", "2.13.0")
}

val dest = File("C:/TheOutpost/plugins")
val update = File("$dest/update")
val paperMavenPublicUrl = "https://papermc.io/repo/repository/maven-public/"

val targetJavaVersion = 17
java.toolchain {
    languageVersion.set( JavaLanguageVersion.of(targetJavaVersion) )
}

/* Tasks */
tasks {
    jar {
        archiveFileName.set("TheOutpost.jar")
        destinationDirectory.set(update)
        doLast {
            val delete = File("$update/LABYRINTH")
            if (delete.exists()) delete.delete()
        }
    }

    withType<ShadowJar> {
        relocate("io.github.retrooper.packetevents", "moe.caramel.common.shaded.io.github.retrooper.packetevents")
        relocate("com.github.retrooper.packetevents", "moe.caramel.common.shaded.com.github.retrooper.packetevents")
    }

    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }
}