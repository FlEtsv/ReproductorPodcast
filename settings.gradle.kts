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
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.android" -> useVersion("1.9.0")
                "com.android.application" -> useVersion("8.4.0")
                "com.android.library" -> useVersion("8.4.0")
                // Add other plugins here, referring to the versions defined in libs.versions.toml
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Navegacion"
include(":app")
include(":comun")
//include(":audioPlayerLibrary")
include(":mylibrary")
