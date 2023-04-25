package org.celeste.zombieplugins.command;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.celeste.zombieplugins.ZombiePlugins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * scancommand処理クラス
 * ワールド内?のチェストの位置を出力
 * 移動すると変動するのでもしかしたらserver設定のチャンク読み込み範囲読み込まれている可能性
 * yamlファイルはコマンド実行ごとに初期化されます。
 */
public class scancommand extends ZombieSubCommand {

    private static final String COMMAND_NAME = "scan";
    private static final String FILE_NAME = "chest-data.yml";

    /**
     * コマンドを取得
     * @return コマンド名
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    private Plugin plugin;
    private File dataFile;
    private YamlConfiguration dataConfig;

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

        this.plugin = ZombiePlugins.getInstance();
        this.dataFile = new File(plugin.getDataFolder(), FILE_NAME);
        this.dataConfig = new YamlConfiguration();
        try {
            // ファイルが存在している場合、内容を空にする
            if (dataFile.exists()) {
                dataConfig.save(dataFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Player player = (Player) sender;
        Map<String, Map<String, Integer>> chests = new HashMap<>();
        for (Chunk chunk : player.getWorld().getLoadedChunks()) {
            for (BlockState blockState : chunk.getTileEntities()) {
                if (blockState instanceof Chest) {
                    Chest chest = (Chest) blockState;
                    Inventory inventory = chest.getInventory();
                    if (isChestEmpty(inventory)) {
                        String key = String.format("chest%d", chests.size() + 1);
                        Map<String, Integer> chestData = new HashMap<>();
                        chestData.put("x", chest.getX());
                        chestData.put("y", chest.getY());
                        chestData.put("z", chest.getZ());
                        chests.put(key, chestData);
                    }
                }
            }
        }
        dataConfig.set("chests", chests);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage("Successfully scanned all chests!");
        sender.sendMessage(String.format("%d empty chests!", chests.size()));
        return true;
    }

    /**
     *チェストの中身の有無の確認
     * @param inventory インベントリの中身
     * @return チェストの中身の有無
     */
    private boolean isChestEmpty(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }
}
