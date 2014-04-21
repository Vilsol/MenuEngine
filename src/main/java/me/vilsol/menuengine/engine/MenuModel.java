package me.vilsol.menuengine.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuModel implements Listener {

	public static HashMap<Class<? extends MenuModel>, MenuModel> menus = new HashMap<Class<? extends MenuModel>, MenuModel>();
	public static HashMap<Player, Class<? extends MenuModel>> last_menu = new HashMap<Player, Class<? extends MenuModel>>();
	protected Inventory inv;
	public Map<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();
	protected long lastClick = 0;
	protected int size;
	
	/**
	 * Creates a new menu with inventories that contains multiple MenuItem's,
	 * which are executed when they are clicked in this specific menu.
	 * 
	 * @param size The size of the menu (Must be divisible by 9)
	 * @param name The name of the menu
	 */
	protected MenuModel(int size, String name) {
		if(name == null){
			inv = Bukkit.createInventory(null, size);
		} else {
			inv = Bukkit.createInventory(null, size, name);
			MenuModel.menus.put(this.getClass(), this);
		}
		this.size = size;
	}
	
	public static void openLastMenu(Player plr){
		if(!last_menu.containsKey(plr)) return;
		Class<? extends MenuModel> last = last_menu.get(plr);
		if(DynamicMenuModel.class.isAssignableFrom(last)){
			DynamicMenuModel.menus.get(last).showToPlayer(plr);
		}else{
			menus.get(last).showToPlayer(plr);
		}
	}
	
	public void addItem(Class<? extends MenuItem> itemClass, int slot) {
		addItem(itemClass, slot, null);
	}
	
	public void addItem(Class<? extends MenuItem> itemClass, int slot, Object bonus) {
		if(!MenuItem.items.containsKey(itemClass)) return;
		MenuItem item = MenuItem.items.get(itemClass);
		
		if(bonus != null){
			try {
				item = (MenuItem) itemClass.getConstructors()[0].newInstance();
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
			((BonusItem) item).setBonusData(bonus);
		}
		
		if(inv.getItem(slot) == null || inv.getItem(slot).getType() == Material.AIR) {
			inv.setItem(slot, item.getItem());
		} else {
			inv.addItem(item.getItem());
		}
		
		items.put(slot, item);
	}
	
	public void showToPlayer(Player plr) {
		plr.openInventory(inv);
		last_menu.put(plr, this.getClass());
	}
	
	public void setName(String name) {
		Inventory newInv = Bukkit.createInventory(null, inv.getSize(), name);
		newInv.setContents(inv.getContents());
		inv = newInv;
	}
	
	/**
	 * Check if the item is in this inventory
	 * 
	 * @param item The item
	 * @return True if in this inventory
	 */
	public boolean isOurItem(ItemStack item) {
		for (MenuItem i : items.values()) {
			if(i.getItem().equals(item)) { return true; }
		}
		return false;
	}
	
	public Inventory isThisInventory(Inventory i, Player plr){
		if(i.getName().equals(inv.getName())) return inv;
		return null;
	}
	
}
