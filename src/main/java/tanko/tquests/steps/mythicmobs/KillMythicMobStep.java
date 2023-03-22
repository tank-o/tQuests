package tanko.tquests.steps.mythicmobs;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

public class KillMythicMobStep extends Step {
    private String mobName = null;

    public KillMythicMobStep(String ID, Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    @EventHandler
    public void onMobKill(MythicMobDeathEvent event){
        LivingEntity entity = event.getKiller();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (mobName == null || amount == 0) return;
        if (!event.getMobType().getInternalName().equals(mobName)) return;
        if (incrementProgress(player)){
            player.sendMessage("Progress: " + progress.get(player.getUniqueId()) + "/" + amount);
        }
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        mobName = section.getString("mob");
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("mob", mobName);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String var = args[0];
        switch (var) {
            case "mob" -> {
                if (args.length < 2) {
                    player.sendMessage("Usage: /step set mob <mobID>");
                    return;
                }
                String mobName = args[1];
                // Check that mob exists
                if (MythicBukkit.inst().getMobManager().getMythicMob(mobName).isEmpty()) {
                    player.sendMessage("Mob " + mobName + " does not exist");
                    return;
                }
                this.mobName = mobName;
            }
            case "amount" -> {
                if (args.length < 2) {
                    player.sendMessage("Usage: /step set amount <value>");
                    return;
                }
                try {
                    int amount = Integer.parseInt(args[1]);
                    if (amount <= 0) {
                        player.sendMessage("§cYou can't set the amount to be less than 1");
                        return;
                    }
                    setAmount(amount);
                    player.sendMessage("Amount set to " + amount);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cError setting amount, most likely not a number");
                }
            }
            default -> player.sendMessage("Unknown variable " + var);
        }
    }

    @Override
    public void viewInfo(Player player) {

    }
}
