plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.coin_details"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    buildFeatures{
        viewBinding=true
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
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)
    implementation(libs.dagger)
    implementation(libs.vico.views)
    annotationProcessor(libs.dagger.compiler)
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))
    implementation(project(":domain:coins"))
    implementation(project(":domain:live-notification"))
    implementation(project(":feature:live-notification"))
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}