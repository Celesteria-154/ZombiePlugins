package org.celeste.zombieplugins.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.celeste.zombieplugins.ZombiePlugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * コマンド処理クラス
 */
public class ZombieCommand {
    private ArrayList<ZombieSubCommand> commands;
    private ArrayList<ZombieSubCommand> commonCommands;

    private final JavaPlugin plugin;
    private YamlConfiguration dataConfig;
    private final File dataFile;

    /**
     * コンストラクタ
     */
    public ZombieCommand() {
        commands = new ArrayList<ZombieSubCommand>();
        commands.add(new scancommand());
        commands.add(new tempcommand());
        commands.add(new SetRootChestCommand());

        commonCommands = new ArrayList<ZombieSubCommand>();

        plugin = JavaPlugin.getPlugin(ZombiePlugins.class);
        dataFile = new File(plugin.getDataFolder(), "data.yml");

    }

    /**
     * コマンド実行時呼び出されるメソッド
     * @param sender 実行者
     * @param label コマンドラベル
     * @param args コマンド引数
     * @return コマンドが実行したか
     */
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1) {
            for (ZombieSubCommand c : commands) {
                if (c.getCommandName().equalsIgnoreCase(args[0])) {
                    return c.runCommand(sender, label, args);
                }
            }
        }
        return false;
    }

    /**
     * TAB補完が実行されたときに呼び出されるメソッド
     * @param sender TAB補完実行者
     * @param label 実行されたコマンドのラベル
     * @param args 実行されたコマンドの引数
     * @return 補完候補
     */
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            // コマンド名で補完する
            String arg = args[0].toLowerCase();
            ArrayList<String> coms = new ArrayList<String>();
            for (ZombieSubCommand c : commands) {
                if (c.getCommandName().startsWith(arg)) {
                    coms.add(c.getCommandName());
                }
            }
            for (ZombieSubCommand c : commonCommands) {
                if (c.getCommandName().startsWith(arg)) {
                    coms.add(c.getCommandName());
                }
            }
            return coms;
        }
        return new ArrayList<String>();
    }


}
