package me.vilsol.menuengine.engine;

import java.util.HashMap;

import me.vilsol.menuengine.enums.InventorySize;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class DynamicMenuModel {
	
	private static HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel> menus = new HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel>();
	private static HashMap<Player, Class<? extends DynamicMenuModel>> last_menu = new HashMap<Player, Class<? extends DynamicMenuModel>>();
	private static HashMap<Player, DynamicMenu> playerMenus = new HashMap<Player, DynamicMenu>();
	
	protected DynamicMenuModel() {
		menus.put(this.getClass(), this);
	}
	
	public abstract void addItems(DynamicMenu i, Player plr);
	
	public abstract InventorySize getSize(Player plr);
	
	public abstract boolean canPlaceItem(DynamicMenu i, Player plr, int slot, ItemStack item);
	
	public abstract void onPickupItem(DynamicMenu i, ItemStack item, int slot);
	
	public abstract void onPlaceItem(DynamicMenu i, ItemStack item, int slot);
	
	public static DynamicMenu createMenu(Player plr, Class<? extends DynamicMenuModel> model){
		playerMenus.put(plr, new DynamicMenu(menus.get(model).getSize(plr).getSize(), menus.get(model), plr));
		return playerMenus.get(plr);
	}
	
	public static DynamicMenu getPlayerMenu(Player plr) {
		if(playerMenus.containsKey(plr)) return playerMenus.get(plr);
		return null;
	}
	
	public static DynamicMenu getMenu(Player plr){
		return playerMenus.get(plr);
	}
	
	public static Class<? extends DynamicMenuModel> getLastMenu(Player plr){
		return last_menu.get(plr);
	}
	
	public static void setLastMenu(Player plr, Class<? extends DynamicMenuModel> model){
		last_menu.put(plr, model);
	}

	public static void openLastMenu(Player plr) {
		if(last_menu.containsKey(plr)) playerMenus.get(plr).showToPlayer(plr);
	}

	public static void cleanInventories(Player plr, Inventory inventory) {
		if(playerMenus.containsKey(plr) && playerMenus.get(plr).isThisInventory(inventory) != null) playerMenus.remove(plr);
	}
	
}
