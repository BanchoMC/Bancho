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
    implementation("com.jayway.jsonpath:json-path:2.4.0")
    implementation("org.slf4j:slf4j-nop:1.7.30")
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
    relocate("com.bertramlabs", "bancho.lib.bertramlabs")
    relocate("com.jayway", "bancho.lib.jayway")
    // NOTE: This is completely unsupported by CommandAPI. Just don't do this. Ever.
    relocate("dev.jorel", "bancho.lib.jorel")
    relocate("javassist", "bancho.lib.javassist")
    relocate("org.slf4j", "bancho.lib.slf4j")
    relocate("org.objectweb", "bancho.lib.objectweb")
    relocate("de.tr7zw", "bancho.lib.tr7zw")
    relocate("net.minidev", "bancho.lib.minidev")

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