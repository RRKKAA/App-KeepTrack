plugins {
    alias(libs.plugins.android.application)
    //id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services") // Place it here, inside the plugins {} block
}

android {
    namespace = "com.example.keeptrackbackup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.keeptrackbackup"
        minSdk = 23
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.animation)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.2")
    implementation ("com.google.code.gson:gson:2.10.1")
    annotationProcessor(libs.room.compiler)
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.2")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation (platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
}