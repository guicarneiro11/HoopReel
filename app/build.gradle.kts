plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.guicarneirodev.hoopreel"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guicarneirodev.hoopreel"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    configurations.all {
        resolutionStrategy {
            force("androidx.core:core-ktx:1.13.0")
        }
    }
    implementation(project(":core"))
    implementation(project(":feature:player"))
    implementation(project(":feature:highlights"))
    implementation(project(":feature:search"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:splash"))

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)
    implementation (libs.accompanist.systemuicontroller)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)

    // DI Base
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // WorkManager
    implementation(libs.work.runtime)
    implementation(libs.koin.workmanager)

    // Ícones
    implementation(libs.material.icons.extended)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.robolectric)

    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)
}

tasks.withType<Test> {
    useJUnitPlatform()
}