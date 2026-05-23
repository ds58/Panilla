rootProject.name = "panilla"

// api
include(":panilla-api")

// bukkit
include(
    ":panilla-craftbukkit-v1_8_R3",
    ":panilla-craftbukkit-v1_12_R1",
    ":panilla-craftbukkit-v1_13_R2",
    ":panilla-craftbukkit-v1_14_R1",
    ":panilla-craftbukkit-v1_15_R1",
    ":panilla-craftbukkit-v1_16_R1",
    ":panilla-craftbukkit-v1_16_R2",
    ":panilla-craftbukkit-v1_16_R3",
    ":panilla-craftbukkit-v1_17_R1",
    ":panilla-craftbukkit-v1_18_R1",
    ":panilla-craftbukkit-v1_18_R2",
    ":panilla-craftbukkit-v1_19_R1",
    ":panilla-craftbukkit-v1_19_R2",
    ":panilla-craftbukkit-v1_19_R3",
    ":panilla-craftbukkit-v1_20_R1",
    ":panilla-craftbukkit-v1_20_R2",
    ":panilla-craftbukkit-v1_20_R3",
    ":panilla-paper-v1_20_6",
    ":panilla-paper-v1_21",
    ":panilla-paper-v1_21_3",
    ":panilla-paper-v1_21_5",
    ":panilla-bukkit"
)

// api
project(":panilla-api").projectDir = file("api")

// bukkit
project(":panilla-craftbukkit-v1_8_R3").projectDir = file("craftbukkit-v1_8_R3")
project(":panilla-craftbukkit-v1_12_R1").projectDir = file("craftbukkit-v1_12_R1")
project(":panilla-craftbukkit-v1_13_R2").projectDir = file("craftbukkit-v1_13_R2")
project(":panilla-craftbukkit-v1_14_R1").projectDir = file("craftbukkit-v1_14_R1")
project(":panilla-craftbukkit-v1_15_R1").projectDir = file("craftbukkit-v1_15_R1")
project(":panilla-craftbukkit-v1_16_R1").projectDir = file("craftbukkit-v1_16_R1")
project(":panilla-craftbukkit-v1_16_R2").projectDir = file("craftbukkit-v1_16_R2")
project(":panilla-craftbukkit-v1_16_R3").projectDir = file("craftbukkit-v1_16_R3")
project(":panilla-craftbukkit-v1_17_R1").projectDir = file("craftbukkit-v1_17_R1")
project(":panilla-craftbukkit-v1_18_R1").projectDir = file("craftbukkit-v1_18_R1")
project(":panilla-craftbukkit-v1_18_R2").projectDir = file("craftbukkit-v1_18_R2")
project(":panilla-craftbukkit-v1_19_R1").projectDir = file("craftbukkit-v1_19_R1")
project(":panilla-craftbukkit-v1_19_R2").projectDir = file("craftbukkit-v1_19_R2")
project(":panilla-craftbukkit-v1_19_R3").projectDir = file("craftbukkit-v1_19_R3")
project(":panilla-craftbukkit-v1_20_R1").projectDir = file("craftbukkit-v1_20_R1")
project(":panilla-craftbukkit-v1_20_R2").projectDir = file("craftbukkit-v1_20_R2")
project(":panilla-craftbukkit-v1_20_R3").projectDir = file("craftbukkit-v1_20_R3")
project(":panilla-paper-v1_20_6").projectDir = file("paper-v1_20_6")
project(":panilla-paper-v1_21").projectDir = file("paper-v1_21")
project(":panilla-paper-v1_21_3").projectDir = file("paper-v1_21_3")
project(":panilla-paper-v1_21_5").projectDir = file("paper-v1_21_5")
project(":panilla-bukkit").projectDir = file("bukkit")

pluginManagement {
    plugins {
        id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    }
}
