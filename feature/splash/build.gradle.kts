plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.guicarneirodev.hoopreel.feature.splash"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = false
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:highlights"))

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.material.compose)

    // ViewModel
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)

    // Coil para carregamento de imagens
    implementation(libs.coil.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

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
    testImplementation(libs.kotest.runner.junit4)

    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)
}

// Define a tarefa específica para testes JUnit
tasks.register<Test>("testJUnit") {
    // Usar JUnit 4
    useJUnit()

    // Incluir apenas os testes JUnit
    include("**/*JUnitSplashViewModelTest.class")

    // Configurações adicionais
    systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

// Define uma tarefa específica para testes Kotest
tasks.register<Test>("testKotest") {
    // Usar JUnit Platform (necessário para Kotest)
    useJUnitPlatform()

    // Incluir apenas os testes Kotest
    include("**/*KotestSplashViewModelTest.class")

    // Configurações adicionais
    systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

// Configura a tarefa padrão de teste para usar JUnit Platform (para os testes Kotest)
tasks.withType<Test>().configureEach {
    if (name.contains("test") && !name.contains("JUnit") && !name.contains("Kotest")) {
        useJUnitPlatform()
        systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
        jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
}