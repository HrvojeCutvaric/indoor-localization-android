plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.env.flavors)
}

android {
    namespace = "co.be4you.indoorlocalization"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "co.be4you.indoorlocalization"
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
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coil)
    implementation(project(":core"))
    implementation(project(":password_login"))
    implementation(project(":otp_login"))
    implementation(libs.colorpicker.compose.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.core.splashscreen)
}
