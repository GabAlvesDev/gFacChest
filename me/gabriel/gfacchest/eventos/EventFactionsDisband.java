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
		final String nome_da_facção = event.getFaction().getName();
		final Player player = event.getMPlayer().getPlayer();
		if (!this.chestManager.isChest(nome_da_facção)) {
			Bukkit.getConsoleSender().sendMessage("§c[gFacChest] A facção <fac> não adquiriu o baú de facção por tanto não tem oque excluir!".replace("<fac>", nome_da_facção));
			return;
		}
		final Inventory baú_da_facção = this.chestManager.getChest(nome_da_facção);
		if (!InventoryUtils.inventoryIsEmpty(baú_da_facção)) {
			player.sendMessage("§cVocê não pode desfazer a facção, ainda existe itens dentro do baú da facção, recolha todos para poder desfazer sua facção!");
			event.setCancelled(true);
			return;
		}
		this.chestManager.deleteChest(nome_da_facção);
		player.sendMessage("§aO baú da facção estava vázio e foi deletado.");
	}

}
