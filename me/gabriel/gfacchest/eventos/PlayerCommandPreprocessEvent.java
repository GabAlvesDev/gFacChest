package me.gabriel.gfacchest.eventos;

import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;

import me.gabriel.gfacchest.Main;
import me.gabriel.gfacchest.manager.inventory.Manager_Inventory;
import me.gabriel.gfacchest.utils.InventoryUtils;
import me.gabriel.gfacchest.utils.ItemBuilder;

public class PlayerCommandPreprocessEvent implements Listener {
    private final Manager_Inventory chestManager;
    
    public PlayerCommandPreprocessEvent(final Manager_Inventory chestManager) {
        this.chestManager = chestManager;
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(final org.bukkit.event.player.PlayerCommandPreprocessEvent e) {
        final String message = e.getMessage().toLowerCase();
        if (message.startsWith("/f bau") || message.startsWith("/f chest")) {
            e.setCancelled(true);
            final Player player = e.getPlayer();
            final MPlayer mplayer = MPlayer.get(player);
            final String nome_da_facção = mplayer.getFaction().getName();
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
            	player.sendMessage("§cVocê não pode acessar o baú da facção com o modo de jogo §lCRIATIVO §cativado, desative o criativo e digite o comando novamente!");
            	return;
            }
            if (!mplayer.hasFaction()) {
            	player.sendMessage("§cVocê não está em nenhuma facção, portanto você não tem acesso a nenhum báu de facção!");
            	return;
            }
            if (!this.chestManager.isChest(nome_da_facção)) {
            	if (mplayer.getRole().equals(Rel.LEADER)) {
                    int custo_money = Integer.valueOf(Main.get().getConfig().getInt("Config.Custo_De_Compra_Money"));
                    int custo_cash = Integer.valueOf(Main.get().getConfig().getInt("Config.Custo_De_Compra_Cash"));
                	abrirMenu(player, Main.get().getConfig().getInt("Config.Modo_Compra"), custo_cash, custo_money);
                	return;
            	}
            	player.sendMessage("§cSua facção ainda §lNÃO §cadquiriu um baú, peça ao seu líder para comprar o baú da facção.");
            	return;
            }
            final Inventory baú = this.chestManager.getChest(nome_da_facção);
            player.openInventory(baú);
        }
    }

