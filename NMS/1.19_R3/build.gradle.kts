plugins {
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")

    compileOnly(project(":NMS:Wrapper"))
}