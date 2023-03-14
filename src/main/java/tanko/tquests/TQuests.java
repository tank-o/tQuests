package tanko.tquests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tanko.tinteractions.TInteractions;
import tanko.tquests.commands.QuestCommand;
import tanko.tquests.commands.RewardCommand;
import tanko.tquests.commands.StepCommand;
import tanko.tquests.persistence.*;
import tanko.tquests.system.Condition;
import tanko.tquests.system.Quest;
import tanko.tquests.system.Reward;
import tanko.tquests.system.Step;
import tanko.tquests.tInteractions.QuestInteraction;
import tanko.tquests.tInteractions.QuestRequirement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TQuests extends JavaPlugin {
    private static QuestRegistry questRegistry;
    private static ComponentManager componentManager;
    private static TQuests instance;

    private static boolean citizensEnabled = false;
    private static boolean tInteractionsEnabled = false;

    private static final Map<UUID,Quest> selectedQuests = new HashMap<>();
    private static final Map<UUID,Step> selectedSteps = new HashMap<>();
    private static final Map<UUID,Reward> selectedRewards = new HashMap<>();
    private static final Map<UUID, Condition> selectedConditions = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        //Commands
        getCommand("quest").setExecutor(new QuestCommand());
        getCommand("step").setExecutor(new StepCommand());
        getCommand("reward").setExecutor(new RewardCommand());

        // Setup Data Files
        QuestsFile.setup(this);
        QuestlinesFile.setup(this);
        ProgressFile.setup(this);

        // Plugin startup logic
        questRegistry = new QuestRegistry();
        componentManager = new ComponentManager();

        // Check if Citizens is in the server and enabled
        if (Bukkit.getPluginManager().getPlugin("Citizens") != null){
            if (Bukkit.getPluginManager().getPlugin("Citizens").isEnabled()){
                Bukkit.getLogger().info("Citizens found, enabling Citizens support");
                citizensEnabled = true;
                componentManager.registerCitizensSteps();
            } else {
                Bukkit.getLogger().warning("Citizens not found, types that require Citizens will not be registered");
            }
        }

        // Register Interactions
        if (Bukkit.getPluginManager().getPlugin("TInteractions") == null){
            if (Bukkit.getPluginManager().getPlugin("TInteractions").isEnabled()){
                Bukkit.getLogger().info("TInteractions found, enabling TInteractions support");
                tInteractionsEnabled = true;
                TInteractions.getInteractionRegistry().registerRequirement("quest", QuestRequirement.class);
                TInteractions.getInteractionRegistry().registerInteraction("quest", QuestInteraction.class);
            }
        }

        ConfigReader.readQuests();
    }

    @Override
    public void onDisable() {
        for (Quest quest : questRegistry.getQuests()) {
            Bukkit.getLogger().info("Saving quest: " + quest.getID());
            ConfigWriter.writeQuest(quest);
        }
        questRegistry.savePlayerData();
    }

    public static QuestRegistry getQuestRegistry(){
        return questRegistry;
    }

    public static TQuests getInstance(){
        return instance;
    }

    public static ComponentManager getComponentManager(){
        return componentManager;
    }

    public static boolean isCitizensEnabled(){
        return citizensEnabled;
    }

    public static boolean isTInteractionsEnabled(){
        return tInteractionsEnabled;
    }

    public static Quest getSelectedQuest(Player player){
        return selectedQuests.get(player.getUniqueId());
    }

    public static void setSelectedQuest(Player player, Quest quest){
        selectedQuests.put(player.getUniqueId(),quest);
    }

    public static Step getSelectedStep(Player player){
        return selectedSteps.get(player.getUniqueId());
    }

    public static void setSelectedStep(Player player, Step step){
        selectedSteps.put(player.getUniqueId(),step);
    }

    public static Reward getSelectedReward(Player player){
        return selectedRewards.get(player.getUniqueId());
    }

    public static void setSelectedReward(Player player, Reward reward){
        selectedRewards.put(player.getUniqueId(),reward);
    }

    public static Condition getSelectedCondition(Player player){
        return selectedConditions.get(player.getUniqueId());
    }

    public static void setSelectedCondition(Player player, Condition condition){
        selectedConditions.put(player.getUniqueId(),condition);
    }
}
