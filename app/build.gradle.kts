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

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10")

        //Material Design
        implementation("com.google.android.material:material:1.9.0")


        //Architectural Components
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")

        //Lifecycle
        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")
        implementation("androidx.lifecycle:lifecycle-runtime:2.8.3")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

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
        val hilt_version = "2.49"
        implementation("com.google.dagger:hilt-android:$hilt_version")
        kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
        kapt("androidx.hilt:hilt-compiler:1.0.0")

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
        implementation("androidx.media3:media3-exoplayer:1.3.1")
        implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
        implementation("androidx.media3:media3-ui:1.3.1")
        implementation ("androidx.media:media:1.6.0")
        implementation ("androidx.appcompat:appcompat:1.6.1")

        //Navigation fragment
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

        //Circle ImgaeView
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
}

