package me.gabriel.gfacchest.manager.inventory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Manager_Inventory {
	private static final String YAML_CHEST_EXTENSION = ".chest.yml";
    private static final int YAML_EXTENSION_LENGTH;
    private final File dataFolder;
    private final HashMap<String, Inventory> chests;

    static {
        YAML_EXTENSION_LENGTH = ".chest.yml".length();
    }
    
    public Manager_Inventory (final File dataFolder) {
        this.dataFolder = dataFolder;
        this.chests = new HashMap<String, Inventory>();
        this.load();
    }

    private void load() {
        this.dataFolder.mkdirs();
        final FilenameFilter filter = new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".chest.yml");
            }
        };
        for (final File chestFile : this.dataFolder.listFiles(filter)) {
            final String chestFileName = chestFile.getName();
            try {
                try {
                    final String facção = chestFileName.substring(0, chestFileName.length() - Manager_Inventory.YAML_EXTENSION_LENGTH);
                    this.chests.put(facção, InventoryIO.loadFromYaml(chestFile));
                }
                catch (IllegalArgumentException e) {
                }
            }
            catch (Exception e2) {
                Bukkit.getConsoleSender().sendMessage("§c[gFacChest] Não foi possível carregar o baú da facção <fac>".replace("<fac>", chestFileName));
            }
        }
        Bukkit.getConsoleSender().sendMessage("§a[gFacChest] Foi carregado um total de <i> baú(s) de facções".replace("<i>", String.valueOf(this.chests.size())));
    }

    public int save() {
        int savedChests = 0;
        this.dataFolder.mkdirs();
        final Iterator<Map.Entry<String, Inventory>> chestIterator = this.chests.entrySet().iterator();
        while (chestIterator.hasNext()) {
            final Map.Entry<String, Inventory> entry = chestIterator.next();
            final String facção = entry.getKey();
            final Inventory chest = entry.getValue();
            final File chestFile = new File(this.dataFolder, facção + ".chest.yml");
            if (chest == null) {
                chestFile.delete();
                chestIterator.remove();
            }
            else {
                try {
                    InventoryIO.saveToYaml(chest, chestFile);
                    ++savedChests;
                }
                catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§c[gFacChest] Não foi possível salvar o baú da facção <fac>, mande o código do erro para o Gabriel Alves ".replace("<fac>", chestFile.getName()) + e);
                }
            }
        }
        return savedChests;
    }
    
    public boolean isChest(final String facção) {
        Inventory chest = this.chests.get(facção);
        if (chest == null) {
        	return false;
        }
        return true;
    }
    
    public Inventory getChest(final String facção) {
        Inventory chest = this.chests.get(facção);
        if (chest == null) {
            chest = Bukkit.getServer().createInventory((InventoryHolder)null, 54);
            this.chests.put(facção, chest);
        }
        return chest;
    }

    public void removeChest(final String facção) {
        this.chests.put(facção, null);
    }
    
    public void deleteChest(final String facção) {
        if (!this.isChest(facção)) {
            return;
        }
        this.removeChest(facção);
        final File chestFile = new File(this.dataFolder, String.valueOf(facção) + Manager_Inventory.YAML_CHEST_EXTENSION);
        chestFile.delete();
    }
}
