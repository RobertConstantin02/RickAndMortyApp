plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.example.feature_favorites"
    compileSdk = 34
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =  libs.versions.kotlinCompilerExtensionVersion.get()
    }
}
kapt {
    correctErrorTypes = true
}
dependencies {
    implementation(project(":toplevel:designsystem"))
    implementation(project(":toplevel:navigationlogic"))
    implementation(project(":toplevel:resources"))
    implementation(project(":presentation:presentation_model"))
    implementation(project(":presentation:presentation_mapper"))
    implementation(project(":presentation:common"))
    implementation(project(":domain:domain_model"))
    implementation(project(":domain:usecase"))

    libs.bundles.apply {
        implementation(compose)
        implementation(hilt)
        implementation(coroutines)
        implementation(lifecycle)
        implementation(arrow)
    }
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.common)
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.test)
}