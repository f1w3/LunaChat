# LunaChat Remastered - Channel Chat Plugin

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/f1w3/lunachat/build.yml?style=for-the-badge)](https://github.com/f1w3/LunaChat/releases/tag/dev-build)
[![Latest Release](https://img.shields.io/github/v/release/f1w3/lunachat?style=for-the-badge)](https://github.com/f1w3/lunachat/releases/latest)
[![License](https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge)](https://www.gnu.org/licenses/lgpl-3.0.en.html)  

README - [日本語](https://github.com/f1w3/LunaChat/blob/master/README.md) / [English](https://github.com/f1w3/LunaChat/blob/master/README_en.md)  

## Introduction

This project is a fork of [ucchyocean/LunaChat](https://github.com/ucchyocean/LunaChat).  

For **versions prior to `1.18`**, please use the original project:  

[Github](https://github.com/ucchyocean/LunaChat) / [Spigotmc](https://www.spigotmc.org/resources/lunachat.82293/)  

## Overview

- LunaChat can be installed on `Spigot(Paper) 1.18+` / `BungeeCord`.  
- It converts romanized Japanese input into kanji.  
- It integrates channel chat functionality, with many customizable options.  
- You can set passwords for channel entry or hide channels from the list.  
- Channel moderators can be assigned. Moderators can change channel options, kick, or ban users. For newly created channels, the first person to join becomes the moderator.  
- Messages in channel chat are clickable.  
- The `/ch` command displays help.  
- The `/tell` command is replaced by LunaChat, and its messages are also converted to kanji.  
- The `/r` command allows you to reply to `/tell` messages.  
- Numerous APIs and event hooks are supported.  

### Plugins integratable with LunaChat (Spigot)

- Vault (for Prefix/Suffix plugins)  
- Dynmap  
- Multiverse  
- mcMMO  

### Plugins integratable into LunaChat (Spigot)

- DiscordSRV  

### Plugins integratable with LunaChat (BungeeCord)

- BungeePerms  
- LuckPerms  

## Usage

Place [LunaChat.jar](https://github.com/f1w3/lunachat/releases/latest) into your `plugins` folder and start the server.  

## BStats

<https://bstats.org/plugin/bukkit/LunaChat/7936>  

If you don’t want to send statistics,
set `metricsDisabled` to `true` in `config.yml`.  

## Download

- Stable: [Download Latest](https://github.com/f1w3/lunachat/releases/latest)  
- Development: [dev-build](https://github.com/f1w3/LunaChat/releases/tag/dev-build)  

## Support

Support is provided through [Github issues](https://github.com/f1w3/LunaChat/issues).  

Feel free to reach out.  

## License

This plugin is licensed under **LGPLv3**.  

Please see the [license text](https://www.gnu.org/licenses/lgpl-3.0.en.html) for details.  
