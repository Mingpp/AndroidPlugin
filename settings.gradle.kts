pluginManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/google")
//        maven(url = "https://maven.aliyun.com/repository/central")
//        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
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
        maven {
            url = uri("D:\\release\\repo") // 指定本地仓库路径
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(":app")