	private void abrirMenu(final Player p, final int i, final int cash, final int money) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, "§8Qual moeda deseja usar?");
        final ItemStack Moedas_1 = new ItemBuilder(Material.IRON_INGOT).setName("§cMoedas").setLore("§7Este item não pode ser comprado com $.").toItemStack();
        final ItemStack Moedas_2 = new ItemBuilder(Material.IRON_INGOT).setName("§aMoedas").setLore("§7Você gastará §2" + format(money) + " §7$.").toItemStack();
        final ItemStack Cash_1 = new ItemBuilder(Material.GOLD_INGOT).setEnchant(Enchantment.DURABILITY, 1).setName("§cCash").setLore("§7Este item não pode ser comprado com cash.").addItemFlag(ItemFlag.HIDE_ENCHANTS).toItemStack();
        final ItemStack Cash_2 = new ItemBuilder(Material.GOLD_INGOT).setEnchant(Enchantment.DURABILITY, 1).setName("§aCash").setLore("§7Você gastará §6" + format(cash) + " §7cash.").addItemFlag(ItemFlag.HIDE_ENCHANTS).toItemStack();
        final ItemStack sair = new ItemBuilder(Material.ARROW).setName("§cSair").setLore("§7Clique para sair ").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).toItemStack();
        if (i == 1) {
            inv.setItem(11, Moedas_1);
        }
        if (i == 2) {
            inv.setItem(11, Moedas_2);
        }
        if (i == 3) {
        	inv.setItem(11, Moedas_2);
        }
        if (i == 1) {
        	inv.setItem(15, Cash_2);
        }
        if (i == 2) {
        	inv.setItem(15, Cash_1);
        }
        if (i == 3) {
        	inv.setItem(15, Cash_2);
        }
        inv.setItem(31, sair);
        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        if (!e.getClickedInventory().getTitle().equals("§8Qual moeda deseja usar?")) {
            return;
        }
        final Player p = (Player)e.getWhoClicked();
        e.setCancelled(true);
        e.setResult(Result.DENY);
        if (!e.isLeftClick() && e.getRawSlot() == 53) {
            return;
        }
        this.selectedItem(e.getCurrentItem(), p);
    }

    void selectedItem(final ItemStack select, final Player p) {
        final MPlayer mplayer = MPlayer.get(p);
        final String nome_da_facção = mplayer.getFactionName();
        int custo_money = Integer.valueOf(Main.get().getConfig().getInt("Config.Custo_De_Compra_Money"));
        int custo_cash = Integer.valueOf(Main.get().getConfig().getInt("Config.Custo_De_Compra_Cash"));
        if (mplayer.hasFaction() && mplayer.getRole().equals(Rel.LEADER)) {
        	
        }
        if (select.getType().equals(Material.ARROW) && select.getItemMeta().getDisplayName().equals("§cSair")) {
        	p.closeInventory();
        	return;
        }
        if (select.getType().equals(Material.IRON_INGOT) && select.getItemMeta().getDisplayName().equals("§cMoedas")) {
        	p.sendMessage("§cVocê não pode comprar o baú da facção com §2$ §csomente com cash.");
        	p.closeInventory();
        	return;
        }
        if (select.getType().equals(Material.GOLD_INGOT) && select.getItemMeta().getDisplayName().equals("§cCash")) {
        	p.sendMessage("§cVocê não pode comprar o baú da facção com §6Cash §csomente com cash.");
        	p.closeInventory();
        	return;
        }
        if (select.getType().equals(Material.IRON_INGOT) && select.getItemMeta().getDisplayName().equals("§aMoedas")) {
        	if (!InventoryUtils.isMoney(p, custo_money)) {
                p.sendMessage("§cVoc\u00ea n\u00e3o tem §2$ §csuficiente para comprar o baú da facção.");
            	p.closeInventory();
                return;
        	}
            for (final Player ps : mplayer.getFaction().getOnlinePlayers()) {
                if (!ps.getName().equals(p.getName())) {
                    ps.sendMessage("§a" + p.getName() + " comprou o ba\u00fa da fac\u00e7\u00e3o!");
                    ps.playSound(ps.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
                }
            }
            InventoryUtils.removeMoney(p, custo_money);
            p.sendMessage("§aVocê comprou o baú da sua facção com sucesso, você pagou um total de §l" + format(custo_money) + " §2$ §abasta digitar o comando novamente para abrir o baú de sua facção!");
            p.closeInventory();
            final Inventory baú = this.chestManager.getChest(nome_da_facção);
            p.openInventory(baú);
        	return;
        }
        if (select.getType().equals(Material.GOLD_INGOT) && select.getItemMeta().getDisplayName().equals("§aCash")) {
        	if (!InventoryUtils.isCash(p, custo_cash)) {
                p.sendMessage("§cVoc\u00ea n\u00e3o tem §6Cash §csuficiente para comprar o baú da facção.");
            	p.closeInventory();
                return;
        	}
            for (final Player ps : mplayer.getFaction().getOnlinePlayers()) {
                if (!ps.getName().equals(p.getName())) {
                    ps.sendMessage("§a" + p.getName() + " comprou o ba\u00fa da fac\u00e7\u00e3o!");
                    ps.playSound(ps.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
                }
            }
            InventoryUtils.removeCash(p, custo_cash);
            p.sendMessage("§aVocê comprou o baú da sua facção com sucesso, você pagou um total de §l" + format(custo_cash) + " §6cash §abasta digitar o comando novamente para abrir o baú de sua facção!");
            p.closeInventory();
            final Inventory baú = this.chestManager.getChest(nome_da_facção);
            p.openInventory(baú);
        	return;
        }
		
	}

    public static String format(final double value) {
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-US"));
        if (value <= 1.0) {
            return numberFormat.format(value).concat(" ").concat("");
        }
        return numberFormat.format(value).concat(" ").concat("").replace(",", ".").replace(" ", "");
    }
}
