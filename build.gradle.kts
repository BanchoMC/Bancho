import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    maven("https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.bertramlabs.plugins:hcl4j:0.2.0")
    implementation("dev.jorel:commandapi-core:3.2")
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
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
    // NOTE: This is completely unsupported by CommandAPI. Just don't do this. Ever.
    relocate("dev.jorel.commandapi", "bancho.lib.commandapi")
    relocate("javassist", "bancho.lib.javassist")
    // Multiple libraries in this package
    relocate("de.tr7zw", "bancho.lib")

    // Remove other config.yml
    exclude("config.yml")
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