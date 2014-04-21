package me.vilsol.menuengine.enums;

import org.bukkit.event.inventory.InventoryAction;

public enum ClickType {
	
	RIGHT, LEFT, MIDDLE, SHIFT;
	
	public static ClickType getTypeFromAction(InventoryAction action){
		switch(action){
			case PICKUP_ALL:
			case PLACE_ALL:
			default: 
				return LEFT;
			case PICKUP_HALF:
			case PLACE_ONE:
				return RIGHT;
			case CLONE_STACK:
				return MIDDLE;
			case MOVE_TO_OTHER_INVENTORY:
				return SHIFT;
		}
	}
}
