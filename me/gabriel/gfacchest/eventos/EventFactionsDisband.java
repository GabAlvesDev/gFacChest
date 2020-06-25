package me.gabriel.gfacchest.eventos;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import me.gabriel.gfacchest.manager.inventory.Manager_Inventory;
import me.gabriel.gfacchest.utils.InventoryUtils;

public class EventFactionsDisband implements Listener{
	
	private final Manager_Inventory chestManager;
	
	public EventFactionsDisband(final Manager_Inventory chestManager) {
		this.chestManager = chestManager;
	}
	
	@EventHandler
	private void onEventFactionsDisband(final com.massivecraft.factions.event.EventFactionsDisband event) {
		final String nome_da_fac��o = event.getFaction().getName();
		final Player player = event.getMPlayer().getPlayer();
		if (!this.chestManager.isChest(nome_da_fac��o)) {
			Bukkit.getConsoleSender().sendMessage("�c[gFacChest] A fac��o <fac> n�o adquiriu o ba� de fac��o por tanto n�o tem oque excluir!".replace("<fac>", nome_da_fac��o));
			return;
		}
		final Inventory ba�_da_fac��o = this.chestManager.getChest(nome_da_fac��o);
		if (!InventoryUtils.inventoryIsEmpty(ba�_da_fac��o)) {
			player.sendMessage("�cVoc� n�o pode desfazer a fac��o, ainda existe itens dentro do ba� da fac��o, recolha todos para poder desfazer sua fac��o!");
			event.setCancelled(true);
			return;
		}
		this.chestManager.deleteChest(nome_da_fac��o);
		player.sendMessage("�aO ba� da fac��o estava v�zio e foi deletado.");
	}

}
