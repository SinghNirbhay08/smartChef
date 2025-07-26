plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-parcelize")
}

android {
    namespace = "com.nirbhay.smartchef"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nirbhay.smartchef"
        minSdk = 21
        targetSdk = 35
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

            debug {
                buildConfigField ("String", "SPOONACULAR_API_KEY", "\"YOUR_API_KEY_HERE\"")
                // your other debug config...
            }
            release {
                buildConfigField ("String", "SPOONACULAR_API_KEY", "\"YOUR_API_KEY_HERE\"")
                // your other release config...
            }
        }
        buildFeatures {
            viewBinding = true
            buildConfig = true
        }

        defaultConfig {
            buildConfigField ("String", "GEMINI_API_KEY", "\"${project.findProperty("GEMINI_API_KEY") ?: "AIza........."}\"")
            buildConfigField ("String", "PEXELS_API_KEY", "\"${project.findProperty("PEXELS_API_KEY") ?: "YOUR_API_KEY_HERE"}\"")
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.tracing.perfetto.handshake)
    implementation(libs.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("androidx.cardview:cardview:1.0.0")

    // Retrofit for network calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // RecyclerView & CardView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    implementation ("com.airbnb.android:lottie:6.1.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
}