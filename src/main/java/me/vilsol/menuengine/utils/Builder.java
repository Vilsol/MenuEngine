package me.vilsol.menuengine.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Builder {
	
	private ItemStack item;
	
	public Builder(Material material){
		item = new ItemStack(material);
	}

	public Builder setDurability(short durability){
		item.setDurability(durability);
		return this;
	}
	
	public Builder setType(Material material){
		item.setType(material);
		return this;
	}
	
	public Builder setAmount(int amount){
		item.setAmount(amount);
		return this;
	}
	
	public Builder setName(String name){
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(name);
		item.setItemMeta(m);
		return this;
	}
	
	public ItemStack getItem(){
		return item;
	}
	
}
