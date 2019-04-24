/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
    application
}

repositories {
    jcenter()
    maven("http://dl.bintray.com/spectralogic/ds3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.property("kotlinxCoroutines")}")
    implementation("com.github.ajalt:clikt:${project.property("clikt")}")
    implementation("com.spectralogic.ds3:ds3-sdk:${project.property("ds3")}")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:${project.property("kotlintest")}")
    testImplementation("io.mockk:mockk:${project.property("mockk")}")
}

application {
    mainClassName = "com.spectralogic.bp.bench.AppKt"
}
