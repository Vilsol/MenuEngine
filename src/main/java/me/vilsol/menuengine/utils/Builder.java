package me.vilsol.menuengine.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Builder {
	
	private ItemStack item;
	
	public Builder(Material material){
		item = new ItemStack(material);
	}

    public Builder durability(short durability){
        item.setDurability(durability);
        return this;
    }

    public Builder type(Material material){
        item.setType(material);
        return this;
    }

    public Builder amount(int amount){
        item.setAmount(amount);
        return this;
    }

    public Builder name(String name){
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);
        return this;
    }

    public Builder lore(List<String> lore){
        ItemMeta m = item.getItemMeta();
        m.setLore(lore);
        item.setItemMeta(m);
        return this;
    }

    public ItemStack item(){
        return item;
    }

    @Deprecated
    public Builder setDurability(short durability){
        return durability(durability);
    }

    @Deprecated
    public Builder setType(Material material){
        return type(material);
    }

    @Deprecated
    public Builder setAmount(int amount){
        return amount(amount);
    }

    @Deprecated
    public Builder setName(String name){
        return name(name);
    }

    @Deprecated
    public Builder setLore(List<String> lore){
        return lore(lore);
    }

    @Deprecated
    public ItemStack getItem(){
        return item();
    }
	
}
