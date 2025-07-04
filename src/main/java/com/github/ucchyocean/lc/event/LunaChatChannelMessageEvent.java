/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.lc.event;

import com.github.ucchyocean.lc.channel.ChannelPlayer;

import java.util.ArrayList;

/**
 * チャンネルチャットのメッセージイベント、
 * このイベントはキャンセルできない。
 *
 * @author ucchy
 * @deprecated Legacy Version
 */
public class LunaChatChannelMessageEvent extends LunaChatBaseEvent {

    private final ChannelPlayer player;
    private final String displayName;
    private final String originalMessage;
    private String message;
    private ArrayList<ChannelPlayer> recipients;

    public LunaChatChannelMessageEvent(String channelName,
                                       ChannelPlayer player, String message, ArrayList<ChannelPlayer> recipients,
                                       String displayName, String originalMessage) {
        super(channelName);
        this.player = player;
        this.message = message;
        this.recipients = recipients;
        this.displayName = displayName;
        this.originalMessage = originalMessage;
    }

    /**
     * 発言したプレイヤー、システムメッセージの場合はnullになることに注意
     *
     * @return player 発言プレイヤー
     */
    public ChannelPlayer getPlayer() {
        return player;
    }

    /**
     * 置き換えされたメッセージ
     *
     * @return message メッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * メッセージを上書き設定する
     *
     * @param message メッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * メッセージを受信するプレイヤーリスト
     *
     * @return recipients プレイヤーリスト
     */
    public ArrayList<ChannelPlayer> getRecipients() {
        return recipients;
    }

    /**
     * メッセージ受信者を上書き設定する
     *
     * @param recipients メッセージ受信者
     */
    public void setRecipients(ArrayList<ChannelPlayer> recipients) {
        this.recipients = recipients;
    }

    /**
     * 発言者の表示名を取得する
     *
     * @return 発言者の表示名
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * オリジナルメッセージ（チャットフォーマットを適用していない状態のメッセージ）を取得する
     *
     * @return オリジナルメッセージ
     */
    public String getOriginalMessage() {
        return originalMessage;
    }
}
