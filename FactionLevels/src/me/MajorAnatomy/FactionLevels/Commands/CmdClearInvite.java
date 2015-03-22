package me.MajorAnatomy.FactionLevels.Commands;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPlayer;

public class CmdClearInvite extends FactionsCommand {
	
	FactionLevels facLevels = new FactionLevels();
	
	public boolean onCommand(CommandSender Sender, Command cmd, String s, String[] args) {
	if(Sender instanceof Player){
	facLevels.saveConfig();
	Player p = (Player)Sender;
	
	if(cmd.getName().equalsIgnoreCase("clearinvites") || cmd.getName().equalsIgnoreCase("clearinvs")){
		if(MPlayer.get(p).getRole().equals(Rel.OFFICER) || MPlayer.get(p).getRole().equals(Rel.LEADER)){
			if(MPlayer.get(p).hasFaction()){
				if(MPlayer.get(p).getFaction().getInvitedPlayerIds().size() != 0){
			for(String P : MPlayer.get(p).getFaction().getInvitedPlayerIds()){
				MPlayer.get(p).getFaction().setInvited(MPlayer.get(P), false);
			}
			
			p.sendMessage(ChatColor.GREEN + "Invites cleared!");
				}
				else {
					p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You have no invited players!");
				}
				
			} else {
				p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You need a faction!");
			}
			
		}
		
	}
	
	}
	return false;
	
	}

}
