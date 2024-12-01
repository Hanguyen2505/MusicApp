plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("kotlin-kapt")
    id("kotlin-android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.onlinemusicstreamapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.onlinemusicstreamapp"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.kotlin.stdlib.jdk7)

        //Material Design
        implementation(libs.material.v190)

        //google play service
        implementation(libs.play.services.maps)

        //Architecture Components
        implementation(libs.androidx.lifecycle.viewmodel.ktx)

        //Lifecycle
        implementation(libs.androidx.lifecycle.extensions)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.runtime)
        implementation(libs.androidx.lifecycle.runtime.ktx)

        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

        //Coroutine LifeCycle Scope
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

        //Navigation Components
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

        //Glide
        implementation("com.github.bumptech.glide:glide:4.15.1")
        kapt("com.github.bumptech.glide:compiler:4.15.1")

        //Activity KTX for ViewModel()
        implementation("androidx.activity:activity-ktx:1.9.0")

        //Dagger Hilt
        implementation(libs.hilt.android)
        kapt(libs.hilt.android.compiler)
        kapt(libs.androidx.hilt.compiler)

        //Timber
        implementation("com.jakewharton.timber:timber:5.0.1")

        //Firebase Authentication
        implementation("com.google.firebase:firebase-auth:23.0.0")
        implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
        implementation ("com.google.android.gms:play-services-auth:21.2.0")

        //Firebase Firestore
        implementation("com.google.firebase:firebase-firestore:25.0.0")

        //Firebase Storage KTX
        implementation("com.google.firebase:firebase-storage-ktx:21.0.0")

        //Firebase Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

        // Import the BoM for the Firebase platform
        implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

        // Also add the dependency for the Google Play services library and specify its version
        implementation("com.google.android.gms:play-services-auth:21.2.0")

        //ExoPlayer
        implementation(libs.androidx.media3.exoplayer)
        implementation(libs.androidx.media3.exoplayer.dash)
        implementation(libs.androidx.media3.ui)
        implementation (libs.androidx.media)
        implementation (libs.androidx.appcompat)

        //Navigation fragment
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

        //Circle ImageView
        implementation(libs.circleimageview)

        implementation ("androidx.palette:palette:1.0.0")

        //DrawerLayout
        implementation ("androidx.drawerlayout:drawerlayout:1.1.1")


        //Insetters
//        implementation (libs.insetter.ktx)
    }

    kapt {
        correctErrorTypes = true
    }
}
dependencies {
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.firebase.database.ktx)
}

