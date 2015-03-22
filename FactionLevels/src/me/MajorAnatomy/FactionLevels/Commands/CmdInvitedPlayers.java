package me.MajorAnatomy.FactionLevels.Commands;

import java.util.UUID;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPlayer;

public class CmdInvitedPlayers extends FactionsCommand implements CommandExecutor{

	FactionLevels facLevels = new FactionLevels();
	
	public boolean onCommand(CommandSender Sender, Command cmd, String s, String[] args) {
	if(Sender instanceof Player){
	facLevels.saveConfig();
	Player p = (Player)Sender;
	MPlayer p1 = MPlayer.get(p);
	
	if(cmd.getName().toLowerCase().startsWith("invitedplayers")){
		if(p1.hasFaction()){
			if(p1.getFaction().getInvitedPlayerIds().contains("@console")){
				MPlayer.get("@console").resetFactionData();
			}
			if(p1.getFaction().getInvitedPlayerIds().size() == 0){
				p.sendMessage(ChatColor.RED + "Sorry, you have no invited faction players!");
			} else {
			for(String string: p1.getFaction().getInvitedPlayerIds()){
				UUID u = UUID.fromString(string);
				p.sendMessage("- " + Bukkit.getServer().getPlayer(u).getName());
			}
			}
		} else {
			p.sendMessage(ChatColor.RED + "Sorry, faction required for this!");
		}
	}
	
	}
	
	return false;
	
	}
	
}
