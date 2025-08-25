/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.bridge;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatBukkit;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.bukkit.BukkitRecipientChatJapanizeTask;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.util.Utility;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mcMMO連携クラス
 */
public class McMMOBridge implements Listener {

    /**
     * mcMMOのパーティチャットが発生したときのイベント
     */
    @EventHandler
    public void onMcMMOPartyChatEvent(McMMOPartyChatEvent event) {

        // mcMMOから、パーティのメンバーを取得する
        List<Player> recipients = PartyAPI.getOnlineMembers(event.getParty());

        String message = event.getMessage();

        // ---- 送信者を反射で解決（getSender()/getPlayer()/getAuthor()/get*Name() などに対応）----
        CommandSender sender = resolveMcMMOSender(event);
        if (sender == null) {
            // 送信者が取れない場合は、変換等せずメッセージだけ反映して終了
            event.setMessage(message);
            return;
        }
        ChannelMember player = ChannelMember.getChannelMember(sender);

        LunaChatConfig config = LunaChat.getConfig();
        LunaChatAPI api = LunaChat.getAPI();

        // NGワード発言をマスク
        for (Pattern pattern : config.getNgwordCompiled()) {
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                message = matcher.replaceAll(
                        Utility.getAstariskString(matcher.group(0).length()));
            }
        }

        // カラーコード置き換え（権限持ちのみ）
        if (config.isEnableNormalChatColorCode() && player.hasPermission("lunachat.allowcc")) {
            message = Utility.replaceColorCode(message);
        }

        // 一時的Japanizeスキップ指定（先頭マーカー）
        boolean skipJapanize = false;
        String marker = config.getNoneJapanizeMarker();
        if (!marker.isEmpty() && message.startsWith(marker)) {
            skipJapanize = true;
            message = message.substring(marker.length());
        }

        // 2byte文字や半角カナが含まれる場合はJapanizeを行わない
        String kanaTemp = Utility.stripColorCode(message);
        if (!skipJapanize &&
                (kanaTemp.getBytes(StandardCharsets.UTF_8).length > kanaTemp.length()
                        || kanaTemp.matches("[ \\uFF61-\\uFF9F]+"))) {
            skipJapanize = true;
        }

        // Japanize変換と、発言処理
        if (!skipJapanize
                && api.isPlayerJapanize(player.getName())
                && config.getJapanizeType() != JapanizeType.NONE) {

            int lineType = config.getJapanizeDisplayLine();

            if (lineType == 1) {
                String taskFormat = Utility.replaceColorCode(config.getJapanizeLine1Format());
                String japanized = api.japanize(kanaTemp, config.getJapanizeType());
                if (japanized != null) {
                    String temp = taskFormat.replace("%msg", message);
                    message = temp.replace("%japanize", japanized);
                }
            } else {
                String taskFormat = Utility.replaceColorCode(config.getJapanizeLine2Format());
                BukkitRecipientChatJapanizeTask task = new BukkitRecipientChatJapanizeTask(
                        message, config.getJapanizeType(), player, taskFormat, recipients);
                // 発言処理を必ず先に実施させるため、遅延を入れてタスクを実行
                int wait = config.getJapanizeWait();
                task.runTaskLater(LunaChatBukkit.getInstance(), wait);
            }
        }

        // 発言内容をイベントに反映
        event.setMessage(message);
    }

    /**
     * mcMMOのイベントから送信者を版差に依存せず取得するヘルパー。
     * 利用可能なメソッドを順に探し、CommandSender/Player/名前(String)の各パターンを処理。
     */
    private static CommandSender resolveMcMMOSender(Object event) {
        Object o;

        // 1) getSender()
        o = invokeNoArg(event, "getSender");
        if (o instanceof CommandSender cs) return cs;

        // 2) getPlayer()
        o = invokeNoArg(event, "getPlayer");
        if (o instanceof Player p2) return p2;

        // 3) getAuthor()
        o = invokeNoArg(event, "getAuthor");
        if (o instanceof CommandSender cs2) return cs2;

        // 4) 名前系: getSenderName / getPlayerName / getAuthorName
        for (String nameGetter : new String[]{"getSenderName", "getPlayerName", "getAuthorName"}) {
            o = invokeNoArg(event, nameGetter);
            if (o instanceof String s && !s.isEmpty()) {
                Player p = Bukkit.getPlayerExact(s);
                if (p != null) return p;
            }
        }
        return null;
    }

    private static Object invokeNoArg(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            m.setAccessible(true);
            return m.invoke(target);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
