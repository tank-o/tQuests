package tanko.tquests;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tquests.persistence.ProgressFile;
import tanko.tquests.system.Quest;

import java.util.*;

public final class QuestRegistry {
    private final Map<UUID, List<String>> questsCompleted = new HashMap<>();
    private final Map<UUID, List<String>> questsInProgress = new HashMap<>();
    private final Map<String, Quest> quests = new HashMap<>();

    public QuestRegistry(){
        ConfigurationSection completedSection = ProgressFile.getFile().getConfigurationSection("completed");
        if (completedSection == null) completedSection = ProgressFile.getFile().createSection("completed");
        ConfigurationSection inProgressSection = ProgressFile.getFile().getConfigurationSection("in_progress");
        if (inProgressSection == null) inProgressSection = ProgressFile.getFile().createSection("in_progress");
        if (!completedSection.getKeys(false).isEmpty()) {
            for (String player : completedSection.getKeys(false)) {
                questsCompleted.put(UUID.fromString(player), completedSection.getStringList(player));
            }
        }
        if (!inProgressSection.getKeys(false).isEmpty()) {
            for (String player : inProgressSection.getKeys(false)) {
                questsInProgress.put(UUID.fromString(player), inProgressSection.getStringList(player));
            }
        }
        ProgressFile.save();
    }

    public void registerQuest(Quest quest){
        quests.put(quest.getID(),quest);
    }

    public void deregisterQuest(String ID){
        quests.remove(ID);
        Quest quest = quests.get(ID);
        for (UUID player : questsCompleted.keySet()) {
            questsCompleted.get(player).remove(quest.getID());
            questsInProgress.get(player).remove(quest.getID());
        }
    }

    public void completeQuest(Quest quest, UUID player){
        questsCompleted.computeIfAbsent(player, k -> new ArrayList<>());
        if (questsInProgress.get(player).contains(quest.getID())){
            questsInProgress.get(player).remove(quest.getID());
            questsCompleted.get(player).add(quest.getID());
        }
    }

    public boolean startQuest(Quest quest, Player player){
        try {
            UUID uuid = player.getUniqueId();
            questsInProgress.computeIfAbsent(uuid, k -> new ArrayList<>());
            questsCompleted.computeIfAbsent(uuid, k -> new ArrayList<>());
            if (questsCompleted.get(uuid).contains(quest.getID()))
                throw new IllegalArgumentException("§cYou have already completed quest!");
            if (questsInProgress.get(uuid).contains(quest.getID()))
                throw new IllegalArgumentException("§cYou are already doing this quest!");
            questsInProgress.get(uuid).add(quest.getID());
            quest.accept(player);
            return true;
        } catch (IllegalArgumentException e){
            player.sendMessage(e.getMessage());
            return false;
        }
    }

    public boolean playerHasQuest(Quest quest, Player player){
        if (questsInProgress.get(player.getUniqueId()) == null) return false;
        return questsInProgress.get(player.getUniqueId()).contains(quest.getID());
    }

    public boolean playerHasCompletedQuest(Quest quest, Player player){
        if (questsCompleted.get(player.getUniqueId()) == null) return false;
        return questsCompleted.get(player.getUniqueId()).contains(quest.getID());
    }

    public void resetQuest(Quest quest, Player player){
        questsInProgress.get(player.getUniqueId()).remove(quest.getID());
        quest.resetQuest(player);
    }

    public List<String> getCompletedQuests(Player player){
        return questsCompleted.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public List<String> getInProgressQuests(Player player){
        return questsInProgress.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public Quest getQuest(String ID){
        return quests.get(ID);
    }

    public List<Quest> getQuests(){
        return new ArrayList<>(quests.values());
    }

    public void savePlayerData(){
        ConfigurationSection completedSection = ProgressFile.getFile().getConfigurationSection("completed");
        ConfigurationSection inProgressSection = ProgressFile.getFile().getConfigurationSection("in_progress");
        for (UUID player : questsCompleted.keySet()) {
            completedSection.set(player.toString(), questsCompleted.get(player));
        }
        for (UUID player : questsInProgress.keySet()) {
            inProgressSection.set(player.toString(), questsInProgress.get(player));
        }
        ProgressFile.save();
    }
}
