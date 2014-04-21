package me.vilsol.menuengine.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.vilsol.menuengine.MenuEngine;
import me.vilsol.menuengine.enums.InventorySize;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public abstract class DynamicMenuModel extends MenuModel {
	
	private String name;
	protected static HashMap<Player, Inventory> playerMenus = new HashMap<Player, Inventory>();
	protected static HashMap<Inventory, HashMap<Integer, MenuItem>> inventories = new HashMap<Inventory, HashMap<Integer, MenuItem>>();
	protected static HashMap<Inventory, HashMap<Integer, ItemStack>> placed = new HashMap<Inventory, HashMap<Integer, ItemStack>>();
	protected static HashMap<Inventory, DynamicMenuModel> models = new HashMap<Inventory, DynamicMenuModel>();
	public static HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel> menus = new HashMap<Class<? extends DynamicMenuModel>, DynamicMenuModel>();
	
	protected DynamicMenuModel() {
		super(InventorySize.S_54.getSize(), null);
		menus.put(this.getClass(), this);
	}
	
	public abstract void addItems(Inventory i, Player plr);
	
	public abstract InventorySize getSize(Player plr);
	
	public abstract boolean canPlaceItem(Inventory i, Player plr, int slot, ItemStack item);
	
	public abstract void onPickupItem(Inventory i, ItemStack item, int slot);
	
	public abstract void onPlaceItem(Inventory i, ItemStack item, int slot);
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	public static void addItemDynamic(Class<? extends MenuItem> itemClass, Inventory i, int slot) {
		addItemDynamic(itemClass, i, slot, null);
	}
	
	public static void addItemDynamic(MenuItem item, Inventory i, int slot) {
		i.setItem(slot, item.getItem());
		if(!inventories.containsKey(i)) inventories.put(i, new HashMap<Integer, MenuItem>());
		inventories.get(i).put(slot, item);
	}
	
	public static void addItemDynamic(Class<? extends MenuItem> itemClass, Inventory i, int slot, Object bonus) {
		if(!MenuItem.items.containsKey(itemClass)) return;
		MenuItem item = MenuItem.items.get(itemClass);
		
		if(bonus != null) {
			try {
				item = (MenuItem) itemClass.getConstructors()[0].newInstance();
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
			((BonusItem) item).setBonusData(bonus);
		}
		
		i.setItem(slot, item.getItem());
		if(!inventories.containsKey(i)) inventories.put(i, new HashMap<Integer, MenuItem>());
		inventories.get(i).put(slot, item);
	}
	
	public void placeItem(final Inventory i, final int slot, final ItemStack item) {
		placed.get(i).put(slot, item);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				onPlaceItem(i, item, slot);
			}
		}.runTaskLater(MenuEngine.plugin, 1L);
		delayedRefresh(i);
	}
	
	public static boolean isPlaced(Inventory i, int slot) {
		return placed.get(i).containsKey(slot);
	}
	
	public void removePlaced(final Inventory i, final int slot) {
		if(placed.get(i).containsKey(slot)){
			final ItemStack pl = placed.get(i).get(slot);
			placed.get(i).remove(slot);
			new BukkitRunnable() {
				@Override
				public void run() {
					onPickupItem(i, pl, slot);
				}
			}.runTaskLater(MenuEngine.plugin, 1L);
		}
		delayedRefresh(i);
	}
	
	protected static void delayedRefresh(final Inventory i){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(i.getViewers() != null && i.getViewers().size() > 0) ((Player) i.getViewers().get(0)).updateInventory();
			}
		}.runTaskLater(MenuEngine.plugin, 2L);
	}
	
	@Override
	public void showToPlayer(Player plr) {
		Inventory i = null;
		if(name != null) i = Bukkit.createInventory(plr, getSize(plr).getSize(), name);
		else i = Bukkit.createInventory(plr, getSize(plr).getSize());
		
		inventories.put(i, new HashMap<Integer, MenuItem>());
		placed.put(i, new HashMap<Integer, ItemStack>());
		
		if(inv != null && inv.getContents().length > 0) {
			for(int x = 0; x < i.getSize(); x++) {
				if(inv.getItem(x) != null && inv.getItem(x).getType() != Material.AIR) {
					i.setItem(x, inv.getItem(x));
					if(inventories.get(i).containsKey(x)) inventories.get(i).put(x, items.get(x));
				}
			}
		}
		
		addItems(i, plr);
		
		plr.openInventory(i);
		last_menu.put(plr, this.getClass());
		playerMenus.put(plr, i);
		models.put(i, this);
	}
	
	public static Inventory getPlayerInventory(Player plr) {
		if(playerMenus.containsKey(plr)) return playerMenus.get(plr);
		return null;
	}
	
	public static HashMap<Integer, MenuItem> getInventoryItems(Inventory i) {
		if(inventories.containsKey(i)) return inventories.get(i);
		return null;
	}
	
	public static void cleanInventories(Player plr, Inventory i) {
		List<Inventory> remove = new ArrayList<Inventory>();
		for(Inventory v : inventories.keySet()) {
			if(!v.getName().equals(i.getName())) continue;
			if(v.getSize() != i.getSize()) continue;
			if(v.getHolder().getInventory() != plr.getInventory()) continue;
			if(v.getHolder() == plr) remove.add(i);
		}
		for(Inventory v : remove) {
			inventories.remove(v);
			placed.remove(v);
		}
		playerMenus.remove(plr);
	}

	public static DynamicMenuModel getModelFromInventory(Inventory i) {
		if(models.containsKey(i)) return models.get(i);
		return null;
	}
	
}
