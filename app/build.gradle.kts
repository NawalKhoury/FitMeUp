plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fitmeup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fitmeup"
        minSdk = 21
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
        debug {

        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    dependencies {

        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

        implementation("androidx.viewpager2:viewpager2:1.1.0") // Use the latest version available
        implementation("androidx.room:room-runtime:2.5.2")
        annotationProcessor("androidx.room:room-compiler:2.5.2")
        implementation("com.google.code.gson:gson:2.8.8")

        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
        implementation("com.google.android.material:material:1.4.0")  // Or the latest version
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        annotationProcessor("androidx.room:room-compiler:2.5.2")

        // Glide for image loading
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

        // AndroidX Libraries
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.10.0")
        implementation("androidx.activity:activity-ktx:1.7.2")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        // Testing dependencies
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        implementation(libs.constraintlayout)
        implementation(libs.glide)
        annotationProcessor(libs.glide)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    }

    dependencies {
        implementation("com.google.code.gson:gson:2.8.9")
    }
}

