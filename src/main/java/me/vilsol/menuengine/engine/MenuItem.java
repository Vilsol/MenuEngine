package me.vilsol.menuengine.engine;

import java.util.HashMap;

import me.vilsol.menuengine.enums.ClickType;

import org.bukkit.entity.Player;
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
