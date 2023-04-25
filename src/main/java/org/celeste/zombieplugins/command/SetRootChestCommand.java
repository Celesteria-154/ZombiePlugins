package org.celeste.zombieplugins.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.celeste.zombieplugins.ZombiePlugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SetRootCommand(loot)処理コマンド
 *  yamlファイルをロードしその場所をルートチェストに変更
 */
public class SetRootChestCommand extends ZombieSubCommand {

    private static final String COMMAND_NAME = "setlootchest";
    private static final String FILE_NAME = "chest-data.yml";

    private Plugin plugin;
    private File dataFile;
    private YamlConfiguration dataConfig;

    /**
     * コマンドを取得
     * @return コマンド名
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    public SetRootChestCommand() {
        this.plugin = ZombiePlugins.getInstance();
        this.dataFile = new File(plugin.getDataFolder(), "chest-data.yml");
        if (!dataFile.exists()) {
            throw new RuntimeException("The chest-data.yml file does not exist at: " + dataFile.getAbsolutePath());
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    /**
     * コマンド実行
     * @param sender 実行者
     * @param label　実行ラベル
     * @param args　実行引数
     * @return コマンドの実行したか
     */
    public boolean runCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        int chestsConverted = 0;

        System.out.println(dataConfig.saveToString());

        ConfigurationSection chestsSection = dataConfig.getConfigurationSection("chests");
        Set<String> chestKeys = chestsSection.getKeys(false);

        for (String chestKey : chestKeys) {
            ConfigurationSection chestSection = chestsSection.getConfigurationSection(chestKey);
            Location chestLocation = new Location(
                    player.getWorld(),
                    chestSection.getInt("x"),
                    chestSection.getInt("y"),
                    chestSection.getInt("z")
            );

            Block chestBlock = chestLocation.getBlock();
            //loot chestへ置き換え
            if (chestBlock.getType() == Material.CHEST) {
                chestBlock.setType(Material.CHEST);
                Chest chest = (Chest)chestLocation.getBlock().getState();
                chest.setLootTable(Bukkit.getLootTable(NamespacedKey.minecraft("chests/jungle_temple")));
                chest.update();
                chestsConverted++;
            }
        }


        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage("Successfully converted " + chestsConverted + " chests to emerald blocks!");
        return true;
    }


    private void printData() {
        System.out.println("===== chest-data.yml contents =====");
        System.out.println(dataConfig.saveToString());
        System.out.println("===================================");
    }

}
