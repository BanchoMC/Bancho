import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "bancho"
version = "1.0-DEV"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.bertramlabs.plugins:hcl4j:0.2.0")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        expand("VERSION" to project.version)
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("Bancho")
    archiveClassifier.set("")
    archiveVersion.set(project.version.toString())

    relocate("kotlin", "bancho.lib.kotlin")
    relocate("org.jetbrains", "bancho.lib.jetbrains")
    relocate("org.intellij", "bancho.lib.intellij")
    relocate("org.apache", "bancho.lib.apache")
    relocate("com.bertramlabs.plugins.hcl4j", "bancho.lib.hcl4j")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}