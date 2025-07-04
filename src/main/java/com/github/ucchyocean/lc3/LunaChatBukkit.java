/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3;

import com.github.ucchyocean.lc3.bridge.DynmapBridge;
import com.github.ucchyocean.lc3.bridge.McMMOBridge;
import com.github.ucchyocean.lc3.bridge.MultiverseCoreBridge;
import com.github.ucchyocean.lc3.bridge.VaultChatBridge;
import com.github.ucchyocean.lc3.bukkit.BukkitEventListener;
import com.github.ucchyocean.lc3.bukkit.BukkitEventSender;
import com.github.ucchyocean.lc3.channel.ChannelManager;
import com.github.ucchyocean.lc3.command.LunaChatCommand;
import com.github.ucchyocean.lc3.command.LunaChatJapanizeCommand;
import com.github.ucchyocean.lc3.command.LunaChatMessageCommand;
import com.github.ucchyocean.lc3.command.LunaChatReplyCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
 * LunaChatのBukkit実装
 *
 * @author ucchy
 */
public class LunaChatBukkit extends JavaPlugin implements PluginInterface {

    private static LunaChatBukkit instance;

    private LunaChatConfig config;
    private ChannelManager manager;
    private UUIDCacheData uuidCacheData;

    private VaultChatBridge vaultchat;
    private DynmapBridge dynmap;
    private MultiverseCoreBridge multiverse;

    private BukkitTask expireCheckerTask;
    private LunaChatLogger normalChatLogger;

    private LunaChatCommand lunachatCommand;
    private LunaChatMessageCommand messageCommand;
    private LunaChatReplyCommand replyCommand;
    private LunaChatJapanizeCommand lcjapanizeCommand;

    /**
     * LunaChatのインスタンスを返す
     *
     * @return LunaChat
     */
    public static LunaChatBukkit getInstance() {
        if (instance == null) {
            instance = (LunaChatBukkit) Bukkit.getPluginManager().getPlugin("LunaChat");
        }
        return instance;
    }

    /**
     * プラグインが有効化されたときに呼び出されるメソッド
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {
        LunaChat.setPlugin(this);
        LunaChat.setMode(LunaChatMode.BUKKIT);

        // Metrics
        Metrics metrics = new Metrics(this, 7936);
        metrics.addCustomChart(new Metrics.DrilldownPie(
                "minecraft_server_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> sub = new HashMap<>();
            sub.put(Bukkit.getVersion(), 1);
            map.put(Bukkit.getName(), sub);
            return map;
        }));

        // 変数などの初期化
        config = new LunaChatConfig(getDataFolder(), getFile());
        uuidCacheData = new UUIDCacheData(getDataFolder());
        Messages.initialize(new File(getDataFolder(), "messages"), getFile(), config.getLang());
        manager = new ChannelManager();
        normalChatLogger = new LunaChatLogger("==normalchat");

        // チャンネルチャット無効なら、デフォルト発言先をクリアする(see issue #59)
        if (!config.isEnableChannelChat()) {
            manager.removeAllDefaultChannels();
        }

        // Vault のロード
        Plugin temp = getServer().getPluginManager().getPlugin("Vault");
        if (temp != null) {
            vaultchat = VaultChatBridge.load(temp);
        }

        // Dynmap のロード
        temp = getServer().getPluginManager().getPlugin("dynmap");
        if (temp != null) {
            dynmap = DynmapBridge.load(temp);
            if (dynmap != null) {
                getServer().getPluginManager().registerEvents(dynmap, this);
            }
        }

        // MultiverseCore のロード
        temp = getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (temp != null) {
            multiverse = MultiverseCoreBridge.load(temp);
        }

        // mcMMOのロード
        if (getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            getServer().getPluginManager().registerEvents(new McMMOBridge(), this);
        }

        // リスナーの登録
        getServer().getPluginManager().registerEvents(new BukkitEventListener(), this);

        // コマンドの登録
        lunachatCommand = new LunaChatCommand();
        messageCommand = new LunaChatMessageCommand();
        replyCommand = new LunaChatReplyCommand();
        lcjapanizeCommand = new LunaChatJapanizeCommand();

        // 期限チェッカータスクの起動
        expireCheckerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, new ExpireCheckTask(), 100, 600);

        // イベント実行クラスの登録
        LunaChat.setEventSender(new BukkitEventSender());

        // プラグインチャンネル登録
        getServer().getMessenger().registerOutgoingPluginChannel(this, LunaChat.PMC_MESSAGE);
    }

    /**
     * プラグインが無効化されたときに呼び出されるメソッド
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public void onDisable() {
        // 期限チェッカータスクの停止
        if (expireCheckerTask != null) {
            expireCheckerTask.cancel();
        }
    }

    /**
     * コマンド実行時に呼び出されるメソッド
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(
            CommandSender sender, Command command, String label, String[] args) {

        return switch (command.getName()) {
            case "lunachat" -> lunachatCommand.execute(ChannelMember.getChannelMember(sender), label, args);
            case "tell" -> messageCommand.execute(ChannelMember.getChannelMember(sender), label, args);
            case "reply" -> replyCommand.execute(ChannelMember.getChannelMember(sender), label, args);
            case "japanize" -> lcjapanizeCommand.execute(ChannelMember.getChannelMember(sender), label, args);
            default -> false;
        };

    }

    /**
     * TABキー補完が実行されたときに呼び出されるメソッド
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public List<String> onTabComplete(
            CommandSender sender, Command command, String label, String[] args) {

        List<String> completeList = null;
        if (command.getName().equals("lunachat")) {
            completeList = lunachatCommand.onTabComplete(ChannelMember.getChannelMember(sender), label, args);
        }
        if (completeList != null) {
            return completeList;
        }
        return super.onTabComplete(sender, command, label, args);
    }

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     *
     * @return Jarファイル
     */
    public File getPluginJarFile() {
        return getFile();
    }

