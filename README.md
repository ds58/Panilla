# Panilla
Panilla (the name) is a combination of the word Packet and Vanilla (as in Vanilla Minecraft).

[Support the development of this plugin and purchase a precompiled .jar file on SpigotMC](https://www.spigotmc.org/resources/65694/) (or compile your own for free)

## Overview
Panilla is software to prevent abusive NBT and packets on Minecraft: Java Edition servers.

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

## Compiling
In order for you to compile Panilla, you will need to use [BuildTools, by SpigotMC](https://www.spigotmc.org/wiki/buildtools), and build each version of CraftBukkit which is supported by Panilla.
When you run BuildTools, it will add the dependencies required (CraftBukkit/Bukkit) to your local Maven repository.
From there, you can compile the project with `./gradlew build`. The output plugin jars file will located in the `target/` directory.
