/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.bridge;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Vault-Chat連携クラス
 *
 * @author ucchy
 */
public class VaultChatBridge {

    /**
     * vault-chatクラス
     */
    private Chat chatPlugin;

    /**
     * コンストラクタは使用不可
     */
    private VaultChatBridge() {
    }

    /**
     * vault-chatをロードする
     *
     * @return ロードしたブリッジのインスタンス
     */
    public static VaultChatBridge load() {

        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            VaultChatBridge bridge = new VaultChatBridge();
            bridge.chatPlugin = chatProvider.getProvider();
            return bridge;
        }

        return null;
    }

    /**
     * プレイヤーのprefixを取得します。
     *
     * @param player プレイヤー
     * @return プレイヤーのprefix
     */
    public String getPlayerPrefix(Player player) {
        String prefix = chatPlugin.getPlayerPrefix(player);
        return (prefix != null) ? prefix : "";
    }

    /**
     * プレイヤーのsuffixを取得します。
     *
     * @param player プレイヤー
     * @return プレイヤーのsuffix
     */
    public String getPlayerSuffix(Player player) {
        String suffix = chatPlugin.getPlayerSuffix(player);
        return (suffix != null) ? suffix : "";
    }
}
