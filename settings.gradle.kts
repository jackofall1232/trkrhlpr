pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "trkrhlpr"

include(
    ":app",
    ":core:model",
    ":core:data",
    ":core:designsystem",
    ":core:testing",
    ":feature:dashboard",
    ":feature:learning",
)

