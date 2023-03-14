package tanko.tquests.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import tanko.tquests.TQuests;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Questline;
import tanko.tquests.system.Reward;
import tanko.tquests.system.Step;

import java.util.Map;
import java.util.UUID;

public final class ConfigWriter {

    public static void writeQuestline(Questline questline){
        ConfigurationSection questlineSection = QuestlinesFile.getFile().createSection(questline.getID());
        questlineSection.set("name", questline.getName());
        questlineSection.set("description", questline.getDescription());
        questlineSection.set("quests", questline.getQuests());
        ConfigurationSection playerProgressSection = questlineSection.createSection("playerProgress");
        for (Map.Entry<UUID, Integer> entry : questline.getPlayerProgress().entrySet()) {
            playerProgressSection.set(entry.getKey().toString(), entry.getValue());
        }
        ConfigurationSection rewardsSection = questlineSection.createSection("rewards");
        for (Reward reward : questline.getRewards()) {
            ConfigurationSection rewardSection = rewardsSection.createSection(reward.getID());
            reward.save(rewardSection);
        }
        QuestlinesFile.save();
    }

    public static void writeQuest(Quest quest){
        ConfigurationSection questSection = QuestsFile.getFile().createSection(quest.getID());
        questSection.set("name", quest.getName());
        questSection.set("description", quest.getDescription());
        ConfigurationSection stepsSection = questSection.createSection("steps");
        for (Step step : quest.getSteps()) {
            ConfigurationSection stepSection = stepsSection.createSection(step.getID());
            stepSection.set("type", TQuests.getComponentManager().getStepID(step));
            if (!step.save(stepSection)){
                Bukkit.getLogger().warning("Failed to save step: " + step.getID());
            }
        }
        ConfigurationSection rewardsSection = questSection.createSection("rewards");
        for (Reward reward : quest.getRewards()) {
            ConfigurationSection rewardSection = rewardsSection.createSection(reward.getID());
            rewardSection.set("type", TQuests.getComponentManager().getRewardID(reward));
            reward.save(rewardSection);
        }
        writeQuestProgress(quest);
        QuestsFile.save();
    }

    private static void writeQuestProgress(Quest quest){
        ConfigurationSection questSection = ProgressFile.getFile().createSection("quests." + quest.getID());
        for (Map.Entry<UUID, Integer> entry : quest.getStepProgress().entrySet()) {
            questSection.set(entry.getKey().toString(), entry.getValue());
        }
        ProgressFile.save();
    }

    public static void writeStepProgress(Step step,Map<UUID,Integer> stepProgress) {
        ConfigurationSection stepSection = ProgressFile.getFile().createSection("steps." + step.getID());
        for (Map.Entry<UUID, Integer> entry : stepProgress.entrySet()) {
            stepSection.set(entry.getKey().toString(), entry.getValue());
        }
        ProgressFile.save();
    }
}
