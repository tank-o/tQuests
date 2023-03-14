package tanko.tquests.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import tanko.tquests.system.Quest;

public class GiveItemStep extends CitizenStep {
    private ItemStack item = null;

    public GiveItemStep(String ID,Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    public ItemStack getItem() {
        return item;
    }

    @EventHandler
    public void onItemGive(NPCRightClickEvent event){
        if (this.npc == null || this.item == null || this.amount == 0) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (npc == this.npc){
            if (player.getInventory().containsAtLeast(item, amount)){
                player.getInventory().removeItem(new ItemStack(item.getType(), amount));
                complete(player);
            }
        }
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        item = section.getItemStack("item");
        npc = CitizensAPI.getNPCRegistry().getById(section.getInt("npc"));
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("item", item);
        section.set("npc", npc.getId());
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        super.handleCommand(player, args);
    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("§6Item: §e" + item.getType());
        player.sendMessage("§6Amount: §e" + amount);
    }
}
