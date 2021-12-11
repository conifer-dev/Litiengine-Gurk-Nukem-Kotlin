plugins {
    kotlin("jvm") version "1.6.10-RC"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("de.gurkenlabs:litiengine:0.5.1")
}

application {
    mainClass.set("GameKt")
}

sourceSets {
    main {
        resources.srcDir("sprites")
        resources.srcDir("audio")
        resources.srcDir("maps")

    }
}