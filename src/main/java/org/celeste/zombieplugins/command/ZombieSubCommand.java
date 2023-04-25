package org.celeste.zombieplugins.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * サブコマンド抽象クラス
 */

public abstract class ZombieSubCommand {
    public abstract String getCommandName();
    public abstract boolean runCommand(CommandSender sender,String label,String[] args);


}
