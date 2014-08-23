package me.vilsol.menuengine.engine;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class MenuModel implements Listener {

	private static HashMap<Class<? extends MenuModel>, MenuModel> menus = new HashMap<Class<? extends MenuModel>, MenuModel>();
	private static HashMap<Player, Class<? extends MenuModel>> last_menu = new HashMap<Player, Class<? extends MenuModel>>();
	protected Menu menu;
	
	public MenuModel(int size, String name) {
		menu = new Menu(size, name, this);
		if(name != null){
			MenuModel.menus.put(this.getClass(), this);
		}
	}
	
	public static void openLastMenu(Player plr){
		if(!last_menu.containsKey(plr)) return;
		Class<? extends MenuModel> last = last_menu.get(plr);
		if(DynamicMenuModel.class.isAssignableFrom(last)){
			DynamicMenuModel.getMenu(plr).showToPlayer(plr);
		}else{
			menus.get(last).getMenu().showToPlayer(plr);
		}
	}

	public static void setLastMenu(Player plr, Class<? extends MenuModel> menu){
		last_menu.put(plr, menu);
	}
	
	public static MenuModel getMenu(Class<? extends MenuModel> menu){
		return menus.get(menu);
	}
	
	public static HashMap<Class<? extends MenuModel>, MenuModel> getAllMenus(){
		return menus;
	}
	
	public Menu getMenu(){
		return menu;
	}
	
}
