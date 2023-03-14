package tanko.tquests.system;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import tanko.tquests.TQuests;
import tanko.tquests.persistence.ConfigReader;
import tanko.tquests.persistence.ConfigWriter;

import java.util.*;

public abstract class Step implements Listener {
    protected String ID;
    protected String name;
    protected String description;
    protected Map<UUID,Integer> progress = new HashMap<>();
    protected Quest relatedQuest;
    protected int amount = 0;
    protected boolean autoComplete = true;

    public Step(String ID, Quest relatedQuest){
        this.ID = ID;
        this.relatedQuest = relatedQuest;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addPlayer(Player player){
        progress.put(player.getUniqueId(), 0);
    }

    public void removePlayer(Player player){
        progress.remove(player.getUniqueId());
    }

    public void resetPlayer(Player player){
        progress.put(player.getUniqueId(), 0);
    }

    public Quest getRelatedQuest() {
        return relatedQuest;
    }

    protected void incrementProgress(Player player){
        if (!progress.containsKey(player.getUniqueId())) {
            if (!TQuests.getQuestRegistry().playerHasQuest(relatedQuest,player)) return;
            addPlayer(player);
        }
        if (progress.get(player.getUniqueId()) >= amount) return;
        progress.put(player.getUniqueId(), progress.get(player.getUniqueId()) + 1);
    }

    public boolean complete(Player player){
        if (!progress.containsKey(player.getUniqueId())) return false;
        if (progress.get(player.getUniqueId()) < amount) return false;
        relatedQuest.nextStep(player);
        return true;
    }

    public boolean save(ConfigurationSection section){
        try {
            ConfigWriter.writeStepProgress(this, progress);
            section.set("amount", amount);
            section.set("autoComplete", autoComplete);
            saveCustomData(section);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean load(ConfigurationSection section){
        try {
            progress = ConfigReader.readStepProgress(ID);
            amount = section.getInt("amount");
            autoComplete = section.getBoolean("autoComplete");
            loadCustomData(section);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public abstract void loadCustomData(ConfigurationSection section);

    public abstract void saveCustomData(ConfigurationSection section);

    public abstract void handleCommand(Player player, String[] args);

    public abstract void viewInfo(Player player);
}
