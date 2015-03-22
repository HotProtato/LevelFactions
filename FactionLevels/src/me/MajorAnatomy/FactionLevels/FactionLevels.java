package me.MajorAnatomy.FactionLevels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import me.MajorAnatomy.FactionLevels.Commands.CmdClearInvite;
import me.MajorAnatomy.FactionLevels.Commands.CmdDonate;
import me.MajorAnatomy.FactionLevels.Commands.CmdInvitedPlayers;
import me.MajorAnatomy.FactionLevels.Commands.CmdKillMessage;
import me.MajorAnatomy.FactionLevels.Commands.CmdProgress;
import me.MajorAnatomy.FactionLevels.Commands.CmdQuickJoin;
import me.MajorAnatomy.FactionLevels.Events.MainEvents;
import me.MajorAnatomy.FactionLevels.Events.OnInvClick;
import me.MajorAnatomy.FactionLevels.Events.OnInvite;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.Faction;

public class FactionLevels extends JavaPlugin implements Listener {
	public static HashMap<UUID, Inventory> invs = new HashMap<UUID, Inventory>();
	public static List<String> banned_words = new ArrayList<String>();
	public HashMap<UUID, ArrayList<String>> news = new HashMap<UUID, ArrayList<String>>();
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
		registerEvents();
		registerCmds();
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

	public boolean hasRoom(Faction f){
		if(f.getMPlayers().size() + f.getInvitedPlayerIds().size() <= (getConfig().getInt(f.getName() + " lvl") + getConfig().getInt("default-faction-player-limit"))){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasNews (Player p){
		if(news.get(p.getUniqueId()).equals(null)){
			return false;
		}
		return true;
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new OnInvClick(), plugin);
		getServer().getPluginManager().registerEvents(new OnInvite(), plugin);
		getServer().getPluginManager().registerEvents(new MainEvents(), plugin);
	}
	
	private void registerCmds() {
		getCommand("clearinvites").setExecutor(new CmdClearInvite());
		getCommand("clearinvs").setExecutor(new CmdClearInvite());
		getCommand("donate").setExecutor(new CmdDonate());
		getCommand("invitedplayers").setExecutor(new CmdInvitedPlayers());
		getCommand("killmessage").setExecutor(new CmdKillMessage());
		getCommand("km").setExecutor(new CmdKillMessage());
		getCommand("progress").setExecutor(new CmdProgress());
		getCommand("quickjoin").setExecutor(new CmdQuickJoin());
	}

	}