import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.ksp)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.mknishad.polarbear"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.mknishad.polarbear"
    minSdk = 26
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"
    val p = Properties()
    p.load(project.rootProject.file("local.properties").reader())
    val apiKey: String = p.getProperty("apiKey")
    buildConfigField("String", "API_KEY", apiKey)
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  implementation(libs.coroutines.lifecycle.viewmodel)
  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.android.compiler)
  ksp(libs.hilt.compiler)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.retrofit2.retrofit)
  implementation(libs.retrofit2.converter.gson)
  implementation(libs.okhttp3.okhttp)
  implementation(libs.okhttp3.logging.interceptor)
  implementation(libs.coil.compose)
  implementation(libs.coil.okhttp)
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
  implementation(libs.room.ktx)
  implementation(libs.room.paging)
  implementation(libs.paging.runtime)
  implementation(libs.paging.compose)
}