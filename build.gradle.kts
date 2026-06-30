/*
 * ****************************************************************************
 *   Copyright 2014-2023 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

plugins {
    alias(libs.plugins.kotlinJvm)
    application
    alias(libs.plugins.owaspDepCheck)
    alias(libs.plugins.versions)
}

group = "com.spectralogic"
version = "1.2.0"

dependencies {
    implementation(platform(libs.ds3Bom))
    implementation(platform(libs.kotlinBom))

    implementation(libs.cliktJvm)
    implementation(libs.ds3sdk)
    implementation(libs.kotlinReflect)
    implementation(libs.kotlinStdlib)
    implementation(libs.kotlinxCoroutinesCore)

    testImplementation(libs.kotlinTestRunnerJunit5)
    testImplementation(libs.mockk)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(26))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.test {
    useJUnitPlatform { }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
    }
}

application {
    mainClass.set("com.spectralogic.bp.bench.AppKt")
}

tasks.wrapper {
    // to upgrade the gradle wrapper, bump the version below and run ./gradlew wrapper twice
    gradleVersion = "9.6.1"
}
