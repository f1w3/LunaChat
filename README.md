# LunaChat Remastered - チャンネルチャットプラグイン

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/f1w3/lunachat/build.yml?style=for-the-badge)](https://github.com/f1w3/LunaChat/releases/tag/dev-build)
[![Latest Release](https://img.shields.io/github/v/release/f1w3/lunachat?style=for-the-badge)](https://github.com/f1w3/lunachat/releases/latest)
[![License](https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge)](https://www.gnu.org/licenses/lgpl-3.0.ja.html)

README - [日本語](https://github.com/f1w3/LunaChat/blob/master/README.md) / [English](https://github.com/f1w3/LunaChat/blob/master/README_en.md)  

## 初めに

このプロジェクトは[ucchyocean/LunaChat](https://github.com/ucchyocean/LunaChat)のフォークです。

**`1.18`以前のバージョン**等についてはそちらをお使いください。

[Github](https://github.com/ucchyocean/LunaChat) / [Spigotmc](https://www.spigotmc.org/resources/lunachat.82293/)

## 概要

- LunaChatは、`Spigot(Paper) 1.18～最新版` / `BungeeCord`へ導入できます。
- チャットのローマ字入力を、漢字変換することができます。
- チャンネルチャット機能を統合しています。チャンネルには、多種のオプションを設定可能です。
- チャンネルの入室にパスワードを設定したり、チャンネルをリストから非表示にすることができます。
- チャンネルのモデレーターを設定することができます。モデレーターは、チャンネルのオプションを変更したり、チャンネルからのキックやBANができます。作成直後のチャンネルの場合、最初に入室した人がモデレーターになります。
- チャンネルチャットの発言内容は、クリック可能です。
- `/ch`コマンドを使うとヘルプを表示することが出来ます。
- `/tell`コマンドをLunaChatで置き換えます。`/tell`コマンドの内容も、漢字変換されるようになります。
- `/r`コマンドを使って、`/tell`コマンドのメッセージに返信することもできます。
- 多数のAPIやイベント発行機能をサポートしています。

### LunaChat-spigotから連携可能なプラグイン

- Vault (for Prefix/Suffix plugins)
- Dynmap
- Multiverse
- mcMMO

### LunaChat-spigotへ連携可能なプラグイン

- DiscordSRV

### LunaChat-bungeecordから連携可能なプラグイン

- BungeePerms
- LuckPerms

## 使い方

[LunaChat.jar](https://github.com/f1w3/lunachat/releases/latest)をpluginsフォルダに入れて、サーバーを起動してください。

## BStats

<https://bstats.org/plugin/bukkit/LunaChat/7936>

統計情報を送信したくない場合、
`config.yml`にある`metricsDisabled`を`true`にしてください。

## ダウンロード

\- 安定版: [最新版をダウンロード](https://github.com/f1w3/lunachat/releases/latest)  
\- 開発版: [dev-build](https://github.com/f1w3/LunaChat/releases/tag/dev-build)

## サポート

[Github issues](https://github.com/f1w3/LunaChat/issues)の方でサポートをしています。

お気軽にお問い合わせください。

## ライセンス

本プラグインのライセンスは、**LGPLv3**に従います。

ライセンス条文は[こちら](https://www.gnu.org/licenses/lgpl-3.0.ja.html)を参照してください。
