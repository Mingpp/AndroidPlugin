// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    id("com.ccts.headplugin") version "1.0.0" apply false
    id("com.ccts.highplugin") version "1.0.0" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
//// 应用插件
//apply plugin: 'com.android.application'
//// 或者
//apply plugin: 'com.android.library'
//
//apply plugin: 'com.ccts.headplugin'

buildscript {


    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.0")
        classpath("com.ccts.highplugin:highplugin:1.0.0")
    }
}