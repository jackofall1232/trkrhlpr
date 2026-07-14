plugins { alias(libs.plugins.android.library) }
android {
    namespace = "com.trkrhlpr.core.model"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}
