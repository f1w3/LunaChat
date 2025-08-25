/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.bridge;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.channel.Channel;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapWebChatEvent;

/**
 * dynmap連携クラス
 *
 * @author ucchy
 */
public class DynmapBridge implements Listener {

    /**
     * dynmap-apiクラス
     */
    private final DynmapAPI dynmap;

    /**
     * コンストラクタは使用不可
     */
    private DynmapBridge(DynmapAPI dynmapapi) {
        this.dynmap = dynmapapi;
    }

    /**
     * dynmap-apiをロードする
     *
     * @return ロードしたブリッジのインスタンス
     */
    public static DynmapBridge load() {

        RegisteredServiceProvider<DynmapAPI> dynmapAPIProvider =
                Bukkit.getServicesManager().getRegistration(DynmapAPI.class);
        if (dynmapAPIProvider != null) {
            return new DynmapBridge(dynmapAPIProvider.getProvider());
        }

        return null;
    }

    /**
     * dynmapにプレイヤーのチャットを流す
     *
     * @param player  プレイヤー
     * @param message 発言内容
     */
    public void chat(Player player, String message) {

        dynmap.postPlayerMessageToWeb(player, message);
    }

    /**
     * dynmapにブロードキャストメッセージを流す
     *
     * @param message メッセージ
     */
    public void broadcast(String message) {

        dynmap.sendBroadcastToWeb(null, message);
    }

    /**
     * DynmapのWebUIからチャット発言されたときのイベント
     *
     * @param event DynmapWebChatEvent
     */
    @EventHandler
    public void onDynmapWebChat(DynmapWebChatEvent event) {

        LunaChatAPI api = LunaChat.getAPI();
        LunaChatConfig config = LunaChat.getConfig();
        String dchannel = config.getDynmapChannel();
        Channel channel = null;

        if (!dchannel.isEmpty()) {
            // dynmapChannelが設定されている場合
            channel = api.getChannel(dchannel);

        } else {
            String gchannel = config.getGlobalChannel();
            if (!gchannel.isEmpty()) {
                // dynmapChannelが設定されていなくて、
                // globalChannelが設定されている場合
                channel = api.getChannel(gchannel);

            }
        }

        if (channel != null) {
            // チャンネルへ送信
            channel.chatFromOtherSource(
                    event.getName(), event.getSource(), event.getMessage());
            event.setProcessed();
            dynmap.sendBroadcastToWeb(null, event.getMessage());
        }
    }
}
