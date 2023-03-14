package tanko.tquests.steps;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

public class ObtainItemStep extends Step {
    private int amount = 0;
    private ItemStack item = null;

    public ObtainItemStep(String ID, Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @EventHandler
    public void onItemObtain(EntityPickupItemEvent event){
        if (this.item == null || this.amount == 0) return;
        if (!event.getItem().getItemStack().isSimilar(item)) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (incrementProgress(player)) {
            player.sendMessage("Progress: " + progress.get(player.getUniqueId()) + "/" + amount);
        }
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        item = section.getItemStack("item");
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("item", item);
    }

    @Override
    public void handleCommand(Player player, String[] args) {

    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("§6§l" + "Item: " + item.getType());
        player.sendMessage("§6§l" + "Amount: " + amount);
    }
}
