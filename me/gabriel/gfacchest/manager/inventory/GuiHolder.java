package me.gabriel.gfacchest.manager.inventory;

import org.bukkit.inventory.*;

class GuiHolder implements InventoryHolder
{
    private Menu type;
    
    public GuiHolder(final Menu type) {
        this.type = type;
    }
    
    public Menu getType() {
        return this.type;
    }
    
    public Inventory getInventory() {
        return null;
    }
    
    enum Menu
    {
        Menu_Comprar_Bau("Menu_Comprar_Bau", 0), 
        Confirmar_Compra("Confirmar_Compra", 1);
        
        private Menu(final String s, final int n) {
        }
    }
}
