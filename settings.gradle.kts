pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CryptoTest"
include(":app")
include(":core:ui")
include(":feature:coins")
include(":feature:coin-details")
include(":domain:coins")
include(":core:model")
include(":core:network")
include(":data:coins")
include(":core:database")
include(":feature:coin-list")
include(":core:datastore")
include(":core:navigation")

include(":feature:coin-alert")
include(":domain:alerts")
include(":data:alerts")
include(":feature:live-notification")
include(":domain:live-notification")
include(":data:live-notification")
include(":background:alert-worker")
