pluginManagement {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("D:\\release\\repo") // 指定本地仓库路径
        }
//        mavenLocal()
        mavenCentral()
//        gradlePluginPortal()
    }
}
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 使用阿里镜像源
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
//        maven {
//            url = uri("D:\\release\\repo") // 指定本地仓库路径
//        }
        google()
        mavenCentral()
    }
}

//buildscript {
//    repositories {
//        google()
//        jcenter()
//        mavenLocal()
//        maven { url "https://maven.aliyun.com/repository/google" }
//        maven { url "https://maven.aliyun.com/repository/central" }
//    }
//    dependencies {
////        classpath("com.android.tools.build:gradle:3.4.1")
////        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
//    }
//}

rootProject.name = "My Application"
include(":app")
include(":headplugin")
