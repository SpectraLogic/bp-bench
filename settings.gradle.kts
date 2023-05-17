/*
 * ****************************************************************************
 *   Copyright 2014-2023 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.5.0")
}

rootProject.name = "bp-bench"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            //    implementation("com.github.spectralogic.ds3_java_sdk:ds3-sdk:5.4.0")

            version("clikt", "3.5.0")
            version("kotlin", "1.7.10")
            version("kotlinTest", "3.4.2")
            version("kotlinxCoroutines", "1.6.2")
            version("mockk", "1.12.4")
            version("ds3", "5.6.0")

            library("cliktJvm", "com.github.ajalt.clikt", "clikt-jvm").versionRef("clikt")
            library("ds3Bom", "com.spectralogic.ds3", "ds3-bom").versionRef("ds3")
            library("ds3sdk", "com.spectralogic.ds3", "ds3-sdk").withoutVersion()
            library("kotlinBom", "org.jetbrains.kotlin", "kotlin-bom").versionRef("kotlin")
            library("kotlinReflect", "org.jetbrains.kotlin", "kotlin-reflect").withoutVersion()
            library("kotlinStdlib", "org.jetbrains.kotlin", "kotlin-stdlib").withoutVersion()
            library("kotlinxCoroutinesCore", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinxCoroutines")

            // Test-only dependencies
            library("kotlinTestRunnerJunit5", "io.kotlintest", "kotlintest-runner-junit5").versionRef("kotlinTest")
            library("mockk", "io.mockk", "mockk").versionRef("mockk")

            plugin("kotlinJvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("owaspDepCheck","org.owasp.dependencycheck").version("8.1.2")
            plugin("versions", "com.github.ben-manes.versions").version("0.46.0")
        }
    }
}