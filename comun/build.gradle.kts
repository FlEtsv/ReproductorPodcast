plugins {
    kotlin("jvm") // Version is managed in libs.versions.toml
}

dependencies {
    // Kotlin standard library
    implementation(libs.kotlin.stdlib)

    // JUnit test library
    testImplementation(libs.junit)
}