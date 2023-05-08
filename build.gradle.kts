/*
 * ****************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    application
    alias(libs.plugins.owaspDepCheck)
    alias(libs.plugins.versions)
}

group = "com.spectralogic"
tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "18"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

dependencies {
    implementation(platform(libs.kotlinBom))
    implementation(platform(libs.ds3Bom))

    implementation(libs.cliktJvm)
    implementation(libs.ds3sdk)
    implementation(libs.kotlinReflect)
    implementation(libs.kotlinStdlib)
    implementation(libs.kotlinxCoroutinesCore)

    testImplementation(libs.kotlinTestRunnerJunit5)
    testImplementation(libs.mockk)
}

application {
    mainClass.set("com.spectralogic.bp.bench.AppKt")
}

tasks.wrapper {
    // to upgrade the gradle wrapper, bump the version below and run ./gradlew wrapper twice
    gradleVersion = "8.1.1"
}
