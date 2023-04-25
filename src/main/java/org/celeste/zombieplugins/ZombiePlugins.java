package org.celeste.zombieplugins;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.celeste.zombieplugins.command.ZombieCommand;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class ZombiePlugins extends JavaPlugin {
    private static ZombiePlugins instance;
    private ZombieCommand zombiecommand;

    public static ZombiePlugins getInstance() {
        return instance;
    }

    //プラグイン起動時に呼び出されるメソッド
    @Override
    public void onEnable() {
        File dataFile = new File(getDataFolder(), "chest-data.yml");
        if (!dataFile.exists()) {
            saveResource("chest-data.yml", false);
        }
        instance = this;
        zombiecommand = new ZombieCommand();  // コマンドの登録
    }

    //コマンド実行時呼び出されるメソッド
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if ( command.getName().equals("ZombieP") ) {
            return zombiecommand.execute(sender, label, args);  // 使い回し
        }
        return false;
    }

    //TAB補完実行時呼び出されるメソッド
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        zombiecommand = new ZombieCommand();
        List<String> completeList = null;
        if (command.getName().equalsIgnoreCase("ZombieP")) {
            completeList = zombiecommand.onTabComplete(sender, label, args);
        }
        if (completeList != null) {
            return completeList;
        }
        return super.onTabComplete(sender, command, label, args);
    }

    //プラグイン終了時呼び出されるメソッド
    @Override
    public void onDisable() {
        //実行中のタスクのキャンセル
        getServer().getScheduler().cancelTasks(this);
        //イベントリスナー解除
        HandlerList.unregisterAll(this);
    }
}
