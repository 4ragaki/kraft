plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "fun.aragaki.kraft"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
        ndk.abiFilters("arm64-v8a", "x86_64")
    }

    buildTypes {
        getByName("release") {
/*            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )*/
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
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
            version = "3.10.2"
        }
    }
    sourceSets["main"].jniLibs.srcDir("src/main/libs/")

    kotlinOptions {
        jvmTarget = "1.8"
    }
    ndkVersion = "21.3.6528147"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.datastore:datastore:1.0.0-alpha08")

    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("androidx.activity:activity-ktx:1.3.0-alpha06")
    implementation("androidx.fragment:fragment-ktx:1.3.2")

    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")

    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.7.0-alpha02")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-beta03")

    implementation("androidx.room:room-runtime:2.2.6")
    implementation("androidx.room:room-ktx:2.2.6")
    kapt("androidx.room:room-compiler:2.2.6")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation("org.kodein.di:kodein-di:7.1.0")
    implementation("org.kodein.di:kodein-di-framework-android-x:7.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.tickaroo.tikxml:annotation:0.9.3-SNAPSHOT")
    implementation("com.tickaroo.tikxml:core:0.9.3-SNAPSHOT")
    kapt("com.tickaroo.tikxml:processor:0.9.3-SNAPSHOT")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.9.3-SNAPSHOT")

    implementation("me.imid.swipebacklayout.lib:library:1.3.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    implementation("io.coil-kt:coil:1.1.1")
    implementation("io.coil-kt:coil-gif:1.1.1")
    implementation("io.coil-kt:coil-svg:1.1.1")

    implementation("com.airbnb.android:lottie:3.6.1")
    implementation("androidx.webkit:webkit:1.4.0")
}