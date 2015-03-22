package me.MajorAnatomy.FactionLevels.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

public class OnInvite implements Listener {
	
	FactionLevels facLevels = new FactionLevels();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvite(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		for(int i = 0; i < FactionLevels.banned_words.size(); i++){
			if(msg.toLowerCase().contains(FactionLevels.banned_words.get(i))){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Swearing is not allowed!");
			}
		}
		List<String> myList = new ArrayList<String>(Arrays.asList(msg.split(" ")));
		if(myList.get(0).equalsIgnoreCase("/f")){
			if(myList.size() == 1){
				Inventory inv = Bukkit.createInventory(facLevels.getServer().getPlayer(p.getUniqueId()), 54, "Factions GUI");
				int players = MPlayer.get(p).getFaction().getMPlayers().size();
				int invitedPlayers = MPlayer.get(p).getFaction().getInvitedPlayerIds().size();
				int maxPlayers = facLevels.getConfig().getInt("default-faction-player-limit") + facLevels.getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl");
				Material m1 = Material.WRITTEN_BOOK;
				ItemStack baq = new ItemStack(386);
				List<String> lore1 = new ArrayList<String>();
				ItemMeta baqim = baq.getItemMeta();
				lore1.add("Click this to remove all invited players invites!");
				baqim.setLore(lore1);
				baqim.setDisplayName(FactionLevels.format("&6Click to clear invites!"));
				ArrayList<String> lore = new ArrayList<String>();
				baq.setItemMeta(baqim);
				ItemStack m = new ItemStack(m1);
				BookMeta mbm = (BookMeta) m.getItemMeta();
				mbm.setDisplayName(ChatColor.GOLD + "Click for help");
				mbm.setAuthor("MajorSkillage");
				m.setItemMeta(mbm);
				inv.setItem(2, new ItemStack(m));
				ItemStack paper = new ItemStack(339);
				ItemMeta paperim = paper.getItemMeta();
				inv.setItem(3, baq);
				if(MPlayer.get(p).hasFaction()){
				lore.add("Players: " + players);
				lore.add("Invited Players: " + invitedPlayers);
				lore.add("Max players: " + maxPlayers);
				} else {
					lore.add("You don\'t have a faction!");
				}
				paperim.setLore(lore);
				paperim.setDisplayName(ChatColor.GOLD + "Players");
				paper.setItemMeta(paperim);
				ItemStack diamond = new ItemStack(1);
				ItemMeta diamondim = diamond.getItemMeta();
				List<String> dialore = new ArrayList<String>();
				dialore.add("Invite a player!");
				diamondim.setLore(dialore);
				diamondim.setDisplayName(ChatColor.RED + "Invite");
				diamond.setItemMeta(diamondim);
				inv.setItem(0, paper);
				inv.setItem(1, diamond);
				p.openInventory(inv);
				e.setCancelled(true);
				FactionLevels.invs.put(p.getUniqueId(), inv);
			}
		if(myList.size() > 0){
			if(myList.get(1).equalsIgnoreCase("invite") || myList.get(1).equalsIgnoreCase("inv")){
				if(myList.size() == 2){
					if(myList.get(2).equalsIgnoreCase("@console")){
						e.setCancelled(true);
					}
				}
				facLevels.saveConfig();
				if(MPlayer.get(p).getFaction().getInvitedPlayerIds().contains(facLevels.getServer().getPlayer(myList.get(2)).getName()) || MPlayer.get(p).getFaction().getInvitedPlayerIds().size() + MPlayer.get(p).getFaction().getMPlayers().size() <= facLevels.getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl") + facLevels.getConfig().getInt("default-faction-player-limit")){
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "Sorry, you must level up your faction before getting more members, kick players or revoke invites!");
				}
			} else if(myList.get(1).equalsIgnoreCase("join")){
				Faction faction = FactionColl.get().getByName(myList.get(2));
				if(faction.getInvitedPlayerIds().contains(p.getName())){
				} else if(facLevels.hasRoom(faction)){
					for(MPlayer p1 : faction.getMPlayers()){
						if(!p1.isOnline()){
							if(facLevels.news.containsKey(p1.getUuid())){
								ArrayList<String> temp = facLevels.news.get(p1.getUuid());
								temp.add("-" + ChatColor.YELLOW + p1.getName() + " has join your faction!");
								facLevels.news.put(p1.getUuid(), temp);
								temp.clear();
							} else {
								ArrayList<String> temp = new ArrayList<String>();
								temp.add("-" + ChatColor.YELLOW + p1.getName() + " has join your faction!");
								facLevels.news.put(p1.getUuid(), temp);
								temp.clear();
							}
						}
					}
					} else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "Sorry, This faction is full!");
					}
					}
				}
			}
			}
	
	

}
