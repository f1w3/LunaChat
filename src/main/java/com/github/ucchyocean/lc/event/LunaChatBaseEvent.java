/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.lc.event;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 基底イベントクラス
 *
 * @author ucchy
 * @deprecated Legacy Version
 */
public abstract class LunaChatBaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected String channelName;

    /**
     * コンストラクタ
     *
     * @param channelName チャンネル名
     */
    public LunaChatBaseEvent(String channelName) {
        super(!Bukkit.isPrimaryThread());
        this.channelName = channelName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * チャンネル名を取得する
     *
     * @return チャンネル名
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * チャンネルを取得する
     *
     * @return チャンネル
     */
    public Channel getChannel() {
        return LunaChat.getInstance().getLunaChatAPI().getChannel(channelName);
    }
}
