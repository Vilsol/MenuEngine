package me.vilsol.menuengine.engine;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MenuModel implements Listener {

	public static HashMap<Class<? extends MenuModel>, MenuModel> menus = new HashMap<Class<? extends MenuModel>, MenuModel>();
	public static HashMap<Player, Class<? extends MenuModel>> last_menu = new HashMap<Player, Class<? extends MenuModel>>();
	protected Menu menu;
	
	protected MenuModel(int size, String name) {
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
	
	public Menu getMenu(){
		return menu;
	}
	
}
