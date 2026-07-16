plugins { alias(libs.plugins.android.library) }
android {
    namespace = "com.lastwagon.core.testing"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
dependencies {
    api(project(":core:model"))
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}

