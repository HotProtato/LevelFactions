package me.MajorAnatomy.FactionLevels.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPlayer;

public class CmdDonate extends FactionsCommand {
	
	FactionLevels facLevels = new FactionLevels();
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		if(Sender instanceof Player){
			facLevels.saveConfig();
		    Player p = (Player)Sender;	
		    MPlayer p1 = MPlayer.get(p);
		    
			if(cmd.getName().equalsIgnoreCase("donate")){
				if(args.length == 1){
					if(p1.hasFaction()){
					double playerBalance = facLevels.economy.getBalance(facLevels.getServer().getPlayer(p.getUniqueId()));
					if(FactionLevels.isInteger(args[0])){
						if(new Integer(args[0]) > 0){
							int bal = new Integer(args[0]);
					if(playerBalance - bal >= 0){
						for(Player player1: p1.getFaction().getOnlinePlayers()){
							if(!player1.equals(p)){
								p.sendMessage(ChatColor.RED + p.getName() + ChatColor.BLUE + ChatColor.RED + " has donated $" + args[0]);
							}
						}
						facLevels.economy.withdrawPlayer(p.getName(), bal);
						p.sendMessage(ChatColor.RED + "Thank you for donating $" + ChatColor.DARK_RED + bal + ChatColor.RED + " to " + ChatColor.GOLD + p1.getFactionName());
						facLevels.saveConfig();
							int fbal = facLevels.getConfig().getInt(p1.getFaction().getName() + " progress");
							int faclvl = (facLevels.getConfig().getInt(p1.getFaction().getName() + " lvl")) + 1;
							int cost = facLevels.getConfig().getInt("level " + faclvl);
							float remain = (fbal - cost);
	 						if(remain >= 0){
	 							for(MPlayer pla: p1.getFaction().getMPlayers()){
	 								if(pla.isOnline()){
	 									facLevels.getServer().getPlayer(pla.getUuid()).sendMessage(ChatColor.RED + "Your faction has leveled up to " + faclvl);
	 							} else {
	 								if(facLevels.news.containsKey(pla.getUuid())){
	 									ArrayList<String> temp = facLevels.news.get(pla.getUuid());
	 									temp.add(ChatColor.RED + "Your faction has leveled up to " + faclvl);
	 									facLevels.news.put(pla.getUuid(), temp);
	 									temp.clear();
	 								} else {
	 									ArrayList<String> temp = new ArrayList<String>();
	 									temp.add(ChatColor.RED + "Your faction has leveled up to " + faclvl);
	 									facLevels.news.put(pla.getUuid(), temp);
	 									facLevels.news.clear();
	 								}
	 							}
	 							}
								facLevels.getConfig().set(p1.getFaction().getName() + " progress", remain);
								int lvl = facLevels.getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl");
								facLevels.getConfig().set(MPlayer.get(p).getFaction().getName() + " lvl", lvl + 1);
								p1.getFaction().setPowerBoost(p1.getFaction().getPowerBoost() + 10);
								facLevels.saveConfig();
							} else {
								facLevels.getConfig().set(p1.getFaction().getName() + " progress", facLevels.getConfig().getInt(p1.getFaction().getName() + " progress") + new Integer(args[0]));
								facLevels.saveConfig();
							}
					} else {
						p.sendMessage(ChatColor.RED + "Sorry, you don't have enough to donate that much!");
					}
					} else {
						p.sendMessage(ChatColor.RED + args[0] + " is not a big enough number!");
					}
				} else {
					p.sendMessage(ChatColor.RED + args[0] + " is not a number!");
				}
				} else {
					p.sendMessage(ChatColor.RED + "Sorry, you need a faction for that!");
				}
			} else {
				p.sendMessage(ChatColor.RED + "Correct usage: /donate (amount over 0)");
			}
			}
		  
		}
		return false;
	}

}
