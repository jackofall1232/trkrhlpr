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

rootProject.name = "lastwagon"

include(
    ":app",
    ":core:model",
    ":core:data",
    ":core:designsystem",
    ":core:testing",
    ":feature:dashboard",
    ":feature:learning",
    ":feature:routing",
)
