package me.MajorAnatomy.FactionLevels.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

public class CmdQuickJoin extends FactionsCommand implements CommandExecutor{
	
	FactionLevels facLevels = new FactionLevels();
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		if(Sender instanceof Player){
			facLevels.saveConfig();
		    Player p = (Player)Sender;	
		    MPlayer p1 = MPlayer.get(p);

		    
			if(cmd.getName().equalsIgnoreCase("quickjoin")){
				if(p1.hasFaction()){
					p.sendMessage(ChatColor.RED + "Sorry, you already have a faction!");
				} else {
				for (Faction faction : FactionColl.get().getAll()){
					if(faction.isOpen()){
						if(facLevels.hasRoom(faction) == true){
						MPlayer.get(p).setFaction(faction);
						MPlayer.get(p).sendMessage(ChatColor.RED + "You have joined " + ChatColor.BLUE + faction.getName());
						for(MPlayer player2 : faction.getMPlayers()){
							if(player2.isOnline()){
								player2.sendMessage("-" + p.getName() + ChatColor.YELLOW + " has joined " + ChatColor.GREEN + "your faction!");
							} else {
								if(facLevels.news.containsKey(player2.getUuid())){
									ArrayList<String> temp = facLevels.news.get(player2.getUuid());
									temp.add("-" + ChatColor.YELLOW + p.getName() + " has joined " + ChatColor.GREEN + " your faction!");
									facLevels.news.put(player2.getUuid(), temp);
									temp.clear();
								} else {
									ArrayList<String> new_news = new ArrayList<String>();
									new_news.add("- " + p.getName() + ChatColor.YELLOW + " has joined " + ChatColor.GREEN + "your faction!");
									facLevels.news.put(player2.getUuid(), new_news);
								new_news.clear();
								}
							}
						}
						}
					}
				}
				if(!p1.hasFaction()){
					p.sendMessage(ChatColor.RED + "Sorry, there is no room in any open faction! :(");
				}
				}
			}
		    
		}
		return false;
	}
		    
}
