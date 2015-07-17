package me.vilsol.menuengine.engine;

import me.vilsol.menuengine.MenuEngine;
import me.vilsol.menuengine.enums.InventorySize;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class DynamicMenuModel {
	
	private static HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel> menus = new HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel>();
	private static HashMap<Player, Class<? extends DynamicMenuModel>> last_menu = new HashMap<Player, Class<? extends DynamicMenuModel>>();
	private static HashMap<Player, DynamicMenu> playerMenus = new HashMap<Player, DynamicMenu>();
	private static HashMap<Class<? extends DynamicMenuModel>, Class<? extends DynamicMenu>> menu_workers = new HashMap<Class<? extends DynamicMenuModel>, Class<? extends DynamicMenu>>();
	
	public DynamicMenuModel(){
		menus.put(this.getClass(), this);
		menu_workers.put(this.getClass(), DynamicMenu.class);
	}
	
	public DynamicMenuModel(Class<? extends DynamicMenu> menu) {
		menus.put(this.getClass(), this);
		menu_workers.put(this.getClass(), menu);
	}
	
	public abstract void addItems(DynamicMenu i, Player plr);
	
	public abstract InventorySize getSize(Player plr);

	public abstract boolean canPlaceItem(DynamicMenu i, Player plr, int slot, ItemStack item);

	public abstract boolean canPickupItem(DynamicMenu i, Player plr, int slot, ItemStack item);
	
	public abstract void onPickupItem(DynamicMenu i, ItemStack item, int slot);
	
	public abstract void onPlaceItem(DynamicMenu i, ItemStack item, int slot);
	
	public static DynamicMenu createMenu(Player plr, Class<? extends DynamicMenuModel> model){
		Class<? extends DynamicMenu> worker = menu_workers.get(model);
		try {
			DynamicMenu menu = (DynamicMenu) worker.getConstructors()[0].newInstance(menus.get(model).getSize(plr).getSize(), menus.get(model), plr);
			playerMenus.put(plr, menu);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			System.err.println("Wrong Constructor For " + worker.toString() + ", Using default!");
			playerMenus.put(plr, new DynamicMenu(menus.get(model).getSize(plr).getSize(), menus.get(model), plr));
		}
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
		if(last_menu.containsKey(plr)){
			if(playerMenus.containsKey(plr)){
				playerMenus.get(plr).showToPlayer(plr);
			}else{
				createMenu(plr, last_menu.get(plr)).showToPlayer(plr);
			}
		}
	}

	public static void cleanInventories(final Player plr, Inventory inventory) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!playerMenus.containsKey(plr)) return;
				if(playerMenus.get(plr).getInventory().getViewers().size() > 0) return;
				if(playerMenus.get(plr).isClearOnClose()) playerMenus.remove(plr);
			}
		}.runTaskLater(MenuEngine.getPlugin(), 1L);
	}
	
}
