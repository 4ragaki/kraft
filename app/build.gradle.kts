plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id("kotlinx-serialization")
//    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "fun.aragaki.kraft"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
        ndk.abiFilters.add("arm64-v8a")
        ndk.abiFilters.add("x86_64")
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
/*            isMinifyEnabled = true;
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )*/
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    buildFeatures {
        compose = true
    }

    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }

    buildFeatures.viewBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    externalNativeBuild {
        cmake {
            path = File("${projectDir}/src/main/cpp/CMakeLists.txt")
        }
    }
    sourceSets["main"].jniLibs.srcDir("src/main/libs/")

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=compatibility")
    }
    ndkVersion = "22.1.7171670"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-rc01"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
    implementation("androidx.activity:activity-ktx:1.6.0-rc01")
    implementation("androidx.fragment:fragment-ktx:1.5.2")
    implementation("androidx.core:core-ktx:1.9.0-rc01")
    implementation("androidx.appcompat:appcompat:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("androidx.compose.ui:ui:1.3.0-beta01")
    implementation("androidx.activity:activity-compose:1.6.0-rc01")
    implementation("androidx.compose.material3:material3:1.0.0-beta01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.0-beta01")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.0-beta01")
    implementation("androidx.compose.material:material:1.3.0-beta01")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.0-beta01")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.0-beta01")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.2-alpha")
    implementation("com.google.accompanist:accompanist-insets:0.24.2-alpha")
    implementation("com.google.accompanist:accompanist-insets-ui:0.24.2-alpha")
    implementation("com.google.accompanist:accompanist-pager:0.24.2-alpha")
    implementation("androidx.navigation:navigation-compose:2.5.1")
    implementation("com.google.accompanist:accompanist-placeholder:0.24.2-alpha")
    implementation("androidx.paging:paging-compose:1.0.0-alpha16")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.2-alpha")
//    implementation("com.google.accompanist:accompanist-webview:0.24.2-alpha")
    implementation("androidx.glance:glance-appwidget:1.0.0-alpha04")


    implementation("com.google.android.material:material:1.8.0-alpha01")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("androidx.webkit:webkit:1.5.0")
    implementation("androidx.biometric:biometric:1.2.0-alpha04")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0-alpha01")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")

    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.room:room-paging:2.4.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("org.kodein.di:kodein-di:7.11.0")
    implementation("org.kodein.di:kodein-di-framework-android-x:7.11.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.5")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.5")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.tickaroo.tikxml:annotation:0.9.3-SNAPSHOT")
    implementation("com.tickaroo.tikxml:core:0.9.3-SNAPSHOT")
    kapt("com.tickaroo.tikxml:processor:0.9.3-SNAPSHOT")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.9.3-SNAPSHOT")

    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("io.coil-kt:coil:1.4.0")
    implementation("io.coil-kt:coil-gif:1.4.0")
    implementation("io.coil-kt:coil-svg:1.4.0")

    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.airbnb.android:lottie:5.0.1")
    implementation("com.airbnb.android:lottie-compose:5.0.1")
}
