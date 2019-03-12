# Panilla
Panilla (the name) is a combination of the word Packet and Vanilla (as in Vanilla Minecraft).

## Overview
Panilla is a plugin for CraftBukkit based Minecraft servers (Craftbukkit, Spigot, Paper, etc) to prevent abusive NBT and potentially harmful packets.

With this plugin, you will be able to prevent:

- Unobtainable Enchantments (eg. Sharpness X)
- Unobtainable Potions (eg. Insta-kill)
- Unobtainable Fireworks
- Crash Books
- Crash Chests/Shulker Boxes
- Oversized packets (which crash the client)
- Long item names/item lore
- Additional "AttributeModifiers" on items (eg. Speed)
- Unbreakable items
- and more abusive NBT

## Why is this plugin needed?
Normally, these NBT tags are not obtainable in vanilla survival-mode Minecraft. With the use of creative-mode, players can use a few different methods to obtain these items such as:
- use a hacked client to edit the NBT
- load a toolbar from a single-player world which has edited NBT

This plugin is most useful on creative-mode servers, where players are given creative-mode access. Typically, anti-cheat plugins do not handle these types of "cheats."

## Why have you not used ProtocolLib?
It was a personal decision. I wanted to be as close to Minecraft as possible. It also means users don't have to install an additional plugin.

## Will this plugin work on Minecraft (CraftBukkit) version X?
If X is < Minecraft 1.12.2, the answer is no. If X is listed as a Maven module in the source directory of this plugin, it is likely yes.

## I am using your plugin and have a player that crashes when they login to the server.
Make a GitHub issue on the project's homepage (https://github.com/Ruinscraft/Panilla). It would be very helpful if you included a player.dat file from your world/playerdata/player-uuid.dat so that I can take a look at their inventory contents.
