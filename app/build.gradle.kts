import java.lang.module.ModuleFinder.compose

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.notebook"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.notebook"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    // Retrofit + Gson + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}