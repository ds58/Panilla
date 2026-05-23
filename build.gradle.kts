allprojects {
    group = "com.ruinscraft"
    version = "1.13.7"
}

version = "1.13.7"

repositories {
    mavenCentral()
    maven(url = "https://repo.codemc.io/repository/maven-public/")
    maven(url = "https://repo.codemc.io/repository/nms-local/")
    maven(url = "https://repo.codemc.io/repository/nms-remote/")
    maven(url = "https://repo.papermc.io/repository/maven-public/") // Authlib, thank you PaperMC
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven(url = "https://repo.codemc.io/repository/maven-public/")
        maven(url = "https://repo.codemc.io/repository/nms-local/")
        maven(url = "https://repo.codemc.io/repository/nms-remote/")
        maven(url = "https://repo.papermc.io/repository/maven-public/") // Authlib, thank you PaperMC
    }
}

tasks.register<Delete>("clean") {
    delete("./target")
}
