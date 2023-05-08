/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "com.spectralogic"
tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "18"
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.github.ajalt.clikt:clikt-jvm:3.5.0")

    implementation("com.github.spectralogic.ds3_java_sdk:ds3-sdk:5.4.0")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.12.4")
}

application {
    mainClass.set("com.spectralogic.bp.bench.AppKt")
}

tasks.wrapper {
    // to upgrade the gradle wrapper, bump the version below and run ./gradlew wrapper twice
    gradleVersion = "8.1.1"
}
