/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package com.github.ucchyocean.lc.channel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * UUID管理のプレイヤー
 *
 * @author ucchy
 * @deprecated Legacy Version
 */
public class ChannelPlayerUUID extends ChannelPlayer {

    private final UUID id;

    /**
     * コンストラクタ
     *
     * @param id プレイヤーID
     */
    public ChannelPlayerUUID(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * コンストラクタ
     *
     * @param id UUID
     */
    public ChannelPlayerUUID(UUID id) {
        this.id = id;
    }

    /**
     * プレイヤー名からUUIDを取得してChannelPlayerUUIDを作成して返す
     *
     * @param name プレイヤー名
     * @return ChannelPlayerUUID
     */
    public static ChannelPlayerUUID getChannelPlayerUUIDFromName(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) {
            return new ChannelPlayerUUID(player.getUniqueId());
        }
        OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
        offline.getUniqueId();
        return new ChannelPlayerUUID(offline.getUniqueId());
    }

    /**
     * CommandSenderから、ChannelPlayerを作成して返す
     *
     * @param sender
     * @return ChannelPlayer
     */
    public static ChannelPlayer getChannelPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return new ChannelPlayerUUID(((Player) sender).getUniqueId());
        }
        return new ChannelPlayerName(sender.getName());
    }

    /**
     * オンラインかどうか
     *
     * @return オンラインかどうか
     */
    @Override
    public boolean isOnline() {
        Player player = Bukkit.getPlayer(id);
        return (player != null);
    }

    /**
     * プレイヤー名を返す
     *
     * @return プレイヤー名
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getName()
     */
    @Override
    public String getName() {
        Player player = Bukkit.getPlayer(id);
        if (player != null) {
            return player.getName();
        }
        OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(id);
        return offlineplayer.getName();
    }

    /**
     * プレイヤー表示名を返す
     *
     * @return プレイヤー表示名
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        Player player = getPlayer();
        if (player != null) {
            return player.getDisplayName();
        }
        return getName();
    }

    /**
     * プレフィックスを返す
     *
     * @return プレフィックス
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getPrefix()
     */
    @Override
    public String getPrefix() {
        return "";
    }

    /**
     * サフィックスを返す
     *
     * @return サフィックス
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getSuffix()
     */
    @Override
    public String getSuffix() {
        return "";
    }

    /**
     * メッセージを送る
     *
     * @param message 送るメッセージ
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#sendMessage(java.lang.String)
     */
    @Override
    public void sendMessage(String message) {
        Player player = getPlayer();
        if (player != null) {
            player.sendMessage(message);
        }
    }

    /**
     * BukkitのPlayerを取得する
     *
     * @return Player
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getPlayer()
     */
    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

    /**
     * 発言者が今いるワールドのワールド名を取得する
     *
     * @return ワールド名
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getWorldName()
     */
    @Override
    public String getWorldName() {
        Player player = getPlayer();
        if (player != null) {
            return player.getWorld().getName();
        }
        return "-";
    }

    /**
     * 発言者が今いる位置を取得する
     *
     * @return 発言者の位置
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getLocation()
     */
    @Override
    public Location getLocation() {
        Player player = getPlayer();
        if (player != null) {
            return player.getLocation();
        }
        return null;
    }

    /**
     * 指定されたパーミッションノードの権限を持っているかどうかを取得する
     *
     * @param node パーミッションノード
     * @return 権限を持っているかどうか
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#hasPermission(java.lang.String)
     */
    @Override
    public boolean hasPermission(String node) {
        Player player = getPlayer();
        if (player == null) {
            return false;
        } else {
            return player.hasPermission(node);
        }
    }

    /**
     * 指定されたパーミッションノードが定義されているかどうかを取得する
     *
     * @param node パーミッションノード
     * @return 定義を持っているかどうか
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#isPermissionSet(java.lang.String)
     */
    @Override
    public boolean isPermissionSet(String node) {
        Player player = getPlayer();
        if (player == null) {
            return false;
        } else {
            return player.isPermissionSet(node);
        }
    }

    /**
     * 指定されたCommandSenderと同一かどうかを返す
     *
     * @param sender
     * @return 同一かどうか
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#equals(org.bukkit.entity.Player)
     */
    @Override
    public boolean equals(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        return id.equals(player.getUniqueId());
    }

    /**
     * IDを返す
     *
     * @return "$" + UUID を返す
     * @see com.github.ucchyocean.lc.channel.ChannelPlayer#getID()
     */
    @Override
    public String toString() {
        return "$" + id.toString();
    }
}
