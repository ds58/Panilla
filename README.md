# Panilla
Panilla (the name) is a combination of the word Packet and Vanilla (as in Vanilla Minecraft).

[Support the development of this plugin and purchase a precompiled .jar file on SpigotMC](https://www.spigotmc.org/resources/65694/) (or compile your own for free)

## Overview
Panilla is a plugin for CraftBukkit based Minecraft servers (CraftBukkit, Spigot, Paper, etc) to prevent abusive NBT and potentially harmful packets.

With this plugin, you will be able to prevent:

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

## Why is this plugin needed?
Normally, these NBT tags are not obtainable in vanilla survival-mode Minecraft. With the use of creative-mode, players can use a few different methods to obtain these items such as:
- use a hacked client to edit the NBT
- load a toolbar from a single-player world/other server which has items with edited NBT

This plugin is most useful on creative-mode servers, where players are given creative-mode access. It could also be beneficial for use on survival-mode servers if you just wish to keep your server in-check. Be aware that this plugin does remove NBT from items if it deems they are "illegitimate" (it does not remove the item itself).

## Why have you not used ProtocolLib?
It was a personal decision. I wanted to be as close to Minecraft as possible. It also means users don't have to install an additional plugin.

## Will this plugin work on Minecraft (CraftBukkit) version X?
If X is < Minecraft 1.12.2, the answer is no. If X is listed as a Maven module in the source directory of this plugin, it is likely yes.

## I am using your plugin and have a player that crashes when they login to the server.
Make a GitHub issue on the [project's homepage](https://github.com/Ruinscraft/Panilla). It would be very helpful if you included a player.dat file from your world/playerdata/player-uuid.dat so that I can take a look at their inventory contents.

## Compiling
In order for you to compile Panilla, you will need to use [BuildTools, by SpigotMC](https://www.spigotmc.org/wiki/buildtools), and build each version of CraftBukkit which is supported by Panilla. When you run BuildTools, it will add the dependencies required (CraftBukkit/Bukkit) to your local Maven repository. From there, you can compile the project with `mvn package`. The output plugin jar file will be `plugin/target/Panilla.jar`.
