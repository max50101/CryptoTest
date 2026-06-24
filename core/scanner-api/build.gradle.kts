plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.legacy.kapt) // Возвращаем KAPT для Dagger

    // ВАЖНО: Вместо id("kotlin-parcelize") используем строгий синтаксис compilerPlugin:
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.scanner_api"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    buildFeatures {
        aidl= true
    }
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=kotlinx.parcelize.Parcelize"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Ваши KAPT зависимости (например, для Dagger) добавляются теперь так:
    // kapt(libs.dagger.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
