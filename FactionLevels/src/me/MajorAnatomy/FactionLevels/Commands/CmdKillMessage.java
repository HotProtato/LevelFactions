package me.MajorAnatomy.FactionLevels.Commands;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPlayer;

public class CmdKillMessage extends FactionsCommand {
	
	FactionLevels facLevels = new FactionLevels();
	
	public boolean onCommand(CommandSender Sender, Command cmd, String s, String[] args) {
	if(Sender instanceof Player){
	facLevels.saveConfig();
	Player p = (Player)Sender;
	MPlayer p1 = MPlayer.get(p);
	
	if(cmd.getName().equalsIgnoreCase("killmessage") || cmd.getName().equalsIgnoreCase("km")){
		if(args.length > 0){
		String msg = "";
		for(int i = 0; i < args.length; i++){
			msg += args[i] + " ";
		}
		facLevels.getConfig().set("killmessage " + p.getUniqueId(), msg);
		facLevels.saveConfig();
		p.sendMessage(ChatColor.DARK_RED + "Kill message set!");
	} else {
		p.sendMessage(ChatColor.RED + "Correct usage: " + cmd.getName().toLowerCase() + " (message)");
	}
	}
	
	}
	
	return false;
	
	}
}