package me.vilsol.menuengine.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {
	
	protected Inventory inventory;
	protected HashMap<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();
	protected long lastClick = 0;
	protected int size;
	protected String name;
	protected MenuModel parent;
	
	public Menu(int size, String name, MenuModel parent) {
		this.size = size;
		this.name = name;
		this.parent = parent;
		regenerateInventory();
	}
	
	public void regenerateInventory(){
		if(name == null){
			inventory = Bukkit.createInventory(null, this.size);
		}else{
			inventory = Bukkit.createInventory(null, this.size, this.name);
		}
	}
	
	public HashMap<Integer, MenuItem> getItems(){
		return items;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public long getLastClick(){
		return lastClick;
	}
	
	public int getSize(){
		return size;
	}
	
	public String getName(){
		return name;
	}
	
	public MenuModel getParent(){
		return parent;
	}
	
	public void setName(String name) {
		Inventory newInv = Bukkit.createInventory(null, size, name);
		newInv.setContents(inventory.getContents());
		inventory = newInv;
	}
	
	public boolean isOurItem(ItemStack item) {
		for (MenuItem i : items.values()) {
			if(i.getItem().equals(item)) { return true; }
		}
		return false;
	}
	
	public Inventory isThisInventory(Inventory i){
		if(i.getName().equals(inventory.getName())) return inventory;
		return null;
	}
	
	public void showToPlayer(Player plr) {
		plr.openInventory(inventory);
		MenuModel.setLastMenu(plr, parent.getClass());
	}
	
	public void addItem(Class<? extends MenuItem> itemClass, int slot) {
		addItem(itemClass, slot, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void  addItem(Class<? extends MenuItem> itemClass, int slot, T bonus) {
		if(!MenuItem.items.containsKey(itemClass)) return;
		MenuItem item = MenuItem.items.get(itemClass);
		
		if(bonus != null){
			try {
				item = (MenuItem) itemClass.getConstructors()[0].newInstance();
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
			
			if(!(item instanceof BonusItem)) return;
			
			((BonusItem<T>) item).setBonusData(bonus);
		}
		
		inventory.setItem(slot, item.getItem());
		
		items.put(slot, item);
	}
	
}
