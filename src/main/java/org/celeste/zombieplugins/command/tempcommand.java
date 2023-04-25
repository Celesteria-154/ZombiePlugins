package org.celeste.zombieplugins.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * コマンドテンプレート用とテストコマンド処理クラス
 */
public class tempcommand extends ZombieSubCommand {
    private static final String COMMAND_NAME = "test";

    /**
     * コマンドを取得
     * @return コマンド名
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * コマンド実行
     * @param sender 実行者
     * @param label　実行ラベル
     * @param args　実行引数
     * @return コマンドの実行したか
     */
    @Override
    public boolean runCommand(CommandSender sender, String label, String[] args) {
        System.out.println("test");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Successful command execution!");
        } else {
            sender.sendMessage("This command can only be executed by a player.");
        }

        return true;
    }
}
