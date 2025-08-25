/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.bridge;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.external.vavr.control.Option;

/**
 * MultiverseCore連携クラス
 *
 * @author ucchy
 */
public class MultiverseCoreBridge {

    /**
     * MultiverseCore API クラス
     */
    private final MultiverseCoreApi mvc;

    /**
     * コンストラクタは使用不可
     */
    private MultiverseCoreBridge(MultiverseCoreApi mvc) {
        this.mvc = mvc;
    }

    /**
     * MultiverseCore-apiをロードする
     *
     * @param plugin MultiverseCoreのプラグインインスタンス
     */
    public static MultiverseCoreBridge load(Plugin plugin) {
        try {
            RegisteredServiceProvider<MultiverseCoreApi> rsp =
                    Bukkit.getServicesManager().getRegistration(MultiverseCoreApi.class);
            if (rsp != null) {
                return new MultiverseCoreBridge(rsp.getProvider());
            }
        } catch (Throwable t) {
            if (plugin != null) {
                plugin.getLogger().warning("Multiverse-Core APIの取得に失敗しました: " + t);
            }
        }
        if (plugin != null) {
            plugin.getLogger().severe("Multiverse-Core API が見つかりませんでした。");
        }
        return null;
    }

    /** ワールド名からエイリアス取得（無ければ名前、見つからなければ null） */
    public String getWorldAlias(String worldName) {
        Option<MultiverseWorld> opt = mvc.getWorldManager().getWorld(worldName);
        if (opt.isDefined()) {
            MultiverseWorld mvw = opt.get();
            String alias = mvw.getAlias();
            return (alias != null && !alias.isEmpty()) ? alias : mvw.getName();
        }
        return null;
    }

    /** World からエイリアス取得（無ければ名前、見つからなければ null） */
    public String getWorldAlias(World world) {
        Option<MultiverseWorld> opt = mvc.getWorldManager().getWorld(world);
        if (opt.isDefined()) {
            MultiverseWorld mvw = opt.get();
            String alias = mvw.getAlias();
            return (alias != null && !alias.isEmpty()) ? alias : mvw.getName();
        }
        return null;
    }
}
