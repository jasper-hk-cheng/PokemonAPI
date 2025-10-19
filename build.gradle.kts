plugins {
    // android core
    alias(libs.plugins.com.android.application) apply false // AGP
    id("com.android.library") version "8.13.0" apply false

    // kotlin and kotlin tools
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false

    // third party
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
}