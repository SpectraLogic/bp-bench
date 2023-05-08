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

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.test {
    useJUnitPlatform { }
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlinx.coroutines.FlowPreview")
    }
}

application {
    mainClass.set("com.spectralogic.bp.bench.AppKt")
}

tasks.wrapper {
    // to upgrade the gradle wrapper, bump the version below and run ./gradlew wrapper twice
    gradleVersion = "8.1.1"
}
