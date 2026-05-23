From current maintainer Lumine1909: I'm considering drop support of 1.20.5- servers, so I add bstats to collect user's version info. If there aren't enough users in those legacy versions, I'll do that in roughly next minor version. Then I will rewrite the plugin to improve its performance and readability, just like what I did [here](https://github.com/Lumine1909/CustomBiomeColors_Continue).

-----

# Panilla
Panilla (the name) is a combination of the word Packet and Vanilla (as in Vanilla Minecraft).

[Support the development of this plugin and purchase a precompiled .jar file on SpigotMC](https://www.spigotmc.org/resources/65694/) (or compile your own for free)


## Overview
Panilla is software to prevent abusive NBT and packets on Minecraft servers.

With this software, you will be able to prevent:

- Unobtainable Enchantments (eg. Sharpness X)
- Unobtainable Potions (eg. Insta-kill)
- Unobtainable Fireworks
- Crash Books
- Crash Signs
- Crash Chests/Shulker Boxes
- Crash Potions (invalid CustomPotionColor\s)
- Oversized packets (which crash the client)
- Long item names/item lore
- Additional "AttributeModifiers" on items (eg. Speed)
- Unbreakable items
- and more abusive NBT

## Supported Platforms
Currently Panilla supports:
- Bukkit
  - CraftBukkit* 1.8.8
  - CraftBukkit* 1.12.x-1.20.4
  - Paper 1.20.5-26.1.2
  
  **CraftBukkit includes any CraftBukkit derivatives (Spigot, Paper, Folia, etc)*

If you would like support for a server platform or Minecraft version that is not listed, please make an issue on GitHub.  

## Compiling
In order for you to compile Panilla, you will need to use [BuildTools, by SpigotMC](https://www.spigotmc.org/wiki/buildtools), and build each version of CraftBukkit which is supported by Panilla.
When you run BuildTools, it will add the dependencies required (CraftBukkit/Bukkit) to your local Maven repository.
From there, you can compile the project with `./gradlew build`. The output plugin jars file will located in the `target/` directory.

Java 21 is required to build Panilla.
