package me.vilsol.menuengine.engine;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface MenuItem {
	
	public static HashMap<Class<? extends MenuItem>, MenuItem> items = new HashMap<Class<? extends MenuItem>, MenuItem>();
	
	public void registerItem();
	
	/**
	 * Executes the item
	 * 
	 * @param plr Player who executed
	 * @param click 
	 */
	public void execute(Player plr, ClickType click);
	
	/**
	 * Returns the ItemStack of the item
	 * 
	 * @return The item
	 */
	public ItemStack getItem();
	
}
