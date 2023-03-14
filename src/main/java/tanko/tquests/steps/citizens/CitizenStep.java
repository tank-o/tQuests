package tanko.tquests.steps.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Step;

public abstract class CitizenStep extends Step {
    protected NPC npc = null;

    public CitizenStep(String ID, Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    @Override
    public boolean save(ConfigurationSection section){
        try {
            super.save(section);
            section.set("npc", npc.getId());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean load(ConfigurationSection section){
        try {
            super.load(section);
            npc = CitizensAPI.getNPCRegistry().getById(section.getInt("npc"));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /step set <var> <value>");
            return;
        }
        String var = args[0];
        switch (var) {
            case "npc" -> {
                // Use the NPC selector to select an NPC
                if (CitizensAPI.getDefaultNPCSelector().getSelected(player) == null) {
                    player.sendMessage("§cYou must select an NPC first!");
                    return;
                }
                npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
                player.sendMessage("§aNPC set to " + npc.getName());
            }
            default -> player.sendMessage("Usage: /step set <var> <value>");
        }
    }

}
