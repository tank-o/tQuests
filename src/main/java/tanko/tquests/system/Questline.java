package tanko.tquests.system;

import org.bukkit.entity.Player;
import tanko.tquests.TQuests;

import java.util.*;

public class Questline {
    private final String ID;
    private String name;
    private String description;
    private List<String> quests = new ArrayList<>();
    private List<Reward> rewards = new ArrayList<>();
    private final Map<UUID,Integer> playerProgress = new HashMap<>();

    public Questline(String ID, List<String> quests) {
        this.ID = ID;
        this.quests = quests;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getQuests() {
        return quests;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public Map<UUID,Integer> getPlayerProgress() {
        return playerProgress;
    }

    public Quest getCurrentQuest(Player player){
        int quest = playerProgress.getOrDefault(player.getUniqueId(), 0);
        String questID = quests.get(quest);
        if (questID == null) return null;
        return TQuests.getQuestRegistry().getQuest(questID);
    }

    public void nextQuest(Player player){
        int quest = playerProgress.getOrDefault(player.getUniqueId(), 0);
        playerProgress.put(player.getUniqueId(), quest + 1);
    }

    public void completeQuestline(Player player){
        for (Reward reward : rewards) {
            reward.give(player);
        }
    }

    public void startQuestline(Player player){
        playerProgress.put(player.getUniqueId(), 0);
    }

}
