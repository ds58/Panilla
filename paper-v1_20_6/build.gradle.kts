plugins {
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":panilla-api"))
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    implementation(libs.item.nbt.api)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}