import com.plugins.AndroidX
import com.plugins.Anko
import com.plugins.BuildConfig
import com.plugins.Depend
import com.plugins.Navgation
import com.plugins.Retrofit

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    `maven-publish`
}

android {
    compileSdk = BuildConfig.compileSdkVersion
    namespace = "com.aleyn.mvvm"
    defaultConfig {
        minSdk = BuildConfig.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(11)
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = "com.aleyn"
                artifactId = "mvvm"
                version = "1.0.0"
            }
        }
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //androidx
    AndroidX.values.forEach { api(it) }
    //network
    Retrofit.values.forEach { api(it) }
    //Navgation
    Navgation.values.forEach { api(it) }
    //material-dialogs
    api(Depend.dialogs)
    api(Depend.dialogsCore)
    //coil
    api(Depend.coil)
    // utils 集合了大量常用的工具类
    api(Depend.utilCode)

    api(Depend.banner)
    api(Depend.BRVAH)
    api(Depend.refreshKernel)
    api(Depend.refreshHeader)

}