
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // または手動指定:
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.djtimer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.djtimer"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        testInstrumentationRunner = "com.google.dagger.hilt.android.testing.HiltTestRunner"
    }

    buildFeatures {
        compose = true
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")

    // Compose UI 基本セット
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.7")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Activity for Compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // Lifecycle + ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    // 折りたたみデバイス対応
    implementation("androidx.window:window:1.2.0")
    implementation("com.google.accompanist:accompanist-adaptive:0.32.0")

    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")
    implementation ("androidx.compose.ui:ui-text-google-fonts:1.6.7")

    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.7")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.6.7")

    androidTestImplementation ("androidx.test:core-ktx:1.6.1")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.navigation:navigation-testing:2.8.2")

    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.52")

    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.6.1")
}