    /**
     * LunaChatAPIを取得する
     *
     * @return LunaChatAPI
     */
    public LunaChatAPI getLunaChatAPI() {
        return manager;
    }

    /**
     * LunaChatConfigを取得する
     *
     * @return LunaChatConfig
     */
    public LunaChatConfig getLunaChatConfig() {
        return config;
    }

    /**
     * VaultChat連携クラスを返す
     *
     * @return VaultChatBridge
     */
    public VaultChatBridge getVaultChat() {
        return vaultchat;
    }

    /**
     * Dynmap連携クラスを返す
     *
     * @return DynmapBridge
     */
    public DynmapBridge getDynmap() {
        return dynmap;
    }

    /**
     * MultiverseCore連携クラスを返す
     *
     * @return MultiverseCoreBridge
     */
    public MultiverseCoreBridge getMultiverseCore() {
        return multiverse;
    }

    /**
     * 通常チャット用のロガーを返す
     *
     * @return normalChatLogger
     */
    public LunaChatLogger getNormalChatLogger() {
        return normalChatLogger;
    }

    /**
     * 通常チャット用のロガーを設定する
     *
     * @param normalChatLogger normalChatLogger
     */
    protected void setNormalChatLogger(LunaChatLogger normalChatLogger) {
        this.normalChatLogger = normalChatLogger;
    }

    /**
     * オンラインのプレイヤー名一覧を取得する
     *
     * @return オンラインのプレイヤー名一覧
     */
    @Override
    public Set<String> getOnlinePlayerNames() {
        Set<String> list = new TreeSet<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            list.add(p.getName());
        }
        return list;
    }

    /**
     * このプラグインのログを記録する
     *
     * @param level ログレベル
     * @param msg   ログメッセージ
     */
    @Override
    public void log(Level level, String msg) {
        getLogger().log(level, msg);
    }

    /**
     * UUIDキャッシュデータを取得する
     *
     * @return UUIDキャッシュデータ
     * @see com.github.ucchyocean.lc3.PluginInterface#getUUIDCacheData()
     */
    @Override
    public UUIDCacheData getUUIDCacheData() {
        return uuidCacheData;
    }

    /**
     * 非同期タスクを実行する
     *
     * @param task タスク
     * @see com.github.ucchyocean.lc3.PluginInterface#runAsyncTask(java.lang.Runnable)
     */
    @Override
    public void runAsyncTask(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this, task);
    }

    /**
     * プラグインメッセージを送信する
     *
     * @param bytes 送信内容
     */
    public void sendPluginMessage(byte[] bytes) {
        getServer().sendPluginMessage(this, LunaChat.PMC_MESSAGE, bytes);
    }
}
