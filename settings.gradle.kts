rootProject.name = "leopard"

include(":androidApp")
include(":shared")
include(":desktopApp")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
//        maven(url = "https://api.mapbox.com/downloads/v2/releases/maven") {
//            authentication {
//                create<BasicAuthentication>("basic")
//            }
//            credentials {
//                username = "mapbox"
//                password = "sk.eyJ1Ijoia2FzcmFkZXZlbG9wZXIiLCJhIjoiY2xvMWQycGNpMWtodDJycW9zdTU4OW0ybiJ9.3AERMLpW9covtn043gsBQQ"
//            }
//        }
        maven(url = "https://download2.dynamsoft.com/maven/aar")
    }
}
