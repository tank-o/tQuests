package tanko.tquests.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import tanko.tquests.TQuests;
import tanko.tquests.system.Condition;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Reward;
import tanko.tquests.system.Step;

import java.util.*;

public final class ConfigReader {

    public static void readQuests(){
        ConfigurationSection questsSection = QuestsFile.getFile();
        for (String questID : questsSection.getKeys(false)) {
            try {
                Bukkit.getLogger().info("Reading quest: " + questID);
                ConfigurationSection questSection = questsSection.getConfigurationSection(questID);
                if (questSection == null) continue;
                Quest quest = readQuest(questSection);
                TQuests.getQuestRegistry().registerQuest(quest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Quest readQuest(ConfigurationSection questSection){
        String ID = questSection.getName();
        String name = questSection.getString("name");
        String description = questSection.getString("description");
        List<Reward> rewards = new ArrayList<>();
        ConfigurationSection rewardsSection = questSection.getConfigurationSection("rewards");
        for (String rewardID : rewardsSection.getKeys(false)) {
            try {
                ConfigurationSection rewardSection = rewardsSection.getConfigurationSection(rewardID);
                String type = rewardSection.getString("type");
                Class<? extends Reward> rewardClass = TQuests.getComponentManager().getReward(type);
                Reward reward = rewardClass.getConstructor(String.class).newInstance(rewardID);
                rewardClass.cast(reward);
                reward.load(rewardSection);
                rewards.add(reward);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        ConfigurationSection conditionsSection = questSection.getConfigurationSection("conditions");
        List<Condition> conditions = new ArrayList<>();
        for (String conditionID : conditionsSection.getKeys(false)) {
            try {
                ConfigurationSection conditionSection = conditionsSection.getConfigurationSection(conditionID);
                String type = conditionSection.getString("type");
                Class<? extends Condition> conditionClass = TQuests.getComponentManager().getCondition(type);
                Condition condition = conditionClass.getConstructor(String.class).newInstance(conditionID);
                conditionClass.cast(condition);
                condition.load(conditionSection);
                conditions.add(condition);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Map<UUID,Integer> playerProgress = readQuestProgress(ID);
        Quest quest = new Quest(ID,name,description,rewards,conditions,playerProgress);
        ConfigurationSection stepsSection = questSection.getConfigurationSection("steps");
        for (String stepID : stepsSection.getKeys(false)) {
            try {
                ConfigurationSection stepSection = stepsSection.getConfigurationSection(stepID);
                if (stepSection == null) continue;
                String type = stepSection.getString("type");
                Class<? extends Step> stepClass = TQuests.getComponentManager().getStep(type);
                Step step = stepClass.getConstructor(String.class,Quest.class).newInstance(stepID,quest);
                stepClass.cast(step);
                if (!step.load(stepSection)){
                    Bukkit.getLogger().warning("Failed to load step: " + stepID);
                    continue;
                }
                quest.addStep(step);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return quest;
    }

    private static Map<UUID,Integer> readQuestProgress(String questID){
        ConfigurationSection playerProgressSection = ProgressFile.getFile().getConfigurationSection("quests." + questID);
        Map<UUID,Integer> playerProgress = new HashMap<>();
        for (String playerID : playerProgressSection.getKeys(false)) {
            int progress = playerProgressSection.getInt(playerID);
            playerProgress.put(UUID.fromString(playerID), progress);
        }
        return playerProgress;
    }

    public static Map<UUID,Integer> readStepProgress(String stepID){
        ConfigurationSection playerProgressSection = ProgressFile.getFile().getConfigurationSection("steps." + stepID);
        Map<UUID,Integer> playerProgress = new HashMap<>();
        for (String playerID : playerProgressSection.getKeys(false)) {
            int progress = playerProgressSection.getInt(playerID);
            playerProgress.put(UUID.fromString(playerID), progress);
        }
        return playerProgress;
    }
}

