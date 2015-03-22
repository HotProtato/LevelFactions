package me.MajorAnatomy.FactionLevels.Events;

import java.util.Arrays;
import java.util.List;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;

public class OnInvClick implements Listener {
	
	FactionLevels favLevels = new FactionLevels();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		HumanEntity clicker = e.getWhoClicked();
		Player p = favLevels.getServer().getPlayer(clicker.getUniqueId());
		Inventory peps = Bukkit.createInventory(p, 54, "Invite a player");
		Inventory inv = e.getInventory();
		if(inv.equals(FactionLevels.invs.get(p.getUniqueId()))){
			if(e.getSlot() == 1){
				//on invite click
				/*if(!MPlayer.get(p).hasFaction()){
					p.closeInventory();
					p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You need a faction for this!");
					e.setCancelled(true);
				}
				if(getServer().getOnlinePlayers().length - MPlayer.get(p).getFaction().getMPlayersWhereOnline(true).size() == 0 && e.isCancelled() == false){
					p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "All members online are in your faction already!");
					p.closeInventory();
					e.setCancelled(true);
				}
				*/
				e.setCancelled(true);
				p.closeInventory();
				p.sendMessage(ChatColor.DARK_RED + "Coming soon!");
				/*
				int i = 0;
				for(Player p1 : getServer().getOnlinePlayers()){
					for(int w = 0; i - 54 == 0; w++){
						ItemStack paper = new ItemStack(Material.PAPER);
						ItemMeta paperim = paper.getItemMeta();
						paperim.setDisplayName(ChatColor.RED + "Click for the next page");
						paper.setItemMeta(paperim);
						peps.setItem(54, paper);
					}
					if(!MPlayer.get(p).getFaction().getMPlayers().contains(MPlayer.get(p1))){
					ItemStack s = new ItemStack(Material.SKULL_ITEM);
					ItemMeta sim = s.getItemMeta();
					sim.setDisplayName(p1.getName() + "");
					s.setItemMeta(sim);
					peps.setItem(i, s);
					i += 1;
					}
					}
				p.openInventory(peps);
				*/
				}
			}
		if(inv.equals(peps)){
			ItemStack is = peps.getItem(e.getSlot());
			ItemMeta isim = is.getItemMeta();
			if(e.getSlot() == 54){
				if(peps.getItem(54).equals(null) || peps.getItem(54).equals(Material.AIR)){
				} else {
				List<String> list = Arrays.asList(isim.getDisplayName().split(" "));
					isim.setDisplayName(ChatColor.RED + "Click for page " + (new Integer(list.get(4)) + 1));
					p.closeInventory();
					int i1 = 0;
					//get all players of the server
					for(Player player : favLevels.getServer().getOnlinePlayers()){
						if(i1 - (54 * new Integer(list.get(4))) % 54 != 0){
							Inventory newinv = Bukkit.createInventory(p, 54, "Invite a player");
							Skull s = (Skull)new ItemStack(144);
							s.setSkullType(SkullType.PLAYER);
							s.setOwner(player.getName());
							s.getData().toItemStack().getItemMeta().setDisplayName(player.getName());
							newinv.setItem(i1 - (54 * new Integer(list.get(4))), s.getData().toItemStack());
							p.openInventory(newinv);
						} else {
							isim.setDisplayName(ChatColor.RED + "Click for page " + (new Integer(list.get(4)) + 2));
						}
						i1++;
					}
				}
			}
			if(is instanceof Skull){
				String s = ((Skull)is).getOwner();
				if(favLevels.hasRoom(MPlayer.get(p).getFaction())){
					if(MPlayer.get(p).getRole().equals(Rel.OFFICER) || MPlayer.get(p).getRole().equals(Rel.LEADER)){
						if(MPlayer.get(p).getFaction().getInvitedPlayerIds().contains(s)){
							p.sendMessage(ChatColor.RED + s + ChatColor.YELLOW + " revoked!");
							MPlayer.get(p).getFaction().setInvited(s, false);
							favLevels.getServer().getPlayer(s).sendMessage(ChatColor.RED + "Your invite to " + ChatColor.GOLD + MPlayer.get(p).getFaction().getName() + ChatColor.RED + " has been revoked!");
						} else {
						p.sendMessage(ChatColor.RED + s + ChatColor.YELLOW + " invited!");
					MPlayer.get(p).getFaction().setInvited(s, true);
					favLevels.getServer().getPlayer(s).sendMessage(ChatColor.GOLD + "You have been invited to " + ChatColor.GREEN + MPlayer.get(p).getFactionName() + " " + ChatColor.GOLD + "by " + ChatColor.RED + p.getName());
					p.closeInventory();
						}
					} else {
						p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You are not a officer in your faction!");
					}
				}
			}
		}
		if(e.getInventory().equals(inv) && e.getSlot() == 2 && inv.getItem(2).getType().equals(Material.WRITTEN_BOOK) && ((BookMeta) e.getCurrentItem().getItemMeta()).getAuthor().equals("MajorSkillage")){
			BookMeta bm = (BookMeta) inv.getItem(2).getItemMeta();
			if(bm.getAuthor().equals("MajorSkillage")){
				p.closeInventory();
				p.sendMessage(ChatColor.DARK_RED + "Thanks for choosing this server!");
				p.sendMessage("");
				p.sendMessage(ChatColor.BLUE + "/donate " + ChatColor.GREEN + "lets you donate to your faction.");
				p.sendMessage(ChatColor.BLUE + "/progress " + ChatColor.GREEN + "lets you see your progress to your next faction level.");
				p.sendMessage(ChatColor.BLUE + "/quickjoin " + ChatColor.GREEN + "lets you join a faction automatically if you don\'t have one already.");
				p.sendMessage("");
				p.sendMessage(ChatColor.RED + "Advantages of leveling up your faction:");
				p.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "10 faction power.");
				p.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "Another slot so someone new can join.");
				p.sendMessage("");
				p.sendMessage(ChatColor.DARK_RED + "How leveling system works:");
				p.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "You need to level up your faction so more people can join.");
				p.sendMessage(ChatColor.GOLD + "This helps people work together rather then killing each");
				p.sendMessage(ChatColor.GOLD + "other in your own faction.");
				p.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "Post more suggestions on forums! www.mc-biomecraft.com");
				p.sendMessage(ChatColor.AQUA + "Plugin made by MajorSkillage.");
				e.setCancelled(true);
			}
		}
		if(inv.equals(FactionLevels.invs.get(p.getUniqueId())) || inv.equals(peps)){
		e.setCancelled(true);
		}
		if(inv.equals(FactionLevels.invs.get(p.getUniqueId()))){
			if(e.getSlot() == 3){
				if(MPlayer.get(p).getRole().equals(Rel.OFFICER) || MPlayer.get(p).getRole().equals(Rel.LEADER)){
					if(MPlayer.get(p).hasFaction()){
						if(MPlayer.get(p).getFaction().getInvitedPlayerIds().size() != 0){
					for(String P : MPlayer.get(p).getFaction().getInvitedPlayerIds()){
						MPlayer.get(p).getFaction().setInvited(MPlayer.get(P), false);
					}
					p.sendMessage(ChatColor.GREEN + "Invites cleared!");
						} else {
							p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You have no invited players!");
						}
					} else {
						p.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "You need a faction!");
					}
				}
				e.setCancelled(true);
				p.closeInventory();
			}
		}
		}

}
