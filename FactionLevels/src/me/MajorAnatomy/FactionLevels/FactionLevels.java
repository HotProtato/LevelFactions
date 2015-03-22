package me.MajorAnatomy.FactionLevels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;

public class FactionLevels extends JavaPlugin implements Listener{
	HashMap<UUID, Inventory> invs = new HashMap<UUID, Inventory>();
	List<String> banned_words = new ArrayList<String>();
	HashMap<UUID, ArrayList<String>> news = new HashMap<UUID, ArrayList<String>>();
	HashMap<Integer, Faction> ranks = new HashMap<Integer, Faction>();
	File f = new File(this.getDataFolder(), "news.yml");
	FileConfiguration newsconfig = YamlConfiguration.loadConfiguration(f);
	Plugin plugin;
	//economy hook variable
	public Economy economy;
	  public static String format(String format)
	  {
	    return ChatColor.translateAlternateColorCodes('&', format);
	  }
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
 
        return (economy != null);
    }
	HashMap<Integer, Integer> lvlPrices = new HashMap<Integer, Integer>();
	public void onEnable(){
		for(String e : newsconfig.getKeys(true)){
			UUID u = UUID.fromString(e);
			news.put(u, (ArrayList<String>) newsconfig.getStringList(u + ""));
		}
		try {
			newsconfig.save(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		banned_words.add("fuck");
		banned_words.add("bitch");
		banned_words.add("pussy");
		banned_words.add("shit");
		banned_words.add("cunt");
		getConfig().addDefault("default-faction-player-limit", 5);
		plugin = this;
		setupEconomy();
		saveConfig();
		lvlPrices.put(1, 1000);
		lvlPrices.put(2, 2000);
		lvlPrices.put(3, 5000);
		lvlPrices.put(4, 10000);
		lvlPrices.put(5, 15000);
		lvlPrices.put(6, 20000);
		lvlPrices.put(7, 50000);
		lvlPrices.put(8, 75000);
		lvlPrices.put(9, 100000);
		lvlPrices.put(10, 150000);
		for(Entry<Integer, Integer> e : lvlPrices.entrySet()){
			getConfig().addDefault("level " + e, e.getValue());
		}
		getServer().getPluginManager().registerEvents(this, this);
		saveConfig();
	}
	public void onDisable(){
		saveConfig();
		for(Entry<UUID, ArrayList<String>> e : news.entrySet()){
			newsconfig.set(e.getKey() + "", e.getValue());
		}
	}
	public static boolean isInteger(String s){
	     try { 
	         Integer.parseInt(s); 
	     } catch(NumberFormatException e) {
	         return false; 
	     }
	     return true;
		
	}
	@Override
	public boolean onCommand(CommandSender Sender, Command cmd, String commandLabel, String[] args){
		if(Sender instanceof Player){
			saveConfig();
		Player p = (Player)Sender;
		if(cmd.getName().equalsIgnoreCase("clearinvites") || cmd.getName().equalsIgnoreCase("clearinvs")){
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
		}
		MPlayer p1 = MPlayer.get(p);
		if(cmd.getName().equalsIgnoreCase("killmessage") || cmd.getName().equalsIgnoreCase("km")){
			if(args.length > 0){
			String msg = "";
			for(int i = 0; i < args.length; i++){
				msg += args[i] + " ";
			}
			getConfig().set("killmessage " + p.getUniqueId(), msg);
			saveConfig();
			p.sendMessage(ChatColor.DARK_RED + "Kill message set!");
		} else {
			p.sendMessage(ChatColor.RED + "Correct usage: " + cmd.getName().toLowerCase() + " (message)");
		}
		}
		if(cmd.getName().toLowerCase().startsWith("invitedplayers")){
			if(p1.hasFaction()){
				if(p1.getFaction().getInvitedPlayerIds().contains("@console")){
					MPlayer.get("@console").resetFactionData();
				}
				if(p1.getFaction().getInvitedPlayerIds().size() == 0){
					p.sendMessage(ChatColor.RED + "Sorry, you have no invited faction players!");
				} else {
				for(String s: p1.getFaction().getInvitedPlayerIds()){
					UUID u = UUID.fromString(s);
					p.sendMessage("- " + getServer().getPlayer(u).getName());
				}
				}
			} else {
				p.sendMessage(ChatColor.RED + "Sorry, faction required for this!");
			}
		}
		if(cmd.getName().equalsIgnoreCase("donate")){
			if(args.length == 1){
				if(p1.hasFaction()){
				double playerBalance = economy.getBalance(getServer().getPlayer(p.getUniqueId()));
				if(isInteger(args[0])){
					if(new Integer(args[0]) > 0){
						int bal = new Integer(args[0]);
				if(playerBalance - bal >= 0){
					for(Player player1: p1.getFaction().getOnlinePlayers()){
						if(!player1.equals(p)){
							p.sendMessage(ChatColor.RED + p.getName() + ChatColor.BLUE + ChatColor.RED + " has donated $" + args[0]);
						}
					}
					economy.withdrawPlayer(p.getName(), bal);
					p.sendMessage(ChatColor.RED + "Thank you for donating $" + ChatColor.DARK_RED + bal + ChatColor.RED + " to " + ChatColor.GOLD + p1.getFactionName());
					saveConfig();
						int fbal = getConfig().getInt(p1.getFaction().getName() + " progress");
						int faclvl = (getConfig().getInt(p1.getFaction().getName() + " lvl")) + 1;
						int cost = getConfig().getInt("level " + faclvl);
						float remain = (fbal - cost);
 						if(remain >= 0){
 							for(MPlayer pla: p1.getFaction().getMPlayers()){
 								if(pla.isOnline()){
 								getServer().getPlayer(pla.getUuid()).sendMessage(ChatColor.RED + "Your faction has leveled up to " + faclvl);
 							} else {
 								if(news.containsKey(pla.getUuid())){
 									ArrayList<String> temp = news.get(pla.getUuid());
 									temp.add(ChatColor.RED + "Your faction has leveled up to " + faclvl);
 									news.put(pla.getUuid(), temp);
 									temp.clear();
 								} else {
 									ArrayList<String> temp = new ArrayList<String>();
 									temp.add(ChatColor.RED + "Your faction has leveled up to " + faclvl);
 									news.put(pla.getUuid(), temp);
 									news.clear();
 								}
 							}
 							}
							getConfig().set(p1.getFaction().getName() + " progress", remain);
							int lvl = getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl");
							getConfig().set(MPlayer.get(p).getFaction().getName() + " lvl", lvl + 1);
							p1.getFaction().setPowerBoost(p1.getFaction().getPowerBoost() + 10);
							saveConfig();
						} else {
							getConfig().set(p1.getFaction().getName() + " progress", getConfig().getInt(p1.getFaction().getName() + " progress") + new Integer(args[0]));
							saveConfig();
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
		if(cmd.getName().equalsIgnoreCase("quickjoin")){
			if(p1.hasFaction()){
				p.sendMessage(ChatColor.RED + "Sorry, you already have a faction!");
			} else {
			for (Faction faction : FactionColl.get().getAll()){
				if(faction.isOpen()){
					if(hasRoom(faction) == true){
					MPlayer.get(p).setFaction(faction);
					MPlayer.get(p).sendMessage(ChatColor.RED + "You have joined " + ChatColor.BLUE + faction.getName());
					for(MPlayer player2 : faction.getMPlayers()){
						if(player2.isOnline()){
							player2.sendMessage("-" + p.getName() + ChatColor.YELLOW + " has joined " + ChatColor.GREEN + "your faction!");
						} else {
							if(news.containsKey(player2.getUuid())){
								ArrayList<String> temp = news.get(player2.getUuid());
								temp.add("-" + ChatColor.YELLOW + p.getName() + " has joined " + ChatColor.GREEN + " your faction!");
								news.put(player2.getUuid(), temp);
								temp.clear();
							} else {
								ArrayList<String> new_news = new ArrayList<String>();
								new_news.add("- " + p.getName() + ChatColor.YELLOW + " has joined " + ChatColor.GREEN + "your faction!");
							news.put(player2.getUuid(), new_news);
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
		if(cmd.getName().equalsIgnoreCase("progress")){
			if(args.length == 0){
			if(p1.hasFaction()){
				int balance = getConfig().getInt(p1.getFaction().getName() + " progress");
				p.sendMessage(ChatColor.GREEN + "" + balance + ChatColor.RED + " out of " + ChatColor.GREEN + getConfig().getInt("level " + (getConfig().getInt(p1.getFaction().getName() + " lvl") + 1)) + ChatColor.RED + " till level " + ChatColor.BLUE + (getConfig().getInt(p1.getFaction().getName() + " lvl") + 1));
			} else {
				p.sendMessage(ChatColor.RED + "Sorry, you need a faction for this!");
			}
			}
		}
		}
		return false;
	}
	@EventHandler
	public void onInvite(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		for(int i = 0; i < banned_words.size(); i++){
			if(msg.toLowerCase().contains(banned_words.get(i))){
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Swearing is not allowed!");
			}
		}
		List<String> myList = new ArrayList<String>(Arrays.asList(msg.split(" ")));
		if(myList.get(0).equalsIgnoreCase("/f")){
			if(myList.size() == 1){
				Inventory inv = Bukkit.createInventory(getServer().getPlayer(p.getUniqueId()), 54, "Factions GUI");
				int players = MPlayer.get(p).getFaction().getMPlayers().size();
				int invitedPlayers = MPlayer.get(p).getFaction().getInvitedPlayerIds().size();
				int maxPlayers = getConfig().getInt("default-faction-player-limit") + getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl");
				Material m1 = Material.WRITTEN_BOOK;
				ItemStack baq = new ItemStack(386);
				List<String> lore1 = new ArrayList<String>();
				ItemMeta baqim = baq.getItemMeta();
				lore1.add("Click this to remove all invited players invites!");
				baqim.setLore(lore1);
				baqim.setDisplayName(format("&6Click to clear invites!"));
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
				invs.put(p.getUniqueId(), inv);
			}
		if(myList.size() > 0){
			if(myList.get(1).equalsIgnoreCase("invite") || myList.get(1).equalsIgnoreCase("inv")){
				if(myList.size() == 2){
					if(myList.get(2).equalsIgnoreCase("@console")){
						e.setCancelled(true);
					}
				}
				saveConfig();
				if(MPlayer.get(p).getFaction().getInvitedPlayerIds().contains(getServer().getPlayer(myList.get(2)).getName()) || MPlayer.get(p).getFaction().getInvitedPlayerIds().size() + MPlayer.get(p).getFaction().getMPlayers().size() <= getConfig().getInt(MPlayer.get(p).getFaction().getName() + " lvl") + getConfig().getInt("default-faction-player-limit")){
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "Sorry, you must level up your faction before getting more members, kick players or revoke invites!");
				}
			} else if(myList.get(1).equalsIgnoreCase("join")){
				Faction faction = FactionColl.get().getByName(myList.get(2));
				if(faction.getInvitedPlayerIds().contains(p.getName())){
				} else if(hasRoom(faction)){
					for(MPlayer p1 : faction.getMPlayers()){
						if(!p1.isOnline()){
							if(news.containsKey(p1.getUuid())){
								ArrayList<String> temp = news.get(p1.getUuid());
								temp.add("-" + ChatColor.YELLOW + p1.getName() + " has join your faction!");
								news.put(p1.getUuid(), temp);
								temp.clear();
							} else {
								ArrayList<String> temp = new ArrayList<String>();
								temp.add("-" + ChatColor.YELLOW + p1.getName() + " has join your faction!");
								news.put(p1.getUuid(), temp);
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
	public boolean hasRoom(Faction f){
		if(f.getMPlayers().size() + f.getInvitedPlayerIds().size() <= (getConfig().getInt(f.getName() + " lvl") + getConfig().getInt("default-faction-player-limit"))){
			return true;
		} else {
			return false;
		}
	}
	@EventHandler
	public void onCreate(EventFactionsCreate e){
		e.getMSender().getFaction().setPowerBoost(20D);
		getConfig().set(e.getFactionName() + " lvl", 0);
		getConfig().set(e.getFactionName() + " progress", 0);
		saveConfig();
	}
	@EventHandler
	public void onMemberChange(EventFactionsMembershipChange e){
		e.getMPlayer().setPower((double) 0);
		e.getMPlayer().setPowerBoost((double)0);
		saveConfig();
}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(hasNews(p)){
			for(Entry<UUID, ArrayList<String>> en : news.entrySet()){
				if(en.getKey().equals(p.getUniqueId())){
					for(int i = 0; i < en.getValue().size(); i++){
					p.sendMessage(format(en.getValue().get(i)));
					}
				}
			}
		}
	}
	@EventHandler
	public void onDisband(EventFactionsDisband e){
		for(MPlayer p : e.getFaction().getMPlayers()){
			if(!p.isOnline()){
				ArrayList<String> temp = news.get(p.getUuid());
				temp.add(ChatColor.RED + "I am sorry to say, your faction is disbanded!");
				news.put(p.getUuid(), temp);
				temp.clear();
			}
		}
		getConfig().set(e.getFaction().getName() + " lvl", null);
		getConfig().set(e.getFaction().getName() + " progress", null);
	}
	@EventHandler
	public void onKill(EntityDamageByEntityEvent e){
		Entity dmger1 = e.getDamager();
		Damageable dmged1 = (Player)e.getEntity();
		if(dmger1 instanceof Player && dmged1 instanceof Player){
			if(dmged1.isDead() || (dmged1.getHealth() >= 0)){
				
		Player dmger = (Player)e.getDamager();
		Player dmged = (Player)e.getEntity();
		saveConfig();
		if(getConfig().getString("killmessage " + dmger.getUniqueId()) != null){
			dmged.sendMessage(format(getConfig().getString("killmessage " + dmger.getUniqueId())));
			dmged.sendMessage("TESTING 1");
		} else {
			dmger.sendMessage(ChatColor.RED + "I see you have not set a kill message yet, you can do this with /km (message), this message will be messaged to the player you killed!");
		}
			}
		}
	}
	public boolean hasNews (Player p){
		if(news.get(p.getUniqueId()).equals(null)){
			return false;
		}
		return true;
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		HumanEntity clicker = e.getWhoClicked();
		Player p = getServer().getPlayer(clicker.getUniqueId());
		Inventory peps = Bukkit.createInventory(p, 54, "Invite a player");
		Inventory inv = e.getInventory();
		if(inv.equals(invs.get(p.getUniqueId()))){
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
					for(Player player : getServer().getOnlinePlayers()){
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
				if(hasRoom(MPlayer.get(p).getFaction())){
					if(MPlayer.get(p).getRole().equals(Rel.OFFICER) || MPlayer.get(p).getRole().equals(Rel.LEADER)){
						if(MPlayer.get(p).getFaction().getInvitedPlayerIds().contains(s)){
							p.sendMessage(ChatColor.RED + s + ChatColor.YELLOW + " revoked!");
							MPlayer.get(p).getFaction().setInvited(s, false);
							getServer().getPlayer(s).sendMessage(ChatColor.RED + "Your invite to " + ChatColor.GOLD + MPlayer.get(p).getFaction().getName() + ChatColor.RED + " has been revoked!");
						} else {
						p.sendMessage(ChatColor.RED + s + ChatColor.YELLOW + " invited!");
					MPlayer.get(p).getFaction().setInvited(s, true);
					getServer().getPlayer(s).sendMessage(ChatColor.GOLD + "You have been invited to " + ChatColor.GREEN + MPlayer.get(p).getFactionName() + " " + ChatColor.GOLD + "by " + ChatColor.RED + p.getName());
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
		if(inv.equals(invs.get(p.getUniqueId())) || inv.equals(peps)){
		e.setCancelled(true);
		}
		if(inv.equals(invs.get(p.getUniqueId()))){
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