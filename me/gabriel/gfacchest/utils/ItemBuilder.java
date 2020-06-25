package me.gabriel.gfacchest.utils;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.*;

public class ItemBuilder
{
    private ItemStack is;
    
    public ItemBuilder(final Material m) {
        this(m, 1);
    }
    
    public ItemBuilder(final ItemStack is) {
        this.is = is;
    }
    
    public ItemBuilder(final Material m, final int quantia) {
        this.is = new ItemStack(m, quantia);
    }
    
    public ItemBuilder(final Material m, final int quantia, final byte durabilidade) {
        this.is = new ItemStack(m, quantia, (short)durabilidade);
    }
    
    public ItemBuilder(final Material m, final int quantia, final int durabilidade) {
        this.is = new ItemStack(m, quantia, (short)durabilidade);
    }
    
    public ItemBuilder setAmount(final int amount) {
        this.is.setAmount(amount);
        final ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder setEnchant(final Enchantment enchant, final int nível) {
    	final ItemMeta im = this.is.getItemMeta();
    	im.addEnchant(enchant, nível, true);
    	this.is.setItemMeta(im);
    	return this;
    }
    
    public ItemBuilder setName(final String nome) {
        final ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(nome);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder setSkullOwner(final String dono) {
        try {
            final SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(dono);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (Throwable t) {}
        return this;
    }
    
    public ItemBuilder addItemFlag(final ItemFlag flag) {
        final ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(new ItemFlag[] { flag });
        this.is.setItemMeta(im);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ItemBuilder setLore(final String... lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore((List)Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ItemBuilder setLore(final String string2, final String string3, final String string4, final String string5, final String string6, final String string7, final String string8, final String string9, final List<String> lista1, final String string10, final String string11, final String string12, final String string13, final String string14, final List<String> lista2) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> l = new ArrayList<String>();
        l.add(string2);
        l.add(string3);
        l.add(string4);
        l.add(string5);
        l.add(string6);
        l.add(string7);
        l.add(string8);
        l.add(string9);
        l.addAll(lista1);
        l.add(string10);
        l.add(string11);
        l.add(string12);
        l.add(string13);
        l.add(string14);
        l.addAll(lista2);
        im.setLore((List)l);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder setLeatherArmorColor(final Color cor) {
        try {
            final LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(cor);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (Throwable t) {}
        return this;
    }
    
    public ItemStack toItemStack() {
        return this.is;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ItemBuilder setLore(final String string, final String string2, final String string3, final String string4, final String string5, final String string6, final String top, final String string7, final String string8, final List<String> fplayers, final String string9, final String string10, final String string11) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> l = new ArrayList<String>();
        l.add(string);
        l.add(string2);
        l.add(string3);
        l.add(string4);
        l.add(string5);
        l.add(string6);
        l.add(top);
        l.add(string7);
        l.add(string8);
        l.addAll(fplayers);
        l.add(string9);
        l.add(string10);
        l.add(string11);
        im.setLore((List)l);
        this.is.setItemMeta(im);
        return this;
    }
}
