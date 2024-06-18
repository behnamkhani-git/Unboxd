// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    // if it doesnt suggest to move it to Gradle Catalog Version, then we should do this manually
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" apply false
    alias(libs.plugins.androidLibrary) apply false


    id("androidx.navigation.safeargs") version "2.5.3" apply false
}

