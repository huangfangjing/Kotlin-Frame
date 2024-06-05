pluginManagement {
    repositories {
        mavenCentral()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/jetbrains/anko")  //kotlin anko
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven(url = "https://dl.bintray.com/jetbrains/anko")  //kotlin anko
    }
}

rootProject.name = "KotlinFrame"
include(":app", ":mvvm", ":ChartLibrary")
 