/*
 * App module. Converted from the plain-JVM 'application' plugin to the
 * Android application plugin. No separate Kotlin plugin is applied:
 * AGP 9's built-in Kotlin support handles Kotlin sources automatically,
 * and actually errors out if org.jetbrains.kotlin.android is applied
 * alongside it.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.example.test"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.example.test"
        minSdk = 28
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
