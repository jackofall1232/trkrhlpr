plugins { alias(libs.plugins.android.library) }
android {
    namespace = "com.lastwagon.core.model"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    // Tree-walking JSON parsing for the versioned content pipeline (TruckStopContent);
    // same library and version the routing providers already use. No codegen plugin needed.
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}
