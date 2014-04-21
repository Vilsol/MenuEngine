package me.vilsol.menuengine.engine;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatCallback {

	public static HashMap<Player, ChatCallback> locked_players = new HashMap<Player, ChatCallback>();
	
	public void onChatMessage(AsyncPlayerChatEvent e);
	
}
