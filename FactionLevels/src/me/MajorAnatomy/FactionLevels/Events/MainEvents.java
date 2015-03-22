package me.MajorAnatomy.FactionLevels.Events;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import me.MajorAnatomy.FactionLevels.FactionLevels;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;

public class MainEvents implements Listener {
	
	FactionLevels facLevels = new FactionLevels();
	
	@EventHandler
	public void onCreate(EventFactionsCreate e){
		e.getMSender().getFaction().setPowerBoost(20D);
		facLevels.getConfig().set(e.getFactionName() + " lvl", 0);
		facLevels.getConfig().set(e.getFactionName() + " progress", 0);
		facLevels.saveConfig();
	}
	@EventHandler
	public void onMemberChange(EventFactionsMembershipChange e){
		e.getMPlayer().setPower((double) 0);
		e.getMPlayer().setPowerBoost((double)0);
		facLevels.saveConfig();
}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(facLevels.hasNews(p)){
			for(Entry<UUID, ArrayList<String>> en : facLevels.news.entrySet()){
				if(en.getKey().equals(p.getUniqueId())){
					for(int i = 0; i < en.getValue().size(); i++){
					p.sendMessage(FactionLevels.format(en.getValue().get(i)));
					}
				}
			}
		}
	}
	@EventHandler
	public void onDisband(EventFactionsDisband e){
		for(MPlayer p : e.getFaction().getMPlayers()){
			if(!p.isOnline()){
				ArrayList<String> temp = facLevels.news.get(p.getUuid());
				temp.add(ChatColor.RED + "I am sorry to say, your faction is disbanded!");
				facLevels.news.put(p.getUuid(), temp);
				temp.clear();
			}
		}
		facLevels.getConfig().set(e.getFaction().getName() + " lvl", null);
		facLevels.getConfig().set(e.getFaction().getName() + " progress", null);
	}
	@EventHandler
	public void onKill(EntityDamageByEntityEvent e){
		Entity dmger1 = e.getDamager();
		Damageable dmged1 = (Player)e.getEntity();
		if(dmger1 instanceof Player && dmged1 instanceof Player){
			if(dmged1.isDead() || (dmged1.getHealth() >= 0)){
				
		Player dmger = (Player)e.getDamager();
		Player dmged = (Player)e.getEntity();
		facLevels.saveConfig();
		if(facLevels.getConfig().getString("killmessage " + dmger.getUniqueId()) != null){
			dmged.sendMessage(FactionLevels.format(facLevels.getConfig().getString("killmessage " + dmger.getUniqueId())));
			dmged.sendMessage("TESTING 1");
		} else {
			dmger.sendMessage(ChatColor.RED + "I see you have not set a kill message yet, you can do this with /km (message), this message will be messaged to the player you killed!");
		}
			}
		}
	}

}
