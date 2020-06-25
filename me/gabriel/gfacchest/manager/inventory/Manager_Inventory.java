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
                    final String fac��o = chestFileName.substring(0, chestFileName.length() - Manager_Inventory.YAML_EXTENSION_LENGTH);
                    this.chests.put(fac��o, InventoryIO.loadFromYaml(chestFile));
                }
                catch (IllegalArgumentException e) {
                }
            }
            catch (Exception e2) {
                Bukkit.getConsoleSender().sendMessage("�c[gFacChest] N�o foi poss�vel carregar o ba� da fac��o <fac>".replace("<fac>", chestFileName));
            }
        }
        Bukkit.getConsoleSender().sendMessage("�a[gFacChest] Foi carregado um total de <i> ba�(s) de fac��es".replace("<i>", String.valueOf(this.chests.size())));
    }

    public int save() {
        int savedChests = 0;
        this.dataFolder.mkdirs();
        final Iterator<Map.Entry<String, Inventory>> chestIterator = this.chests.entrySet().iterator();
        while (chestIterator.hasNext()) {
            final Map.Entry<String, Inventory> entry = chestIterator.next();
            final String fac��o = entry.getKey();
            final Inventory chest = entry.getValue();
            final File chestFile = new File(this.dataFolder, fac��o + ".chest.yml");
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
                    Bukkit.getConsoleSender().sendMessage("�c[gFacChest] N�o foi poss�vel salvar o ba� da fac��o <fac>, mande o c�digo do erro para o Gabriel Alves ".replace("<fac>", chestFile.getName()) + e);
                }
            }
        }
        return savedChests;
    }
    
    public boolean isChest(final String fac��o) {
        Inventory chest = this.chests.get(fac��o);
        if (chest == null) {
        	return false;
        }
        return true;
    }
    
    public Inventory getChest(final String fac��o) {
        Inventory chest = this.chests.get(fac��o);
        if (chest == null) {
            chest = Bukkit.getServer().createInventory((InventoryHolder)null, 54);
            this.chests.put(fac��o, chest);
        }
        return chest;
    }

    public void removeChest(final String fac��o) {
        this.chests.put(fac��o, null);
    }
    
    public void deleteChest(final String fac��o) {
        if (!this.isChest(fac��o)) {
            return;
        }
        this.removeChest(fac��o);
        final File chestFile = new File(this.dataFolder, String.valueOf(fac��o) + Manager_Inventory.YAML_CHEST_EXTENSION);
        chestFile.delete();
    }
}
