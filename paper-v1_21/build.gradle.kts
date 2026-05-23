plugins {
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":panilla-api"))
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    implementation(libs.item.nbt.api)
    compileOnly("me.lucko:spark-paper:1.10.119-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}