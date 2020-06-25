package me.gabriel.gfacchest.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gabriel.gfacchest.Main;

public class InventoryUtils {
	
	@SuppressWarnings("deprecation")
	public static int getMoney(final Player player) {
		return Integer.valueOf((int) Main.eco.getBalance(player.getName()));
	}
	
	@SuppressWarnings("deprecation")
	public static void removeMoney(final Player player, final int i) {
		Main.eco.withdrawPlayer(player.getName(), i);
	}
	
	public static boolean isMoney(final Player player, final int i) {
		if (getMoney(player) >= i) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static int getCash(final Player player) {
		return Main.getCash().getAPI().look(player.getName());
	}
	
	@SuppressWarnings("deprecation")
	public static void removeCash(final Player player, final int i) {
		Main.getCash().getAPI().take(player.getName(), i);
	}
	
	public static boolean isCash(final Player player, final int i) {
		if (getCash(player) >= i) {
			return true;
		}
		return false;
	}

	public static boolean inventoryIsEmpty(final Inventory inventario) {
		for (ItemStack i : inventario.getContents()) {
			if(i != null && !(i.getType() == Material.AIR)) return false;
		}
		return true;
	}

}
