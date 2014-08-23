package me.vilsol.menuengine;

import me.vilsol.menuengine.listeners.ItemListener;

import org.bukkit.plugin.java.JavaPlugin;

public class MenuEngine extends JavaPlugin{
	
	private static MenuEngine c;
	
	public void onEnable() {
		c = this;
		getServer().getPluginManager().registerEvents(new ItemListener(), this);
	}
	
	public static JavaPlugin getPlugin(){
		return c;
	}
	
}
