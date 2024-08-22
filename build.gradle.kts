// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    id("com.ccts.headplugin") version "1.0.0" apply false
}
//// 应用插件
//apply plugin: 'com.android.application'
//// 或者
//apply plugin: 'com.android.library'
//
//apply plugin: 'com.ccts.headplugin'

buildscript {

//    repositories {
//
//
//        // 如果插件发布在自定义的 Maven 仓库
//        maven {
//            url = uri("D:\\release\\repo") // 指定本地仓库路径
//        }
//    }
//    dependencies {
//        classpath("com.ccts.headplugin:1.0.0")
//    }
}