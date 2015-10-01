package me.vilsol.menuengine.listeners;

import me.vilsol.menuengine.engine.ChatCallback;
import me.vilsol.menuengine.engine.DynamicMenu;
import me.vilsol.menuengine.engine.DynamicMenuModel;
import me.vilsol.menuengine.engine.MenuModel;

import me.vilsol.menuengine.utils.CallbackResult;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
		
		Player p = (Player) e.getWhoClicked();
		
		for(MenuModel m : MenuModel.getAllMenus().values()) {
			Inventory i = m.getMenu().isThisInventory(e.getInventory());
			if(i == null) continue;
			e.setCancelled(true);
			p.setItemOnCursor(new ItemStack(Material.AIR));
			if(!m.getMenu().isOurItem(e.getCurrentItem())) continue;
			if(!m.getMenu().getItems().containsKey(e.getSlot())) continue;
			m.getMenu().getItems().get(e.getSlot()).execute(p, e.getClick());
			return;
		}
		
		DynamicMenu i = DynamicMenuModel.getMenu(p);
		if(i == null) return;
		DynamicMenuModel m = i.getDynamicParent();
		
		if(e.getRawSlot() < p.getOpenInventory().getTopInventory().getSize()){
			if(e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PICKUP_ONE || e.getAction() == InventoryAction.PICKUP_SOME || e.getAction() == InventoryAction.CLONE_STACK || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
				CallbackResult result = new CallbackResult();
				m.canPickupItem(i, p, e.getRawSlot(), e.getInventory().getItem(e.getRawSlot()), result, e.getClick());

				if(result.canHandle()){
					i.removePlaced(e.getRawSlot(), e.getInventory().getItem(e.getRawSlot()));
				}else{
					e.setCancelled(true);
				}

				if(result.callItem()){
					if(i.getDynamicItems().containsKey(e.getSlot())) {
						i.getDynamicItems().get(e.getSlot()).execute(p, e.getClick());
						p.setItemOnCursor(new ItemStack(Material.AIR));
					}else if(i.getItems().containsKey(e.getSlot())){
						i.getItems().get(e.getSlot()).execute(p, e.getClick());
						p.setItemOnCursor(new ItemStack(Material.AIR));
					}
				}
			}else{
				if(e.getAction() == InventoryAction.SWAP_WITH_CURSOR || e.getAction() == InventoryAction.PLACE_ALL || e.getAction() == InventoryAction.PLACE_ONE || e.getAction() == InventoryAction.PLACE_SOME){
					CallbackResult result = new CallbackResult();
					m.canPlaceItem(i, p, e.getRawSlot(), e.getCursor(), result, e.getClick());

					if(result.canHandle()){
						i.placeItem(e.getRawSlot(), e.getCursor());
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
		for(MenuModel m : MenuModel.getAllMenus().values()) {
			Inventory i = m.getMenu().isThisInventory(e.getInventory());
			if(i == null) continue;
			e.setCancelled(true);
		}
		
		DynamicMenu i = DynamicMenuModel.getMenu((Player) e.getWhoClicked());
		if(i == null) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		DynamicMenuModel.cleanInventories((Player) e.getPlayer(), e.getInventory());
	}

}
