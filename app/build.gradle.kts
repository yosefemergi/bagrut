plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.real"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.real"
        minSdk = 33
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

    // הוספת התראה עבור Deprecated API
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }

}


dependencies {
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.airbnb.android:lottie:6.3.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation(libs.room.common)
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
