plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(rootProject)
}

kotlin {
    jvmToolchain(17)
}