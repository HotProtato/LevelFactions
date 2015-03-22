package me.MajorAnatomy.FactionLevels.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPlayer;

public class CmdProgress extends FactionsCommand {
	
	FactionLevels facLevels = new FactionLevels();
	
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		if(Sender instanceof Player){
			facLevels.saveConfig();
		    Player p = (Player)Sender;	
		    MPlayer p1 = MPlayer.get(p);
		

		if(cmd.getName().equalsIgnoreCase("progress")){
			if(args.length == 0){
			if(p1.hasFaction()){
				int balance = facLevels.getConfig().getInt(p1.getFaction().getName() + " progress");
				p.sendMessage(ChatColor.GREEN + "" + balance + ChatColor.RED + " out of " + ChatColor.GREEN + facLevels.getConfig().getInt("level " + (facLevels.getConfig().getInt(p1.getFaction().getName() + " lvl") + 1)) + ChatColor.RED + " till level " + ChatColor.BLUE + (facLevels.getConfig().getInt(p1.getFaction().getName() + " lvl") + 1));
			} else {
				p.sendMessage(ChatColor.RED + "Sorry, you need a faction for this!");
			}
			}
		}
		}
		return false;
	}

}
