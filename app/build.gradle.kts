import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

// Uncommitted key sources only, in precedence order: Gradle property (-P or
// ~/.gradle/gradle.properties), environment variable, then the gitignored
// local.properties (ORS_API_KEY=...). Never put the key in a committed file.
val orsKeyFromLocalProperties = rootProject.file("local.properties")
    .takeIf { it.exists() }
    ?.inputStream()
    ?.use { stream -> Properties().apply { load(stream) } }
    ?.getProperty("ORS_API_KEY")
    .orEmpty()
val orsApiKey = providers.gradleProperty("ORS_API_KEY")
    .orElse(providers.environmentVariable("ORS_API_KEY"))
    .orElse(orsKeyFromLocalProperties)
    .get()
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")

android {
    namespace = "com.lastwagon.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lastwagon.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0-beta"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "ORS_API_KEY", "\"$orsApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true; buildConfig = true }
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    lint { abortOnError = true; checkReleaseBuilds = false }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:learning"))
    implementation(project(":feature:routing"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
