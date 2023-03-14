package tanko.tquests.steps;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

public class BreakBlockStep extends Step {
    private Material material = null;

    public BreakBlockStep(String ID,Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!(progress.containsKey(player.getUniqueId()))) return;
        if (event.getBlock().getType() != material) return;
        if (incrementProgress(player)) {
            player.sendMessage("Progress: " + progress.get(player.getUniqueId()) + "/" + amount);
        }
    }

    public boolean setBlock(Material material) {
        if (material.isBlock()) {
            this.material = material;
            return true;
        }
        return false;
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        String materialName = section.getString("material");
        if (materialName == null){
            material = null;
            return;
        }
        material = Material.getMaterial(materialName);
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("material", material.toString());
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /step set <var> <value>");
            return;
        }
        String var = args[0];
        switch (var) {
            case "block" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType().equals(Material.AIR)) {
                    player.sendMessage("You must be holding a block in your main hand");
                    return;
                }
                if (setBlock(item.getType())) {
                    player.sendMessage("Block set to " + item.getType().toString());
                } else {
                    player.sendMessage("§cError setting block");
                }

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
        }
    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("§6Block: §e" + material);
        player.sendMessage("§6Amount: §e" + amount);
    }
}
