plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    `maven-publish`
    java
}



allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "com.github.Outspending"
    version = "0.0.1"



    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"

    }

    // The file for publication.
    val reobfBuildFile = project.layout.buildDirectory.file("libs/${project.name}-${project.version}.jar")

    // Check if a project has the reobfJar task.
    fun Project.usesReobfuscatedJar() : Boolean {
        return try {
            tasks.named("reobfJar")
            true
        } catch (ignored: UnknownTaskException) {
            false
        }
    }

    // needs to be in after evaluate, otherwise the project's reobfJar task (if it exists)
    // won't be there.
    afterEvaluate {

        // publish NMS apis, and the main project API
        // this is required because consumers require getting the POM
        // files from this project's dependencies, even if they"re completely
        // shaded and included in this file.
        //
        // not optimal because this leads to more network calls when downloading
        // the api for the first time. ideally we only publish the main api.
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = "com.github.Outspending"
                    artifactId = project.name

                    if (usesReobfuscatedJar()) artifact(reobfBuildFile)
                    from(components["java"])
                }
            }
        }

        // Throws an error if we don"t explicitly say we're using the output
        // of reobfJar in the final build.
        tasks.named("publishMavenPublicationToMavenLocal") {
            if (usesReobfuscatedJar()) {
                dependsOn(tasks.reobfJar)
            }
        }

        // Don't use the task if it doesn't exist on this project
        if (usesReobfuscatedJar()) {
            tasks.reobfJar {
                outputJar.set(reobfBuildFile)
            }
        }

        // Run reobfJar on applicable projects
        tasks.assemble {
            if (usesReobfuscatedJar()) dependsOn(tasks.reobfJar)
        }
    }

    repositories {
        mavenCentral()
        maven {
            name = "papermc-repo"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
    }
}


val nmsVersions = listOf("1.19_R2", "1.19_R3", "1.20_R1", "1.20_R2", "1.20_R3")

dependencies {
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")

    implementation(project(":NMS:Wrapper"))
    for (version in nmsVersions) {
        implementation(project(path = ":NMS:${version}", configuration = "reobf"))
    }
}


tasks.wrapper {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.ALL
}