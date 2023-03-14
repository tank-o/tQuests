package tanko.tquests.steps;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

public class KillMobStep extends Step {
    private EntityType mob = null;

    public KillMobStep(String ID, Quest quest) {
        super(ID,quest);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if (player == null) return;
        if (mob == null || amount == 0){
            player.sendMessage("Step not configured properly - mob or amount not set");
        }
        if (event.getEntity().getType() != mob) return;
        if (incrementProgress(player)){
            player.sendMessage("Progress: " + progress.get(player.getUniqueId()) + "/" + amount);
        }
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        String mobName = section.getString("mob");
        if (mobName == null){
            mob = null;
            return;
        }
        mob = EntityType.valueOf(mobName);
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("mob", mob.toString());
    }

    @Override
    public void handleCommand(Player player, String[] args) {

    }

    @Override
    public void viewInfo(Player player) {

    }
}
