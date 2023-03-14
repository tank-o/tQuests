package tanko.tquests.steps.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import tanko.tquests.system.Quest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCTalkStep extends CitizenStep {
    List<String> messages = new ArrayList<>();

    public NPCTalkStep(String ID, Quest relatedQuest) {
        super(ID, relatedQuest);
    }

    @EventHandler
    public void onNPCInteract(NPCRightClickEvent event){
        if (npc == null || messages.isEmpty()) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (!(progress.containsKey(player.getUniqueId()))) return;
        if (npc != this.npc) return;
        String message = messages.get(progress.get(player.getUniqueId()));
        player.sendMessage(progressString(player)+ ChatColor.YELLOW + npc.getName() + ": " + ChatColor.WHITE + message);
        incrementProgress(player);
    }

    @Override
    public void loadCustomData(ConfigurationSection section) {
        messages = section.getStringList("messages");
    }

    @Override
    public void saveCustomData(ConfigurationSection section) {
        section.set("messages", messages);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        super.handleCommand(player, args);
        String var = args[0];
        switch (var) {
            case "message" -> {
                String sub = args[1];
                switch (sub) {
                    case "add" -> {
                        // Join the rest of the args together and put it into one string
                        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                        messages.add(message);
                        amount++;
                        player.sendMessage("§aAdded message: " + message);
                    }
                    case "remove" -> {
                        int index = Integer.parseInt(args[2]);
                        messages.remove(index);
                        amount--;
                        player.sendMessage("§aRemoved message at index " + index);
                    }
                }
            }
            default -> player.sendMessage("§cInvalid command " + var);
        }
    }

    @Override
    public void viewInfo(Player player) {
        player.sendMessage("§aMessages:");
        for (int i = 0; i < messages.size(); i++) {
            player.sendMessage("§8[" + i + "] " + messages.get(i));
        }
        player.sendMessage("§aNPC: " + npc.getName());
    }

    private String progressString(Player player){
        return "§8[" + (progress.get(player.getUniqueId()) + 1) + "/" + messages.size() + "]";
    }
}
