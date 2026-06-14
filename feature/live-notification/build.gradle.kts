plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.feature.live_notification"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.dagger)
    annotationProcessor(libs.dagger.compiler)
    implementation(libs.glide)
    implementation(project(":domain:coins"))
    implementation(project(":core:model"))
    implementation(project(":domain:live-notification"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}