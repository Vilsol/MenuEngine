package me.vilsol.menuengine.engine;

import me.vilsol.menuengine.MenuEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;

public class DynamicMenu extends Menu {

	private HashMap<Integer, MenuItem> dynamicItems = new HashMap<Integer, MenuItem>();
	private boolean clearOnClose = true;
	private DynamicMenuModel parent;
	private Player owner;
	
	public DynamicMenu(int size, DynamicMenuModel parent, Player owner) {
		super(parent.getSize(owner).getSize(), null, null);
		this.parent = parent;
		this.owner = owner;
	}
	
	public DynamicMenuModel getDynamicParent(){
		return parent;
	}
	
	public Player getOwner(){
		return owner;
	}
	
	@Override
	public void regenerateInventory() {
		this.size = (parent == null) ? this.size : parent.getSize(owner).getSize();
		if(name == null){
			inventory = Bukkit.createInventory(null, size);
		}else{
			inventory = Bukkit.createInventory(null, size, this.name);
		}
	}
	
	@Override
	public void showToPlayer(Player plr) {
		regenerateInventory();
		parent.addItems(this, plr);
		
		for(Entry<Integer, MenuItem> item : items.entrySet()) {
			inventory.setItem(item.getKey(), item.getValue().getItem());
		}
		
		plr.openInventory(inventory);
		DynamicMenuModel.setLastMenu(plr, parent.getClass());
	}
	
	public void addItemDynamic(Class<? extends MenuItem> itemClass, int slot) {
		if(!MenuItem.items.containsKey(itemClass)) return;
		MenuItem item = MenuItem.items.get(itemClass);
		inventory.setItem(slot, item.getItem());
		dynamicItems.put(slot, item);
	}
	
	public void addItemDynamic(MenuItem item, int slot) {
		inventory.setItem(slot, item.getItem());
		dynamicItems.put(slot, item);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void addItemDynamic(Class<? extends BonusItem<?>> itemClass, int slot, T bonus) {
		if(!MenuItem.items.containsKey(itemClass)) return;
		MenuItem item = MenuItem.items.get(itemClass);
		
		if(bonus != null) {
			try {
				item = (MenuItem) itemClass.getConstructors()[0].newInstance();
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
			
			if(!(item instanceof BonusItem)) return;
			
			((BonusItem<T>) item).setBonusData(bonus);
		}
		
		inventory.setItem(slot, item.getItem());
		dynamicItems.put(slot, item);
	}
	
	public void placeItem(final int slot, final ItemStack item) {
		final DynamicMenu thisMenu = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				parent.onPlaceItem(thisMenu, item, slot);
			}
		}.runTaskLater(MenuEngine.getPlugin(), 1L);
		delayedRefresh();
	}
	
	public void removePlaced(final int slot, final ItemStack item) {
		final DynamicMenu thisMenu = this;
		new BukkitRunnable() {
				@Override
				public void run() {
					parent.onPickupItem(thisMenu, item, slot);
				}
			}.runTaskLater(MenuEngine.getPlugin(), 1L);
		delayedRefresh();
	}

	@SuppressWarnings("deprecation")
	public void delayedRefresh(){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(inventory.getViewers() != null && inventory.getViewers().size() > 0) ((Player) inventory.getViewers().get(0)).updateInventory();
			}
		}.runTaskLater(MenuEngine.getPlugin(), 2L);
	}
	
	public HashMap<Integer, MenuItem> getDynamicItems(){
		return dynamicItems;
	}

	public boolean isClearOnClose() {
		return clearOnClose;
	}

	public void setClearOnClose(boolean clearOnClose) {
		this.clearOnClose = clearOnClose;
	}
	
}
