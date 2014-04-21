package me.vilsol.menuengine.listeners;

import me.vilsol.menuengine.engine.ChatCallback;
import me.vilsol.menuengine.engine.DynamicMenuModel;
import me.vilsol.menuengine.engine.MenuModel;
import me.vilsol.menuengine.enums.ClickType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

public class ItemListener implements Listener {
	
	@EventHandler
	public void onAsyncChatEvent(AsyncPlayerChatEvent e) {
		if(ChatCallback.locked_players.containsKey(e.getPlayer())) {
			ChatCallback.locked_players.get(e.getPlayer()).onChatMessage(e);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getOpenInventory() == null) return;
		if(e.getSlot() < 0) return;
		
		for(MenuModel m : MenuModel.menus.values()) {
			Inventory i = m.isThisInventory(e.getInventory(), (Player) e.getWhoClicked());
			if(i == null) continue;
			e.setCancelled(true);
			if(!m.isOurItem(e.getCurrentItem())) continue;
			m.items.get(e.getSlot()).execute((Player) e.getWhoClicked(), ClickType.getTypeFromAction(e.getAction()));
			return;
		}
		
		Inventory i = DynamicMenuModel.getPlayerInventory((Player) e.getWhoClicked());
		if(i == null) return;
		DynamicMenuModel m = DynamicMenuModel.getModelFromInventory(i);
		
		if(e.getRawSlot() < e.getWhoClicked().getOpenInventory().getTopInventory().getSize()){
			if(e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PICKUP_SOME){
				if(DynamicMenuModel.isPlaced(i, e.getRawSlot())){
					((DynamicMenuModel) m).removePlaced(i, e.getRawSlot());
				}else{
					e.setCancelled(true);
					if(DynamicMenuModel.getInventoryItems(i).containsKey(e.getSlot())) {
						DynamicMenuModel.getInventoryItems(i).get(e.getSlot()).execute((Player) e.getWhoClicked(), ClickType.getTypeFromAction(e.getAction()));
					}
				}
			}else{
				if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR || e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_ONE || e.getAction() == InventoryAction.PLACE_SOME){
					boolean allow = ((DynamicMenuModel) m).canPlaceItem(i, (Player) e.getWhoClicked(), e.getRawSlot(), e.getCursor());
					if(allow){
						((DynamicMenuModel) m).placeItem(i, e.getRawSlot(), e.getCursor());
					}else{
						e.setCancelled(true);
					}
				}else{
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e){
		for(MenuModel m : MenuModel.menus.values()) {
			Inventory i = m.isThisInventory(e.getInventory(), (Player) e.getWhoClicked());
			if(i == null) continue;
			e.setCancelled(true);
		}
		
		Inventory i = DynamicMenuModel.getPlayerInventory((Player) e.getWhoClicked());
		if(i == null) return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		DynamicMenuModel.cleanInventories((Player) e.getPlayer(), e.getInventory());
	}
	
}
