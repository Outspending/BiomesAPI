repositories {
    maven { url = uri("https://repo.skriptlang.org/releases") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    compileOnly(group = "com.github.SkriptLang", name = "Skript", version = "2.7.1").apply {
        isTransitive = false
    }

    implementation(rootProject)
}
