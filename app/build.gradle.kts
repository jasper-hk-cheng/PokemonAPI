// import org.gradle.kotlin.dsl.androidTestImplementation

plugins {
    // android core
    alias(libs.plugins.com.android.application)

    // kotlin and kotlin tools
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.com.google.devtools.ksp)

    // parcelize
    id("kotlin-parcelize")

    // hilt
    alias(libs.plugins.com.google.dagger.hilt.android)

    // navigation
    alias(libs.plugins.androidx.navigation.safeargs)
}

val APP_PACKAGE_NAME = "tw.com.poke.api"

android {
    namespace = APP_PACKAGE_NAME
    // compileSdk {
    //     version = release(36)
    // }
    compileSdk = 36

    defaultConfig {
        applicationId = APP_PACKAGE_NAME
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // release {
        //     isMinifyEnabled = true
        //     proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        // }
        debug {
            isMinifyEnabled = false
            // isCrunchPngs = false
            // gradle.taskGraph.whenReady {
            //     tasks.forEach { task ->
            //         task.enabled = !(task.name.contains("Test") || task.name.contains("Lint"))
            //     }
            // }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        allWarningsAsErrors = false
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    /*
        androidx library
     */
    // core
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
    // view
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation(libs.material)

    /*
        moshi
     */
    val moshiVersion = "1.15.1"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    /*
        compose
     */
    implementation(platform("androidx.compose:compose-bom:2025.10.00"))

    // UI
    implementation("androidx.compose.ui:ui")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // implementation("androidx.compose.ui:ui-text-google-fonts")

    // Design
    implementation("androidx.compose.material:material")

    // Integration of activity, lifecycle, view model, runtime live data, view binding
    implementation("androidx.activity:activity-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    // implementation("androidx.hilt:hilt-navigation-compose")
    // implementation("androidx.compose.runtime:runtime")
    // implementation("androidx.compose.runtime:runtime-livedata")
    // implementation("androidx.compose.runtime:runtime-annotation")
    // implementation("androidx.compose.runtime:runtime-annotation:1.9.0")

    // Constraint Layout
    // implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    /*
        paging
     */
    val pagingVersion="3.3.2"
    implementation("androidx.paging:paging-runtime:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingVersion")

    /*
        hilt - 這裡libs.plugins與libs版本需全部一致 否則會有symbol not found錯誤
     */
    implementation(libs.com.google.dagger.hilt.android)
    ksp(libs.com.google.dagger.hilt.android.compile)

    /*
        navigation
     */
    val navigationVersion = "2.9.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-compose:${navigationVersion}")

    /*
        airbnb
     */
    implementation("com.airbnb.android:lottie:6.3.0")
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    /*
        room
     */
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-paging:2.8.2")
    ksp("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    /*
        retrofit & okHttp3
     */
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    /*
        coil
        https://github.com/coil-kt/coil
        導入時最新版為3.2.0 但最新版有搭配kotlin版本不夠高的問題(需kotlin 2.2.0) 故暫時先用3.0.0
    */
    implementation("io.coil-kt.coil3:coil-compose:3.0.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0")

    /*
        other tools
     */
    implementation("com.jakewharton.timber:timber:5.0.1")

    /*
        testing
     */
    implementation("androidx.test.ext:junit-ktx:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") // RecyclerViewActions
    // androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // androidTestImplementation("androidx.test:rules:1.5.0")
}