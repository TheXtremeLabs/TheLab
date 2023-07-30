plugins {
    id("thelab.android.library")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
}

android {
    namespace = "com.riders.thelab.core.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.kotlinx.coroutines.android)
}
