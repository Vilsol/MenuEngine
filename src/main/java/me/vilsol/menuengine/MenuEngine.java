package me.vilsol.menuengine;

import me.vilsol.menuengine.listeners.ItemListener;

import org.bukkit.plugin.java.JavaPlugin;

public class MenuEngine extends JavaPlugin {
	
	public static MenuEngine plugin;
	
	public void onEnable(){
		plugin = this;
		getServer().getPluginManager().registerEvents(new ItemListener(), this);
	}
	
}
