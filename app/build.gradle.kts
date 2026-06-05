plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.cryptotest"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.cryptotest"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.dagger)
    annotationProcessor(libs.dagger.compiler)

    //modules

    implementation(project(":feature:coins"))
    implementation(project(":feature:coin-details"))
    implementation(project(":feature:coin-list"))
    implementation(project(":core:ui"))
    implementation(project(":domain:coins"))
    implementation(project(":core:network"))
    implementation(project(":data:coins"))
    implementation(project(":core:database"))



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}